package com.example.meiriq0717.testapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;

/**
 * Created by meiriq0717 on 2015/10/14.
 */
public class MyAdapter extends BaseAdapter {
    private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
    private Context mContext;
    private ImageLoader mImageLoader;
    private DefaultImageLoadHandler handler;
    public MyAdapter(Context context) {
        mContext = context;
//        handler=new DefaultImageLoadHandler(mContext);
//        handler.setLoadingImageColor(Color.parseColor("#ffffff"));
//        handler.setLoadingResources(R.mipmap.ic_launcher);
      //  mImageLoader= ImageLoaderFactory.create(mContext);
        mImageLoader=ImageLoaderFactory.createStableImageLoader(mContext);
    }

    public void setmList(List<Map<String, String>> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.intem_layout, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cube_iv = (CubeImageView) convertView.findViewById(R.id.cube_iv);
        viewHolder.imageTitle_tv = (TextView) convertView.findViewById(R.id.imageTitle_tv);

        viewHolder.imageTitle_tv.setText(mList.get(position).get("imageTitle"));
        viewHolder.cube_iv.loadImage(mImageLoader, mList.get(position).get("imageUrl"));

        return convertView;
    }

    public class ViewHolder {
        private CubeImageView cube_iv;
        private TextView imageTitle_tv;
    }
}
