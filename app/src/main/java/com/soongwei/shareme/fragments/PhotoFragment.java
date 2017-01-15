package com.soongwei.shareme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.activities.MainActivity;
import com.soongwei.shareme.adapters.GalleryImageAdapter;
import com.soongwei.shareme.base.BaseFragment;
import com.soongwei.shareme.objects.GalleryImageObject;
import com.soongwei.shareme.objects.PhoneAlbum;
import com.soongwei.shareme.objects.PhonePhoto;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by YoKeyword on 16/6/30.
 */
public class PhotoFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private PhoneAlbum phoneAlbum;

    private long id;
    private String title;
    private GalleryImageAdapter adapter;
    private List<GalleryImageObject> imageObjects = new ArrayList<>();

    @BindView(R.id.gridView)GridView gridView;

    public static PhotoFragment newInstance(long id, String title) {

        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, title);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getLong(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);

            phoneAlbum = (PhoneAlbum)StringUtils.convertStringToObject(title, PhoneAlbum.class);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {
        Log.i(PhotoFragment.class.getSimpleName(), "Loading init View ...");

        imageObjects.clear();
        for(PhonePhoto photo:phoneAlbum.getAlbumPhotos()){
            imageObjects.add(new GalleryImageObject(photo.getId(), photo.getAlbumName(), photo.getPhotoUri(), false));
        }

        if (gridView.getAdapter() == null){
            adapter = new GalleryImageAdapter(getActivity());
            adapter.setData(imageObjects);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((MainActivity)getActivity()).start(ImageViewerFragment.newInstance(0,imageObjects.get(position).getUri()));

                }
            });
        }else{
            adapter.setData(imageObjects);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

    }


    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
