package com.timecat.module.editor.markdown;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.timecat.component.commonbase.utils.FileUtils;
import com.timecat.component.data.model.events.RefreshTitleEvent;
import com.timecat.component.readonly.Constants;
import com.timecat.component.setting.DEF;
import com.timecat.module.editor.R;
import com.timecat.module.editor.R2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class EditorFragment extends BaseEditorFragment {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.editor_viewpager)
    ViewPager editorViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_editor;
    }

    @Override
    public void initView() {
        getArgs();
        context.setSupportActionBar(toolbar);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white_trans60));

        setHasOptionsMenu(true);
        setViewPager();
        setViewPagerListener();
    }

    public void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            fromFile = args.getBoolean(Constants.BUNDLE_KEY_FROM_FILE);
            fromNote = args.getBoolean(Constants.BUNDLE_KEY_FROM_NOTE);
            if (fromFile) {
                saved = args.getBoolean(Constants.BUNDLE_KEY_SAVED);
                fileName = args.getString(Constants.BUNDLE_KEY_FILE_NAME);
                filePath = args.getString(Constants.BUNDLE_KEY_FILE_PATH);
                if (filePath != null) {
                    fileContent = FileUtils.readContentFromPath(filePath, true);
                }
            } else {
                fileContent = DEF.config().getString(Constants.UNIVERSAL_SAVE_COTENT, "");
            }
        }
        if (!fromFile) {
            saved = false;
            fileName = null;
            filePath = null;
            fileContent = null;
        }
        toolbar.setTitle(fileName == null ? "" : fileName);
        toolbar.setSubtitle(fileContent == null ? "0" : "" + fileContent.length());
    }

    public void setViewPager() {
        final ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(
                getChildFragmentManager());
        editorViewPager.setAdapter(adapter);
    }

    public void setViewPagerListener() {
        editorViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    EventBus.getDefault().post(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EditorAction editorAction = new EditorAction(context);
        int i = item.getItemId();
        if (i == R.id.preview) {// switch to preview page
            editorAction.toggleKeyboard(0);
            editorViewPager.setCurrentItem(1, true);

        } else if (i == R.id.edit) {// switch to edit page
            editorViewPager.setCurrentItem(0, true);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshTitle(RefreshTitleEvent event) {
        if (event.title != null && !event.title.equals("")) {
            toolbar.setTitle(event.title);
            toolbar.setSubtitle(String.valueOf(event.contentLength));
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0) {
                fragment = new EditFragment();
            } else {
                fragment = new PreviewFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
