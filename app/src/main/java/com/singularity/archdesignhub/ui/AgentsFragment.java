package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.Utils;


/**
 * Created by Frederick on 4/23/2015.
 */
public class AgentsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = AgentsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private GridView list;
    private ListingAdapter adapter;
    private static DisplayImageOptions options;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();

    private static final String[] AGENTS_COLUMNS = {
            CassiniContract.AgentEntry.TABLE_NAME + "." + CassiniContract.AgentEntry.C_NAME,
            CassiniContract.AgentEntry.TABLE_NAME + "." + CassiniContract.AgentEntry.C_ID,
            CassiniContract.AgentEntry.C_TEL,
            CassiniContract.AgentEntry.C_EMAIL,
            CassiniContract.AgentEntry.C_WEBSITE,
            CassiniContract.ImageEntry.C_URL
    };
    private int AGENTS_LOADER = 0;

    public AgentsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AgentsFragment newInstance(int sectionNumber) {
        AgentsFragment fragment = new AgentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_agent_list,
                container, false);
        list = (GridView) parent.findViewById(android.R.id.list);
        adapter = new ListingAdapter(getActivity());
        list.setAdapter(adapter);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ca_image)
                        //.showImageForEmptyUri(R.drawable.w_empty)
                .showImageOnFail(R.drawable.ca_archdesign_blur)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();


        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(AGENTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = CassiniContract.AgentEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                uri,
                AGENTS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setCursor(data);
        Log.i(TAG, "loader finished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }


    public static class ListingAdapter extends BaseAdapter {
        private Cursor cursor;
        LayoutInflater inflater;

        public ListingAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public void setCursor(Cursor cursor) {
            this.cursor = cursor;
            this.notifyDataSetChanged();
        }

        @Override
        public Cursor getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(CassiniContract.AgentEntry.C_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_agent, null);
                convertView.setTag(holder);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.name = (TextView) convertView.findViewById(R.id.textView);
                holder.tel = (TextView) convertView.findViewById(R.id.textView2);
                holder.email = (TextView) convertView.findViewById(R.id.textView3);
                holder.website = (TextView) convertView.findViewById(R.id.textView14);

                Utils.applyFonts(convertView, App.getRobotoSlabLight());

                //holder.bottomPadding = convertView.findViewById(R.id.bottomPad);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //cursor.moveToPosition(position);
            //hack for the padding at the bottom of the list
//            if (position + 1 == getCount())
//                holder.bottomPadding.setVisibility(View.VISIBLE);
//            else
//                holder.bottomPadding.setVisibility(View.GONE);

            cursor.moveToPosition(position);
            holder.name.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.AgentEntry.C_NAME)));
            holder.tel.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.AgentEntry.C_TEL)));
            holder.website.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.AgentEntry.C_WEBSITE)));
            holder.email.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.AgentEntry.C_EMAIL)));


            imageLoader.displayImage(cursor.getString(cursor.getColumnIndex(CassiniContract.ImageEntry.C_URL)), holder.imageView, options);


            return convertView;
        }

        @Override
        public int getCount() {
            return cursor != null ? cursor.getCount() : 0;
        }


        private class ViewHolder {
            ImageView imageView;
            TextView name, tel, email, website;
            View bottomPadding;
        }

    }
}
