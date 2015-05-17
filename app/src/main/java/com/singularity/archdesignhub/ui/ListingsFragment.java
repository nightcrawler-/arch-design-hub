package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.DefaultImageLoader;
import com.singularity.archdesignhub.utils.Utils;

import java.text.NumberFormat;


/**
 * Created by Frederick on 4/23/2015.
 */
public class ListingsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = ListingsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView list;
    private ListingAdapter adapter;
    private static DisplayImageOptions options;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();

    private static final String[] LISTING_COLUMNS = {
            CassiniContract.ImageEntry.C_URL,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_ID,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_NAME,
            CassiniContract.PropertyEntry.C_LOCATION,
            CassiniContract.PropertyEntry.C_VALUE,
            CassiniContract.PropertyEntry.C_INTENT,
            CassiniContract.PropertyEntry.C_TEL,
            CassiniContract.PropertyEntry.C_BATHROOMS,
            CassiniContract.PropertyEntry.C_BEDROOMS


    };
    private int LISTINGS_LOADER = 0;

    public ListingsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListingsFragment newInstance(int sectionNumber) {
        ListingsFragment fragment = new ListingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home_list,
                container, false);
        list = (ListView) parent.findViewById(android.R.id.list);
        adapter = new ListingAdapter(getActivity());
        FloatingActionButton fab = (FloatingActionButton) parent.findViewById(R.id.fab);
        fab.attachToListView(list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "id -" + id);

                Intent intent = new Intent(getActivity(), ListingDetailActivity.class);
                intent.putExtra(CassiniContract.PropertyEntry.C_ID, id);
                startActivity(intent);
            }
        });

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
        getLoaderManager().initLoader(LISTINGS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri placesUri = CassiniContract.PropertyEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                placesUri,
                LISTING_COLUMNS,
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
            return cursor.getLong(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_listing, null);
                convertView.setTag(holder);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.title = (TextView) convertView.findViewById(R.id.textView10);
                holder.location = (TextView) convertView.findViewById(R.id.textView17);
                holder.tel = (TextView) convertView.findViewById(R.id.textView18);
                holder.value = (TextView) convertView.findViewById(R.id.textView11);
                holder.intentLabel = (TextView) convertView.findViewById(R.id.textView16);
                holder.beds = (TextView) convertView.findViewById(R.id.textView15);
                holder.showers = (TextView) convertView.findViewById(R.id.textView8);

                Utils.applyFonts(convertView, App.getRobotoSlabLight());

                //holder.bottomPadding = convertView.findViewById(R.id.bottomPad);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            cursor.moveToPosition(position);
            //hack for the padding at the bottom of the list
//            if (position + 1 == getCount())
//                holder.bottomPadding.setVisibility(View.VISIBLE);
//            else
//                holder.bottomPadding.setVisibility(View.GONE);


            holder.title.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_NAME)));
            holder.location.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_LOCATION)));
            holder.tel.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_TEL)));
            holder.value.setText("KES. " + NumberFormat.getInstance().format(cursor.getInt(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_VALUE))));
            holder.beds.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_BEDROOMS)));
            holder.showers.setText(cursor.getString(cursor.getColumnIndex(CassiniContract.PropertyEntry.C_BATHROOMS)));

            DefaultImageLoader.getInstance().loadImage(cursor.getString(cursor.getColumnIndex(CassiniContract.ImageEntry.C_URL)), holder.imageView);


            return convertView;
        }

        @Override
        public int getCount() {
            return cursor != null ? cursor.getCount() : 0;
        }


        private class ViewHolder {
            ImageView imageView;
            TextView title, location, tel, value, intentLabel, showers, beds;
            View bottomPadding;
        }

    }
}
