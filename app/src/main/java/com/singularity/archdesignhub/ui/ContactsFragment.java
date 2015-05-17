package com.singularity.archdesignhub.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.DefaultImageLoader;
import com.singularity.archdesignhub.utils.Utils;

/**
 * Created by Frederick on 5/16/2015.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView name, phone, email, website, location;
    private FloatingActionButton call, emailPicture;
    private ImageView image;
    private ObservableScrollView scrollView;

    private static int CONTACTS_LOADER = 123;
    private static String[] CONTACTS_COLUMNS = {
            CassiniContract.ImageEntry.C_URL,
            CassiniContract.ContactEntry.C_TITLE,
            CassiniContract.ContactEntry.C_PHONE,
            CassiniContract.ContactEntry.C_EMAIL,
            CassiniContract.ContactEntry.C_WEBSITE,
            CassiniContract.ContactEntry.C_ADDRESS
    };

    public ContactsFragment() {
    }

    public static Fragment newInstance() {
        return new ContactsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_contacts, container, false);
        image = (ImageView) parent.findViewById(R.id.imageView20);
        name = (TextView) parent.findViewById(R.id.textView29);
        phone = (TextView) parent.findViewById(R.id.textView31);
        email = (TextView) parent.findViewById(R.id.textView32);
        website = (TextView) parent.findViewById(R.id.textView33);
        location = (TextView) parent.findViewById(R.id.textView28);
        call = (FloatingActionButton) parent.findViewById(R.id.fab);
        emailPicture = (FloatingActionButton)parent.findViewById(R.id.fab2);
        scrollView = (ObservableScrollView) parent.findViewById(R.id.scrollView);

        emailPicture.attachToScrollView(scrollView);

        Utils.applyFonts(parent, App.getLatoRegular());


        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), CassiniContract.ContactEntry.CONTENT_URI, CONTACTS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        populateViews(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void populateViews(final Cursor data) {
        if (data.getCount() > 0) {
            data.moveToPosition(0);
            name.setText(data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_TITLE)));
            phone.setText(data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_PHONE)));
            email.setText(data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_EMAIL)));
            website.setText(data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_WEBSITE)));
            location.setText(data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_ADDRESS)));

            DefaultImageLoader.getInstance().loadImage(data.getString(data.getColumnIndex(CassiniContract.ImageEntry.C_URL)), image);


            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_PHONE))));
                    startActivity(intent);
                }
            });

            emailPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO /*Uri.parse("mailto:" + Uri.encode(address))*/);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, data.getString(data.getColumnIndex(CassiniContract.ContactEntry.C_EMAIL)));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "App");
                    startActivity(Intent.createChooser(intent, "Send Email"));
                   // startActivity(intent);
                }
            });

        }
    }
}
