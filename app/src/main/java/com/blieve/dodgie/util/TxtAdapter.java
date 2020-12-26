package com.blieve.dodgie.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TxtAdapter extends BaseAdapter {

    private final String[] txts;
    private final Context ctx;
    private final LayoutInflater inflater;
    private final int resource;

    private int selection;

    public TxtAdapter(Context ctx, int resource, String[] txts) {
        this.ctx = ctx;
        this.resource = resource;
        this.txts = txts;

        this.inflater = LayoutInflater.from(ctx);

        this.selection = -1;
    }

    @Override
    public int getCount() {
        return txts.length;
    }

    @Override
    public Object getItem(int position) {
        return txts[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("debug", "getView() start");
        final TextView text;
        if (convertView == null) {
            text = (TextView) inflater.inflate(resource, parent, false);
        } else {
            text = (TextView) convertView;
        }
        text.setText(txts[position]);
        text.setBackgroundColor(position == selection ? 0x88888888 : 0x00000000);
        Log.d("debug", "getView() end");
        return text;
    }

    public final void setSelection(int selection) {
        Log.d("debug", "setSelection() start");
        this.selection = selection;
        notifyDataSetChanged();
        Log.d("debug", "setSelection() end");
    }

    public final int getSelection() {
        return selection;
    }

}
