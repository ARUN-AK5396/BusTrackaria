package com.project.bustrackeria;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends Fragment {

    private TextView busStatus,getStop1,getStop2,getStop3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        busStatus = view.findViewById(R.id.bus_start_point);
        getStop1 = view.findViewById(R.id.bus_stop1);
        getStop2 = view.findViewById(R.id.bus_stop2);
        getStop3 = view.findViewById(R.id.bus_stop3);


        return view;
    }
}