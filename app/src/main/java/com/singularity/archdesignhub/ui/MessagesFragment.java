package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.TextView;

import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.Utils;

/**
 * Created by Frederick on 5/24/2015.
 */
public class MessagesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] MESSAGES_COLUMNS = {
            CassiniContract.MessageEntry.C_MESSAGE,
            CassiniContract.MessageEntry.C_TIME,
            CassiniContract.MessageEntry.C_EXTRA
    };

    private static int MSG_LOADER;
    private ListView listView;
    private MessagesAdapter adapter;


    public MessagesFragment() {
    }

    public static Fragment newInstance() {
        return new MessagesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home_list, container, false);
        listView = (ListView) parent.findViewById(android.R.id.list);
        adapter = new MessagesAdapter(getActivity());
        listView.setAdapter(adapter);

        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MSG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), CassiniContract.MessageEntry.CONTENT_URI, MESSAGES_COLUMNS, null, null, "time DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static class MessagesAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private Cursor data;
        private Context context;

        public MessagesAdapter(Context context) {
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
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_msg, null);
                convertView.setTag(holder);
                holder.extra = (TextView) convertView.findViewById(R.id.textView22);
                holder.content = (TextView) convertView.findViewById(R.id.textView23);
                holder.time = (TextView) convertView.findViewById(R.id.textView26);
                Utils.applyFonts(convertView, App.getRobotoSlabLight());

                //holder.bottomPadding = convertView.findViewById(R.id.bottomPad);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            data.moveToPosition(position);

            holder.extra.setText(data.getString(data.getColumnIndex(CassiniContract.MessageEntry.C_EXTRA)));
            holder.content.setText(data.getString(data.getColumnIndex(CassiniContract.MessageEntry.C_MESSAGE)));
            holder.time.setText(DateUtils.getRelativeTimeSpanString(context, data.getLong(data.getColumnIndex(CassiniContract.MessageEntry.C_TIME)), true));

            return convertView;
        }

        private class ViewHolder {
            TextView extra, content, time;
        }
    }
}
