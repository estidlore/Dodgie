package com.blieve.dodgie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private final Bitmap[] bmps;
    private final Context ctx;
    private final ViewGroup.LayoutParams imgParams;

    private final int imgPadding;

    public ImageAdapter(Context ctx, Bitmap[] bmps, int imgSize, int imgPadding) {
        this.ctx = ctx;
        this.bmps = bmps;
        this.imgParams = new ViewGroup.LayoutParams(imgSize, imgSize);
        this.imgPadding = imgPadding;
    }

    @Override
    public int getCount() {
        return bmps.length;
    }

    @Override
    public Object getItem(int position) {
        return bmps[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img = new ImageView(ctx);
        img.setImageBitmap(bmps[position]);
        img.setLayoutParams(imgParams);
        Droid.UI.setPadding(img, imgPadding);
        return img;
    }
}
