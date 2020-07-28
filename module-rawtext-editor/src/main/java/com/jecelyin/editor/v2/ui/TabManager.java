package com.jecelyin.editor.v2.ui;

import android.database.DataSetObserver;
import androidx.core.view.GravityCompat;
import android.view.View;

import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.adapter.EditorAdapter;
import com.jecelyin.editor.v2.adapter.TabAdapter;
import com.jecelyin.editor.v2.common.TabCloseListener;
import com.timecat.component.data.DBHelper;
import com.jecelyin.editor.v2.utils.ExtGrep;
import com.jecelyin.editor.v2.view.EditorView;
import com.jecelyin.editor.v2.view.TabViewPager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class TabManager implements TabViewPager.OnPageChangeListener, TabManagerAction {
    private final EditorActivity editorActivity;
    private final TabAdapter tabAdapter;
    private EditorAdapter editorAdapter;
    private boolean exitApp;

    public TabManager(EditorActivity activity) {
        this.editorActivity = activity;

        this.tabAdapter = new TabAdapter();
        tabAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabMenuViewsClick(v);
            }
        });
//        tabAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                editorActivity.tabDrawable.setText(String.valueOf(tabAdapter.getItemCount()));
//            }
//        });
        editorActivity.mTabRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity.getContext()).build());
        editorActivity.mTabRecyclerView.setAdapter(tabAdapter);

        initEditor();

        editorActivity.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorActivity.mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        editorActivity.mTabPager.setOnPageChangeListener(this);
//        setCurrentTab(0); //fix can't set last open tab
    }

    private void onTabMenuViewsClick(View v) {
        int i = v.getId();
        if (i == R.id.close_image_view) {
            closeTab((int) v.getTag());

        } else {
            int position = (int) v.getTag();
            editorActivity.closeMenu();
            setCurrentTab(position);

        }
    }

    private void initEditor() {
        editorAdapter = new EditorAdapter(editorActivity);
        editorActivity.mTabPager.setAdapter(editorAdapter); //优先，避免TabAdapter获取不到正确的CurrentItem

        if (Pref.getInstance(editorActivity).isOpenLastFiles()) {
            ArrayList<DBHelper.RecentFileItem> recentFiles = DBHelper.getInstance(editorActivity).getRecentFiles(true);

            File f;
            for (DBHelper.RecentFileItem item : recentFiles) {
                f = new File(item.path);
                if(!f.isFile())
                    continue;
                editorAdapter.newEditor(false, f, item.line, item.column, item.encoding);
                setCurrentTab(editorAdapter.getCount() - 1); //fixme: auto load file, otherwise click other tab will crash by search result
            }
            editorAdapter.notifyDataSetChanged();
            updateTabList();

            int lastTab = Pref.getInstance(editorActivity).getLastTab();
            setCurrentTab(lastTab);
        }

        editorAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateTabList();

                if (!exitApp && editorAdapter.getCount() == 0) {
                    newTab();
                }
            }
        });

        if (editorAdapter.getCount() == 0)
            editorAdapter.newEditor(editorActivity.getString(R.string.new_filename, editorAdapter.countNoFileEditor() + 1), null);
    }

    @Override
    public void newTab() {
        editorAdapter.newEditor(editorActivity.getString(R.string.new_filename, editorAdapter.getCount() + 1), null);
        setCurrentTab(editorAdapter.getCount() - 1);
    }

    public boolean newTab(CharSequence content) {
        editorAdapter.newEditor(editorActivity.getString(R.string.new_filename, editorAdapter.getCount() + 1), content);
        setCurrentTab(editorAdapter.getCount() - 1);
        return true;
    }

    public boolean newTab(ExtGrep grep) {
        editorAdapter.newEditor(grep);
        setCurrentTab(editorAdapter.getCount() - 1);
        return true;
    }

    public boolean newTab(File path, String encoding) {
        return newTab(path, 0, 0, encoding);
    }

    public boolean newTab(File path, int line, int column, String encoding) {
        int count = editorAdapter.getCount();
        for(int i = 0; i < count; i++) {
            EditorDelegate fragment = editorAdapter.getItem(i);
            if(fragment.getPath() == null)
                continue;
            if(fragment.getPath().equals(path.getPath())) {
                setCurrentTab(i);
                return false;
            }
        }
        editorAdapter.newEditor(path, line, column, encoding);
        setCurrentTab(count);
        return true;
    }

    public void setCurrentTab(final int index) {
        editorActivity.mTabPager.setCurrentItem(index);
        tabAdapter.setCurrentTab(index);
        updateToolbar();
    }

    public int getTabCount() {
        if(tabAdapter == null)
            return 0;
        return tabAdapter.getItemCount();
    }

    public int getCurrentTab() {
        return editorActivity.mTabPager.getCurrentItem();
    }

    public void closeTab(int position) {
        editorAdapter.removeEditor(position, new TabCloseListener() {
            @Override
            public void onClose(String path, String encoding, int line, int column) {
                DBHelper.getInstance(editorActivity).updateRecentFile(path, false);
                int currentTab = getCurrentTab();
                if (getTabCount() != 0) {
                    setCurrentTab(currentTab); //设置title等等
                }
//                tabAdapter.setCurrentTab(currentTab);
            }
        });
    }

    @Override
    public EditorAdapter getEditorAdapter() {
        return editorAdapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabAdapter.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updateTabList() {
        tabAdapter.setTabInfoList(editorAdapter.getTabInfoList());
        tabAdapter.notifyDataSetChanged();
    }

    public void updateEditorView(int index, EditorView editorView) {
        editorAdapter.setEditorView(index, editorView);
    }

    public void onDocumentChanged(int index) {
        updateTabList();
        updateToolbar();
    }

    private void updateToolbar() {
        EditorDelegate delegate = editorAdapter.getItem(getCurrentTab());
        if(delegate == null)
            return;
        editorActivity.mToolbar.setTitle(delegate.getToolbarText());
    }

    public boolean closeAllTabAndExitApp() {
        EditorDelegate.setDisableAutoSave(true);
        exitApp = true;
        if (editorActivity.mTabPager != null) {
            Pref.getInstance(editorActivity).setLastTab(getCurrentTab());
        }
        return editorAdapter.removeAll(new TabCloseListener() {
            @Override
            public void onClose(String path, String encoding, int line, int column) {
                DBHelper.getInstance(editorActivity).updateRecentFile(path, encoding, line, column);
                int count = getTabCount();
                if (count == 0) {
                    editorActivity.finish();
                } else {
                    editorAdapter.removeAll(this);
                }
            }
        });
    }
}
