package com.soongwei.shareme.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photosets.Photoset;
import com.googlecode.flickrjandroid.photosets.Photosets;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.soongwei.shareme.MyApplication;
import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.ImageUtils;
import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.activities.SplashActivity;
import com.soongwei.shareme.adapters.PhotoPagerAdapter;
import com.soongwei.shareme.apis.FlickrHelper;
import com.soongwei.shareme.apis.GetOAuthTokenTask;
import com.soongwei.shareme.apis.GetPhotosetsTask;
import com.soongwei.shareme.base.BaseMainFragment;
import com.soongwei.shareme.constants.Constants;
import com.soongwei.shareme.interfaces.OnPhoneImagesObtained;
import com.soongwei.shareme.objects.PhoneAlbum;
import com.soongwei.shareme.objects.PhonePhoto;
import com.soongwei.shareme.objects.PhotoPermissionObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MainFragment extends BaseMainFragment {
    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.viewPager)ViewPager viewPager;
    @BindView(R.id.tabs)AdvancedPagerSlidingTabStrip tabView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private PhotoPagerAdapter adapter;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

//        initViewFromPhone();
        displayView(new Vector<PhoneAlbum>());
        initViewFromFlickr();


    }

    private void initViewFromFlickr(){
        mProgressDialog = ProgressDialog.show(getActivity(),
                "", getResources().getString(R.string.loading)); //$NON-NLS-1$ //$NON-NLS-2$
        mProgressDialog.setCancelable(false);
        GetPhotosetsTask task = new GetPhotosetsTask(getActivity());
        task.execute(FlickrHelper.getOAuthToken(getActivity()).getUser().getId());
    }

    private void initViewFromPhone() {


        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE);
        } else {
            washPhotos();
        }


    }

    private  Vector<PhoneAlbum> flickrDataConverter(Photosets photosets){
        Vector<PhoneAlbum> albums = new Vector<>();

        for(Photoset photoset:photosets.getPhotosets()){ //each albums
            PhoneAlbum phoneAlbum = new PhoneAlbum();
            phoneAlbum.setId(Long.parseLong(photoset.getId()));
            phoneAlbum.setName(photoset.getTitle());
            phoneAlbum.setCoverUri(FlickrHelper.imageUrlGenerator(photoset.getFarm(), photoset.getServer(), photoset.getId(), photoset.getSecret(), "{s}"));

            Vector<PhonePhoto> phonePhotos = new Vector<>();
            for (Photo photo: photoset.getPhotoList()){
                PhonePhoto phonePhoto = new PhonePhoto();
                phonePhoto.setId(Long.parseLong(photo.getId()));
                phonePhoto.setAlbumName(photo.getTitle());
                phonePhoto.setPhotoUri(FlickrHelper.imageUrlGenerator(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret(), "{s}"));
                phonePhoto.setPhotoOriginalUri(FlickrHelper.imageUrlGenerator(photo.getFarm(), photo.getServer(), photo.getId(), photo.getOriginalSecret(), "o"));
                phonePhotos.add(phonePhoto);

            }

            phoneAlbum.setAlbumPhotos(phonePhotos);

            albums.add(phoneAlbum);
        }

        return albums;

    }


    private void washPhotos(){
        ImageUtils.getPhoneAlbums(getActivity(), new OnPhoneImagesObtained() {
            @Override
            public void onComplete(Vector<PhoneAlbum> albums) {
                String[] stockArr = new String[albums.size()];
                for(int t=0; t<albums.size(); t++){
                    stockArr[t] = albums.get(t).getName();
                }


                displayView(albums);



            }

            @Override
            public void onError() {
                Toast.makeText(_mActivity, "Problem with photo permission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayView(Vector<PhoneAlbum> albums){
        adapter = new PhotoPagerAdapter(getChildFragmentManager());
        adapter.setDatas(albums);
        viewPager.setAdapter(adapter);

        tabView.setViewPager(viewPager);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotoPermissionObject photoPermissionObject) {
        washPhotos();
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotosets(Photosets photosets) {

        new AsyncTask<Photosets,Void,  Vector<PhoneAlbum> >(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Vector<PhoneAlbum> doInBackground(Photosets... params) {
                return flickrDataConverter(params[0]);
            }

            @Override
            protected void onPostExecute(Vector<PhoneAlbum> phoneAlba) {
                super.onPostExecute(phoneAlba);

                displayView(phoneAlba);
            }
        }.execute(photosets);


    };



    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
