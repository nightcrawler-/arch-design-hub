package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.DefaultImageLoader;
import com.singularity.archdesignhub.utils.Utils;

/**
 * Created by Frederick on 5/12/2015.
 */
public class EventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private EventsAdapter adapter;

    public static Fragment newInstance() {
        return new EventsFragment();
    }

    public EventsFragment() {
    }

    private static final String[] EVENTS_COLUMNS = {
            CassiniContract.ImageEntry.C_URL,
            CassiniContract.EventEntry.TABLE_NAME + "." + CassiniContract.EventEntry.C_ID,
            CassiniContract.EventEntry.C_TITLE,
            CassiniContract.EventEntry.C_DESCRIPTION,
            CassiniContract.EventEntry.C_TIME,
            CassiniContract.EventEntry.C_LIKES
    };

    private int EVENTS_LOADER = 101;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home_list, container, false);
        ListView list = (ListView) parent.findViewById(android.R.id.list);
        adapter = new EventsAdapter(getActivity());
        list.setAdapter(adapter);


        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(EVENTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = CassiniContract.EventEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                uri,
                EVENTS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public static class EventsAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Cursor data;
        Context context;

        public EventsAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public void setData(Cursor data) {
            this.data = data;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data != null ? data.getCount() : 0;
        }

        @Override
        public Cursor getItem(int position) {
            data.moveToPosition(position);
            return data;
        }

        @Override
        public long getItemId(int position) {
            data.moveToPosition(position);
            return data.getLong(data.getColumnIndex(CassiniContract.EventEntry.C_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_event, null);
                convertView.setTag(holder);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView17);
                holder.title = (TextView) convertView.findViewById(R.id.textView22);
                holder.description = (TextView) convertView.findViewById(R.id.textView23);
                holder.time = (TextView) convertView.findViewById(R.id.textView26);
                holder.likes = (TextView) convertView.findViewById(R.id.textView27);
                Utils.applyFonts(convertView, App.getRobotoSlabLight());

                //holder.bottomPadding = convertView.findViewById(R.id.bottomPad);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            data.moveToPosition(position);

            holder.title.setText(data.getString(data.getColumnIndex(CassiniContract.EventEntry.C_TITLE)));
            holder.description.setText(data.getString(data.getColumnIndex(CassiniContract.EventEntry.C_DESCRIPTION)));
            holder.likes.setText(String.valueOf(data.getInt(data.getColumnIndex(CassiniContract.EventEntry.C_LIKES))));
            holder.time.setText(DateUtils.getRelativeTimeSpanString(context, data.getLong(data.getColumnIndex(CassiniContract.EventEntry.C_TIME)), true));

            DefaultImageLoader.getInstance().loadImage(data.getString(data.getColumnIndex(CassiniContract.ImageEntry.C_URL)), holder.imageView);

            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView title, description, time, likes;
        }
    }
}
