package com.soongwei.shareme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soongwei.shareme.R;
import com.soongwei.shareme.Utils.RoundedTransformation;
import com.soongwei.shareme.objects.GalleryImageObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 00020443 on 20/1/2016.
 */
public class GalleryImageAdapter extends BaseAdapter {

    private Context context;
    private List<GalleryImageObject> datas;
    private LayoutInflater inflater;

    public GalleryImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setData(List<GalleryImageObject> datas) {
        this.datas = datas;
    }

    public List<GalleryImageObject> getDatas() {
        return datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_gallery_image, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        GalleryImageObject galleryImageObject = datas.get(position);
        Log.d(this.getClass().getName(), "Processing image: " + galleryImageObject.getUri());

        if (galleryImageObject.isSelected()){
            viewHolder.frameLayout.setBackgroundResource(R.color.imageSelectedColor);
        }else{
            viewHolder.frameLayout.setBackgroundResource(R.color.colorPrimeBackground);
        }

        File imageFile = new File(galleryImageObject.getUri());
        Picasso.with(context)
                .load(imageFile)
                .placeholder(R.drawable.placeholder)
//                .error()
                .noFade()
                .resize(250, 250)
                .transform(new RoundedTransformation(5, 0))
                .centerCrop()
                .into(viewHolder.imageView);


        return convertView;
    }


    class ViewHolder {

        @BindView(R.id.frame)
        FrameLayout frameLayout;
        @BindView(R.id.image)
        ImageView imageView;


        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }


}
