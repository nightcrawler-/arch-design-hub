package com.singularity.archdesignhub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singularity.archdesignhub.R;

/**
 * Created by Frederick on 5/11/2015.
 */
public class BlankFragment extends Fragment {

    public BlankFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_blank, container, false);
        return parent;
    }
}
