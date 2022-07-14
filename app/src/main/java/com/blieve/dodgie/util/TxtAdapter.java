package com.blieve.dodgie.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blieve.dodgie.R;

public class TxtAdapter extends BaseAdapter {

    private final String[] txts;
    private final Context ctx;
    private final LayoutInflater inflater;
    private final ViewGroup.LayoutParams params;
    private final int resource;

    private int selection;

    public TxtAdapter(Context ctx, int resource, String[] txts) {
        this.ctx = ctx;
        this.resource = resource;
        this.txts = txts;

        this.inflater = LayoutInflater.from(ctx);
        this.params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.mode_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(txts[position]);
        holder.text.setBackgroundColor(position == selection ? 0x88888888 : 0x00000000);

        return convertView;
    }

    public final void setSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    public final int getSelection() {
        return selection;
    }

    private static class ViewHolder {
        TextView text;
    }

}
