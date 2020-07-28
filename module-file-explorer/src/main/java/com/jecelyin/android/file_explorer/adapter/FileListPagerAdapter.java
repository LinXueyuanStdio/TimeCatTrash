package com.jecelyin.android.file_explorer.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jecelyin.android.file_explorer.FileListPagerFragment;
import com.jecelyin.android.file_explorer.io.JecFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileListPagerAdapter extends FragmentPagerAdapter {
    private final List<JecFile> pathList;
    private FileListPagerFragment mCurrentFragment;

    public FileListPagerFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public FileListPagerAdapter(FragmentManager fm) {
        super(fm);
        this.pathList = new ArrayList<>();
    }

    public void addPath(JecFile path) {
        pathList.add(path);
        notifyDataSetChanged();
    }

    public void removePath(JecFile path) {
        pathList.remove(path);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pathList == null ? 0 : pathList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return FileListPagerFragment.newFragment(pathList.get(position));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentFragment = (FileListPagerFragment) object;
    }
}
