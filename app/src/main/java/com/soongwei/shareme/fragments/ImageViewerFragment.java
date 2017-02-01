package com.soongwei.shareme.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.webkit.URLUtil;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.ImageUtils;
import com.soongwei.shareme.Utils.RoundedTransformation;
import com.soongwei.shareme.Utils.StringUtils;
import com.soongwei.shareme.adapters.GalleryImageAdapter;
import com.soongwei.shareme.apis.OAuthTask;
import com.soongwei.shareme.base.BaseBackFragment;
import com.soongwei.shareme.base.BaseFragment;
import com.soongwei.shareme.objects.GalleryImageObject;
import com.soongwei.shareme.objects.PhoneAlbum;
import com.soongwei.shareme.objects.PhonePhoto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;


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
    private String urlO;
    private PhotoViewAttacher mAttacher;
    private ProgressDialog mProgressDialog;


    @BindView(R.id.bigimage)ImageView imageView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    public static ImageViewerFragment newInstance(long id, String url, String urlO) {

        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, url);
        args.putString(ARG_PARAM3, urlO);
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
            urlO = getArguments().getString(ARG_PARAM3);

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


                        if (URLUtil.isHttpsUrl(urlO) || URLUtil.isHttpUrl(urlO)) {
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, urlO);
                        }else{
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(url)));
                        }

                        startActivity(Intent.createChooser(share, "Share Image"));
                        break;

                    case R.id.action_download:
                        if (URLUtil.isHttpsUrl(urlO) || URLUtil.isHttpUrl(urlO)) {
                            ImageUtils.file_download(getActivity(),urlO );
                        }else{
                            Toast.makeText(getActivity(), R.string.file_notworking, Toast.LENGTH_LONG).show();
                        }

                        break;
                }
                return true;
            }
        });

        initView();
    }

    private void initView() {
        mProgressDialog = ProgressDialog.show(getActivity(),
                "", getResources().getString(R.string.loading_image)); //$NON-NLS-1$ //$NON-NLS-2$
        mProgressDialog.setCancelable(false);

        mAttacher = new PhotoViewAttacher(imageView);


        if (URLUtil.isHttpsUrl(urlO) || URLUtil.isHttpUrl(urlO)) {
            //is url
            Picasso.with(getActivity()).load(urlO).fit().centerInside().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher.update();
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onError() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                }
            });

        }else{
            File imageFile = new File(url);

            Picasso.with(getActivity()).load(imageFile).fit().centerInside().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher.update();
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onError() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                }
            });

        }






    }



    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
