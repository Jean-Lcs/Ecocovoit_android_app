package com.ecocovoit.ecocovoit.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecocovoit.ecocovoit.R;

public class OsmMapFragment extends Fragment {

    public OsmMapFragment() {

    }

    public static OsmMapFragment newInstance() {
        OsmMapFragment fragment = new OsmMapFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_osm_map, container, false);
    }
}