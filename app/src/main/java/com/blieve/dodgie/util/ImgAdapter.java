package com.blieve.dodgie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.blieve.dodgie.R;

public class ImgAdapter extends BaseAdapter {

    private final Bitmap[] bmps;
    private final Context ctx;
    private final ViewGroup.LayoutParams imgParams;

    private final int imgPadding;

    private int selection;

    public ImgAdapter(Context ctx, Bitmap[] bmps, int imgSize, int imgPadding) {
        this.ctx = ctx;
        this.bmps = bmps;
        this.imgParams = new ViewGroup.LayoutParams(imgSize, imgSize);
        this.imgPadding = imgPadding;

        this.selection = -1;
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
        ImageView img;
        if (convertView == null) {
            img = new ImageView(ctx);
            img.setLayoutParams(imgParams);
            Droid.UI.setPadding(img, imgPadding);
        } else {
            img = (ImageView) convertView;
        }
        img.setImageBitmap(bmps[position]);
        img.setBackground(ResourcesCompat.getDrawable(ctx.getResources(),
                position == selection ? R.drawable.ui_gridview_selector : R.drawable.ui_empty,
                null));
        return img;
    }

    public final void setSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    public final int getSelection() {
        return selection;
    }



}
