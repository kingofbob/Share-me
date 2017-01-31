package com.soongwei.shareme.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
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

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.soongwei.shareme.MyApplication;
import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.ImageUtils;
import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.adapters.PhotoPagerAdapter;
import com.soongwei.shareme.apis.FlickrClient;
import com.soongwei.shareme.base.BaseMainFragment;
import com.soongwei.shareme.constants.Constants;
import com.soongwei.shareme.flickrobj.PhotoSetListObject;
import com.soongwei.shareme.flickrobj.Photoset;
import com.soongwei.shareme.interfaces.OnPhoneImagesObtained;
import com.soongwei.shareme.objects.PhoneAlbum;
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


    @BindView(R.id.viewPager)ViewPager viewPager;
    @BindView(R.id.tabs)AdvancedPagerSlidingTabStrip tabView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private PhotoPagerAdapter adapter;
    private FlickrClient client;

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
        client = MyApplication.getRestClient();

        initViewFromPhone();
        initViewFromFlickr();
    }

    private void initViewFromFlickr(){




        client.getAlbumNameList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject json) {
                Log.d("DEBUG", "result success: " + json.toString());

                PhotoSetListObject photoSetListObject =  (PhotoSetListObject) StringUtils.convertStringToObject(json.toString(), PhotoSetListObject.class);

                Log.d(MainFragment.class.getSimpleName(), "Data Name: " + photoSetListObject.getPhotosets().getPhotoset().get(0).getTitle().getContent());


                for (Photoset photoset: photoSetListObject.getPhotosets().getPhotoset()){
                    Log.d(MainFragment.class.getSimpleName(), "Photoset Name: " + photoset.getTitle().getContent());


                    client.getPhotosList(new JsonHttpResponseHandler(){
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject json) {
                            Log.d("DEBUG", "result success2: " + json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", "result failure2: " + throwable.getMessage());
                            Toast.makeText(getActivity(),R.string.check_connectivity, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "result failure: " + throwable.getMessage());
                Toast.makeText(getActivity(),R.string.check_connectivity, Toast.LENGTH_LONG).show();
            }
        });
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


    private void washPhotos(){
        ImageUtils.getPhoneAlbums(getActivity(), new OnPhoneImagesObtained() {
            @Override
            public void onComplete(Vector<PhoneAlbum> albums) {
                String[] stockArr = new String[albums.size()];
                for(int t=0; t<albums.size(); t++){
                    stockArr[t] = albums.get(t).getName();
                }



                if (viewPager.getAdapter() == null){
                    adapter = new PhotoPagerAdapter(getChildFragmentManager());
                    adapter.setDatas(albums);
                    viewPager.setAdapter(adapter);

                    tabView.setViewPager(viewPager);


                }else{
                    adapter.setDatas(albums);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onError() {
                Toast.makeText(_mActivity, "Problem with photo permission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotoPermissionObject photoPermissionObject) {
        washPhotos();
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
