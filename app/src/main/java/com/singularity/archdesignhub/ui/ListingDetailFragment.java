package com.singularity.archdesignhub.ui;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.Utils;


/**
 * Created by Frederick on 4/21/2015.
 */
public class ListingDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    private FadingActionBarHelper mFadingHelper;
    private Bundle mArguments;
    private TextView propertyName, price, perMonth, agentName, agentNumber, details, beds, bathroms;
    private GoogleMap map;
    private static final String[] PROPERTY_COLUMNS = {
            CassiniContract.PropertyEntry.C_NAME,
            CassiniContract.PropertyEntry.C_LOCATION,
            CassiniContract.PropertyEntry.C_TEL,
            CassiniContract.PropertyEntry.C_AGENT_ID,
            CassiniContract.PropertyEntry.C_BATHROOMS,
            CassiniContract.PropertyEntry.C_BEDROOMS,
            CassiniContract.PropertyEntry.C_DESCRIPTION,
            CassiniContract.PropertyEntry.C_INTENT,
            CassiniContract.PropertyEntry.C_VALUE


    };

    private int propertyId = 0;

    public ListingDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = mFadingHelper.createView(inflater);
        Utils.applyFonts(view, App.getRobotoSlabLight());

        propertyName = (TextView) view.findViewById(R.id.textView5);
        price = (TextView) view.findViewById(R.id.textView12);
        perMonth = (TextView) view.findViewById(R.id.textView13);
        agentName = (TextView) view.findViewById(R.id.textView7);
        agentNumber = (TextView) view.findViewById(R.id.textView6);
        details = (TextView) view.findViewById(R.id.textView9);
        beds = (TextView) view.findViewById(R.id.textView15);
        bathroms = (TextView) view.findViewById(R.id.textView8);

        mArguments = getArguments();

        if (mArguments != null)
            propertyId = mArguments.getInt(CassiniContract.PropertyEntry.C_ID);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFadingHelper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_solid_cassini)
                .headerLayout(R.layout.header_listing_detail)
                .headerOverlayLayout(R.layout.header_overlay_listing_detail)
                .contentLayout(R.layout.fragment_listing_detail);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(propertyId, null, this);
        initActionBar(getActivity());
    }


    public void initActionBar(Activity activity) {
        mFadingHelper.initActionBar((ActionBarActivity) activity);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri placesUri = CassiniContract.PropertyEntry.buildPropertyUri(id);
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

    private void populateHolders(Cursor data) {
        propertyName.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_NAME)));
        agentName.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_AGENT_ID)));
        agentNumber.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_TEL)));
        bathroms.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_BATHROOMS)));
        beds.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_BEDROOMS)));
        details.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_DESCRIPTION)));
        price.setText(data.getString(data.getColumnIndex(CassiniContract.PropertyEntry.C_VALUE)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.257662, 36.799800))
                .title("Home"));
        LatLng latlng = new LatLng(-1.257662, 36.799800);

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng) );
    }
}

