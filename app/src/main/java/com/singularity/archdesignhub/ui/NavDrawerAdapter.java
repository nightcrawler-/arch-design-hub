package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.pojo.NavItem;
import com.singularity.archdesignhub.utils.DefaultImageLoader;
import com.singularity.archdesignhub.utils.Utils;

/**
 * Created by Frederick on 5/17/2015.
 */
public class NavDrawerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private NavItem[] data;

    public NavDrawerAdapter(Context context, NavItem[] data) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public NavItem getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_nav_drawer, null);
            convertView.setTag(holder);

            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView4);
            holder.name = (TextView) convertView.findViewById(R.id.textView2);
            Utils.applyFonts(holder.name, App.getRobotoSlabLight());


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).title);
        DefaultImageLoader.getInstance().loadImage("drawable://" + getItem(position).icon, holder.imageView);

        return convertView;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView name;
    }



}
