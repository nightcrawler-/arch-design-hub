package com.singularity.archdesignhub.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;

/**
 * Created by Frederick on 5/10/2015.
 */
public class GalleryActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = GalleryActivity.class.getSimpleName();
    private static DisplayImageOptions options;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();

    private int PROPERTYLOADER = 105;
    private PagerAdapter adapter;

    private static final String[] IMAGE_COLUMNS = {
            CassiniContract.ImageEntry.C_URL
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.ca_image)
                //.showImageForEmptyUri(R.drawable.ca_image)
                .showImageOnFail(R.drawable.ca_archdesign_blur)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        getSupportLoaderManager().initLoader(PROPERTYLOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "loader start");

        Uri placesUri = CassiniContract.PropertyEntry.buildPropertyUri(getIntent().getExtras().getLong(CassiniContract.PropertyEntry.C_ID));
        Log.i(TAG, "uri - " +placesUri.toString());

        return new CursorLoader(
                this,
                placesUri,
                IMAGE_COLUMNS,
                null,
                null,
                null
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setData(data);
        Log.i(TAG, "loader finished - " +data.getCount());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setData(null);
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        private Cursor data;

        public void setData(Cursor data) {
            this.data = data;
            this.notifyDataSetChanged();
        }

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            data.moveToPosition(position);
            return GalleryFragment.getInstance(data.getString(data.getColumnIndex(CassiniContract.ImageEntry.C_URL)));
        }

        @Override
        public int getCount() {
            return data != null ? data.getCount() : 0;
        }
    }

    public static class GalleryFragment extends Fragment {

        public GalleryFragment() {
        }

        public static Fragment getInstance(String imageUrl) {
            Fragment frag = new GalleryFragment();
            Bundle args = new Bundle();
            args.putString(CassiniContract.ImageEntry.C_URL, imageUrl);
            frag.setArguments(args);

            Log.i(TAG, "url - " + imageUrl);
            return frag;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View parent = inflater.inflate(R.layout.fragment_gallery,
                    container, false);
            ImageView imageView = (ImageView) parent.findViewById(R.id.imageView);
            imageLoader.displayImage(getArguments().getString(CassiniContract.ImageEntry.C_URL), imageView, options);
            return parent;
        }
    }
}
