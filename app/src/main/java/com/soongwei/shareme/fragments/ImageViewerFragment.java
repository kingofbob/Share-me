package com.soongwei.shareme.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;
import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.adapters.GalleryImageAdapter;
import com.soongwei.shareme.base.BaseBackFragment;
import com.soongwei.shareme.base.BaseFragment;
import com.soongwei.shareme.objects.GalleryImageObject;
import com.soongwei.shareme.objects.PhoneAlbum;
import com.soongwei.shareme.objects.PhonePhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by YoKeyword on 16/6/30.
 */
public class ImageViewerFragment extends BaseBackFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private PhoneAlbum phoneAlbum;

    private long id;
    private String url;



    @BindView(R.id.bigimage)BigImageView imageView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    public static ImageViewerFragment newInstance(long id, String url) {

        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, url);

        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getLong(ARG_PARAM1);
            url = getArguments().getString(ARG_PARAM2);


        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imageviewer, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        EventBus.getDefault().register(this);
        initToolbarNav(toolbar);

        toolbar.inflateMenu(R.menu.share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(url)));
                        startActivity(Intent.createChooser(share, "Share Image"));
                        break;
                }
                return true;
            }
        });

        initView();
    }

    private void initView() {
//        imageView.setProgressIndicator(new ProgressPieIndicator());
        File imageFile = new File(url);
        Log.i(ImageViewerFragment.class.getSimpleName(), "FilePath: " + url);
        imageView.showImage(Uri.fromFile(imageFile));


    }



    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
