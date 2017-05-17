package com.example.android.assignment4;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SINDHU on 20-03-2017.
 */

public class MapDummy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdummy);
        String value = getIntent().getExtras().getString("userarray");
        Bundle userarraybundel = new Bundle();
        MapViewFilter mapView = new MapViewFilter();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragtran = fm.beginTransaction();
        userarraybundel.putString("userarray", value);
        mapView.setArguments(userarraybundel);
        fragtran.replace(R.id.mapHolder, mapView);
        fragtran.addToBackStack(null);
        fragtran.commit();
    }
}

