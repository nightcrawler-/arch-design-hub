package com.singularity.archdesignhub.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.utils.Utils;


/**
 * Created by Frederick on 4/23/2015.
 */
public class ListingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView list;
    private ListingAdapter adapter;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    static DisplayImageOptions options;

    public ListingFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListingFragment newInstance(int sectionNumber) {
        ListingFragment fragment = new ListingFragment();
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_listing, null);
                convertView.setTag(holder);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.title = (TextView) convertView.findViewById(R.id.textView5);
                holder.location = (TextView) convertView.findViewById(R.id.textView7);
                holder.tel = (TextView) convertView.findViewById(R.id.textView6);
                holder.cost = (TextView) convertView.findViewById(R.id.textView12);
                holder.perMonth = (TextView) convertView.findViewById(R.id.textView13);

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


//            holder.title.setText(cursor.getString(cursor.getColumnIndex(PlaceContract.PlacesEntry.COLUMN_NAME)));
//            String firstPhotoRef = cursor.getString(cursor.getColumnIndex(PlaceContract.PlacesEntry.COLUMN_PHOTO_REFERENCE));


//            if (firstPhotoRef == null || firstPhotoRef.length() == 0)
//                holder.imageView.setVisibility(View.GONE);
//            else {
//                imageLoader.displayImage(Backbone.BASE_PHOTO_URL + firstPhotoRef, holder.imageView, options);
//                holder.imageView.setVisibility(View.VISIBLE);
//            }
//
            imageLoader.displayImage("drawable://" + R.drawable.lizzy, holder.imageView, options);


            return convertView;
        }

        @Override
        public int getCount() {
            return cursor != null ? cursor.getCount() : 0;
        }


        private class ViewHolder {
            ImageView imageView;
            TextView title, location, tel, cost, perMonth;
            View bottomPadding;
        }

    }
}
