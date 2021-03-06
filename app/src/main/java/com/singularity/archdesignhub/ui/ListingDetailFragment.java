package com.singularity.archdesignhub.ui;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.Utils;

import java.text.NumberFormat;


/**
 * Created by Frederick on 4/21/2015.
 */
public class ListingDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    private static final String TAG = ListingDetailFragment.class.getSimpleName();
    private FadingActionBarHelper mFadingHelper;
    private Bundle mArguments;
    private TextView propertyName, price, perMonth, agentName, agentNumber, details, beds, bathrooms, time, intent;
    private ImageView image;
    private FloatingActionButton fab;

    private GoogleMap map;
    private LatLng latlng;
    private String title;


    private static DisplayImageOptions options;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();

    private static final String[] PROPERTY_COLUMNS = {
            CassiniContract.ImageEntry.C_URL,
            CassiniContract.AgentEntry.TABLE_NAME + "." + CassiniContract.AgentEntry.C_NAME,
            CassiniContract.AgentEntry.TABLE_NAME + "." + CassiniContract.AgentEntry.C_TEL,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_NAME,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_LOCATION,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_TIME,
            CassiniContract.PropertyEntry.C_INTENT,
            CassiniContract.PropertyEntry.C_LATT,
            CassiniContract.PropertyEntry.C_LONG,
            CassiniContract.PropertyEntry.C_AGENT_ID,
            CassiniContract.PropertyEntry.C_BATHROOMS,
            CassiniContract.PropertyEntry.C_BEDROOMS,
            CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_DESCRIPTION,
            CassiniContract.PropertyEntry.C_INTENT,
            CassiniContract.PropertyEntry.C_VALUE
    };

    private int PROPERTY_LOADER = 3;
    private long propertyId;

    public ListingDetailFragment() {
    }

    public static Fragment getInstance(long listingId) {
        Fragment frag = new ListingDetailFragment();
        Bundle args = new Bundle();
        args.putLong(CassiniContract.PropertyEntry.C_ID, listingId);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.ca_image)
                //.showImageForEmptyUri(R.drawable.w_empty)
                // .showImageOnFail(R.drawable.ca_archdesign_blur)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = mFadingHelper.createView(inflater);
        Utils.applyFonts(view, App.getRobotoSlabLight());

        image = (ImageView) view.findViewById(R.id.imageViewDetail);

        propertyName = (TextView) view.findViewById(R.id.textView5);
        price = (TextView) view.findViewById(R.id.textView12);
        perMonth = (TextView) view.findViewById(R.id.textView13);
        agentName = (TextView) view.findViewById(R.id.textView7);
        agentNumber = (TextView) view.findViewById(R.id.textView6);
        details = (TextView) view.findViewById(R.id.textView9);
        beds = (TextView) view.findViewById(R.id.textView15);
        bathrooms = (TextView) view.findViewById(R.id.textView8);
        time = (TextView) view.findViewById(R.id.textView3);
        intent = (TextView) view.findViewById(R.id.textView13);
        fab = (FloatingActionButton) view.findViewById(R.id.fab2);

        mArguments = getArguments();

        if (mArguments != null)
            propertyId = mArguments.getLong(CassiniContract.PropertyEntry.C_ID);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView openGallery = (ImageView) view.findViewById(R.id.imageViewGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                intent.putExtra(CassiniContract.PropertyEntry.C_ID, propertyId);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFadingHelper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_solid_archdesignhub)
                .headerLayout(R.layout.header_listing_detail)
                .headerOverlayLayout(R.layout.header_overlay_listing_detail)
                .contentLayout(R.layout.fragment_listing_detail);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(PROPERTY_LOADER, null, this);
        initActionBar(getActivity());
    }


    public void initActionBar(Activity activity) {
        mFadingHelper.initActionBar((ActionBarActivity) activity);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mArguments != null)
            propertyId = mArguments.getLong(CassiniContract.PropertyEntry.C_ID);
        Log.i(TAG, "id -" + propertyId);
        Uri placesUri = CassiniContract.PropertyEntry.buildPropertyUri(propertyId);
        return new CursorLoader(
                getActivity(),
                placesUri,
                PROPERTY_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0)
            populateHolders(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void populateHolders(final Cursor data) {
        if (data.getCount() > 0) {
            if (data.getPosition() == -1)
                data.moveToPosition(0);

            String[] columns = data.getColumnNames();
            for (int i = 0; i < columns.length; i++) {
                Log.i(TAG, "columns - " + columns[i]);

            }
            propertyName.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_NAME)));
            agentName.setText(data.getString(1));
            agentNumber.setText(data.getString(data.getColumnIndex(CassiniContract.AgentEntry.C_TEL)));
            bathrooms.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_BATHROOMS)));
            beds.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_BEDROOMS)));
            details.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_DESCRIPTION)));
            price.setText("KES. " + NumberFormat.getInstance().format(data.getInt(data.getColumnIndex(CassiniContract.PropertyEntry.C_VALUE))));
            time.setText(DateUtils.getRelativeDateTimeString(getActivity(), data.getLong(data.getColumnIndex(CassiniContract.PropertyEntry.C_TIME)), (1 * 60 * 60 * 1000), (3 * 60 * 60 * 1000), DateUtils.FORMAT_ABBREV_RELATIVE));

            if (data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_INTENT)).equals("Sale"))
                intent.setVisibility(View.GONE);
            else
                intent.setVisibility(View.VISIBLE);

            imageLoader.displayImage(data.getString(data.getColumnIndex(CassiniContract.ImageEntry.C_URL)), image, options);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + data.getString(data.getColumnIndex(CassiniContract.AgentEntry.C_TEL))));
                    startActivity(intent);
                }
            });

            //map things
            title = propertyName.getText().toString().trim();
            latlng = new LatLng(data.getDouble(data.getColumnIndex(CassiniContract.PropertyEntry.C_LATT)), data.getDouble(data.getColumnIndex(CassiniContract.PropertyEntry.C_LONG)));
            if (map != null)
                addMarkerOnMap();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (latlng != null)
            addMarkerOnMap();
    }

    private void addMarkerOnMap() {
        map.addMarker(new MarkerOptions()
                .position(latlng)
                .title(title));
        map.animateCamera(CameraUpdateFactory.newLatLng(latlng));
    }
}

