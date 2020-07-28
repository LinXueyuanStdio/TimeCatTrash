package com.jecelyin.android.file_explorer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jecelyin.android.file_explorer.adapter.FileListItemAdapter;
import com.jecelyin.android.file_explorer.adapter.PathButtonAdapter;
import com.jecelyin.android.file_explorer.io.JecFile;
import com.jecelyin.android.file_explorer.io.LocalFile;
import com.jecelyin.android.file_explorer.io.RootFile;
import com.jecelyin.android.file_explorer.listener.FileListResultListener;
import com.jecelyin.android.file_explorer.listener.OnClipboardPasteFinishListener;
import com.jecelyin.android.file_explorer.util.FileListSorter;
import com.jecelyin.editor.v2.Pref;
import com.timecat.component.commonbase.listeners.OnItemClickListener;
import com.timecat.component.commonbase.task.JecAsyncTask;
import com.timecat.component.commonbase.task.TaskListener;
import com.timecat.component.commonbase.task.TaskResult;
import com.timecat.component.alert.UIUtils;
import com.timecat.component.commonsdk.listener.OnResultCallback;
import com.timecat.component.commonsdk.utils.utils.RootShellRunner;
import com.timecat.component.commonsdk.utils.utils.command.ShellDaemon;
import com.timecat.component.data.DBHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileListPagerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, FileExplorerView, ExplorerContext, SharedPreferences.OnSharedPreferenceChangeListener {
    private FileListItemAdapter adapter;
    private JecFile path;
    //    private FileExplorerFragmentBinding binding;
    private PathButtonAdapter pathAdapter;
    private boolean isRoot;
    private ScanFilesTask task;
    private FileExplorerAction action;
    View view_frag;
    TextView emptyLayout;
    IndexFastScrollRecyclerView recyclerView;
    SwipeRefreshLayout explorer_swipe_refresh_layout;
    EditText nameFilterEditText;
    ImageView recentPathBtn;
    RecyclerView pathScrollView;

    public static Fragment newFragment(JecFile path) {
        FileListPagerFragment f = new FileListPagerFragment();
        Bundle b = new Bundle();
        b.putParcelable("path", path);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        path = (JecFile) getArguments().getParcelable("path");
        view_frag = LayoutInflater.from(getContext()).inflate(R.layout.file_explorer_fragment, container, false);

        return view_frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emptyLayout = view.findViewById(R.id.emptyLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        explorer_swipe_refresh_layout = view.findViewById(R.id.explorer_swipe_refresh_layout);
        nameFilterEditText = view.findViewById(R.id.nameFilterEditText);
        recentPathBtn = view.findViewById(R.id.recentPathBtn);
        pathScrollView = view.findViewById(R.id.pathScrollView);
        action = new FileExplorerAction(getContext(), this, ((FileExplorerActivity) getActivity()).getFileClipboard(), this);
        adapter = new FileListItemAdapter();
        adapter.setOnCheckedChangeListener(action);
        adapter.setOnItemClickListener(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                    }
                });

            }
        });

        pathScrollView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pathAdapter = new PathButtonAdapter();
        pathAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                pathScrollView.scrollToPosition(pathAdapter.getItemCount() - 1);
            }
        });
        pathAdapter.setPath(path);
        pathAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                JecFile file = pathAdapter.getItem(position);
                switchToPath(file);
            }
        });
        pathScrollView.setAdapter(pathAdapter);

        explorer_swipe_refresh_layout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(getResources().getDimensionPixelSize(R.dimen.file_list_item_divider_left_margin), 0).build());
        explorer_swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                explorer_swipe_refresh_layout.setRefreshing(true);
            }
        });
        nameFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recentPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentPathsMenu menu = new RecentPathsMenu(getContext(), v);
                menu.setOnPathSelectListener(new RecentPathsMenu.OnPathSelectListener() {
                    @Override
                    public void onSelect(String path) {
                        switchToPath(new LocalFile(path));
                    }
                });
                menu.show();
            }
        });

        Pref.getInstance(getContext()).registerOnSharedPreferenceChangeListener(this);

        if (Pref.getInstance(getContext()).isRootEnabled()) {
            new RootShellRunner().isRootAvailable(new OnResultCallback<Boolean>() {
                @Override
                public void onError(String error) {
                    onRefresh();
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (getActivity() == null)
                        return;

                    isRoot = result;
                    onRefresh();
                }
            });
        } else {
            isRoot = false;
            onRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Pref.getInstance(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        if (action != null) {
            action.destroy();
        }
        ShellDaemon.getShell().reset();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 不能加在onPause，因为请求Root UI会导致pause
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.paste_menu) {
            final FileClipboard fileClipboard = ((FileExplorerActivity) getActivity()).getFileClipboard();
            fileClipboard.paste(getContext(), getCurrentDirectory(), new OnClipboardPasteFinishListener() {
                @Override
                public void onFinish(int count, String error) {
                    onRefresh();
                    fileClipboard.showPasteResult(getContext(), count, error);
                }
            });
            item.setVisible(false);
        } else if (item.getItemId() == R.id.add_folder_menu) {
            action.doCreateFolder();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        UpdateRootInfo updateRootInfo = new UpdateRootInfo() {

            @Override
            public void onUpdate(JecFile f) {
                path = f;
            }
        };
        task = new ScanFilesTask(getActivity(), path, isRoot, updateRootInfo);
        task.setTaskListener(new TaskListener<JecFile[]>() {
            @Override
            public void onCompleted() {
                if (explorer_swipe_refresh_layout != null)
                    explorer_swipe_refresh_layout.setRefreshing(false);
            }

            @Override
            public void onSuccess(JecFile[] result) {
                if (adapter != null) {
                    pathAdapter.setPath(path);
                    adapter.setData(result);
                    Pref.getInstance(getContext()).setLastOpenPath(path.getPath());
                    DBHelper.getInstance(getContext()).addRecentPath(path.getPath());
                }

            }

            @Override
            public void onError(Exception e) {
                if (explorer_swipe_refresh_layout == null)
                    return;
                explorer_swipe_refresh_layout.setRefreshing(false);
                final String storage = Environment.getExternalStorageDirectory().getPath();
                UIUtils.showConfirmDialog(getContext()
                        , null
                        , getString(R.string.error_occurred_x, e.getMessage())
                        , new UIUtils.OnClickCallback() {
                            @Override
                            public void onOkClick() {
                                switchToPath(new LocalFile(storage));
                            }
                        }
                        , getString(R.string.goto_x, storage)
                        , getString(android.R.string.cancel)
                                         );
            }
        });
        task.execute();
    }

    @Override
    public void onItemClick(int position, View view) {
        JecFile file = adapter.getItem(position);
        if (!((FileExplorerActivity) getActivity()).onSelectFile(file)) {
            if (file.isDirectory()) {
                switchToPath(file);
            }
        }
    }

    private void switchToPath(JecFile file) {
        path = file;

        onRefresh();
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return ((AppCompatActivity) getActivity()).startSupportActionMode(callback);
    }

    @Override
    public void setSelectAll(boolean checked) {
        adapter.checkAll(checked);
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public JecFile getCurrentDirectory() {
        return path;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(Pref.KEY_LAST_OPEN_PATH))
            onRefresh();
    }

    private static interface UpdateRootInfo {
        public void onUpdate(JecFile path);
    }

    private static class ScanFilesTask extends JecAsyncTask<Void, Void, JecFile[]> {
        private final UpdateRootInfo updateRootInfo;
        private JecFile path;
        private boolean isRoot;
        private final Context context;

        private ScanFilesTask(Context context, JecFile path, boolean isRoot, UpdateRootInfo updateRootInfo) {
            this.context = context.getApplicationContext();
            this.path = path;
            this.isRoot = isRoot;
            this.updateRootInfo = updateRootInfo;
        }

        @Override
        protected void onRun(final TaskResult<JecFile[]> taskResult, Void... params) throws Exception {

            if (!(path instanceof RootFile) && (isRoot || RootShellRunner.isRootPath(path.getPath()))) {
                RootFile.obtain(path.getPath(), new OnResultCallback<RootFile>() {
                    @Override
                    public void onError(String error) {
                        updateList(taskResult);
                    }

                    @Override
                    public void onSuccess(RootFile result) {
                        path = result;
                        updateList(taskResult);
                    }
                });
            } else {
                updateList(taskResult);
            }
        }

        private void updateList(final TaskResult<JecFile[]> taskResult) {
            Pref pref = Pref.getInstance(context);
            final boolean showHiddenFiles = pref.isShowHiddenFiles();
            final int sortType = pref.getFileSortType();
            updateRootInfo.onUpdate(path);
            path.listFiles(new FileListResultListener() {
                @Override
                public void onResult(JecFile[] result) {
                    if (result == null || result.length == 0) {
                        taskResult.setResult(result);
                        return;
                    }
                    if (!showHiddenFiles) {
                        List<JecFile> list = new ArrayList<>(result.length);
                        for (JecFile file : result) {
                            if (file.getName().charAt(0) == '.') {
                                continue;
                            }
                            list.add(file);
                        }
                        result = new JecFile[list.size()];
                        list.toArray(result);
                    }
                    Arrays.sort(result, new FileListSorter(true, sortType, true));
                    taskResult.setResult(result);
                }

                @Override
                public void onError(String error) {
                    taskResult.setError(new Exception(error));
                }
            });
        }
    }
}
