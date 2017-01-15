package com.soongwei.shareme.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.fragments.PhotoFragment;
import com.soongwei.shareme.objects.PhoneAlbum;

import java.util.Vector;


public class PhotoPagerAdapter extends FragmentPagerAdapter {
    private Vector<PhoneAlbum> albums;
    public PhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return PhotoFragment.newInstance(Long.valueOf(position), StringUtils.convertObjectToString(albums.get(position)));
    }

    public void setDatas(Vector<PhoneAlbum> albums){
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return albums.get(position).getName();
    }
}
