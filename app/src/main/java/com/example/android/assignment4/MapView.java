package com.example.android.assignment4;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SINDHU on 19-03-2017.
 */

public class MapView extends Fragment  {

    SupportMapFragment mapFragment;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;
    private static final int CONNECTION_RESOLUTION_REQUEST = 2;

    private View rootView;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private com.google.android.gms.maps.MapView mapView;
    private GoogleMap gMap;
    JSONObject userJSON;
    Bundle bundle;
    String nick;
    Double lat,longi;
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    double zoomLevel = 16.0;
    private LocationRequest mLocationRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mapfragment, container, false);
        bundle=getArguments();
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    gMap = map;
                    gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    String arrayLists = bundle.getString("userarray");
                    Log.d("message", "user" + arrayLists);
                    JSONArray jObj = null;
                    try {
                        jObj = new JSONArray(arrayLists);
                        Log.d("message", "jobj " + jObj.length());
                        for (int i = 0; i < jObj.length(); i++) {
                            Log.d("message", "inside for loop");
                            JSONObject userJSON = jObj.getJSONObject(i);
                            //Log.d("message","inside for loop"+userJSON);
                            LatLng latlng = new LatLng(userJSON.getDouble("latitude"), userJSON.getDouble("longitude"));
                            //Log.d("message","latlng"+latlng);
                            //lists.add(latlng);
                            handleNewLocation(latlng,userJSON.getString("nickname"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                });
        }
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void handleNewLocation(LatLng latLng,String name) {
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(name);
        gMap.addMarker(options).setDraggable(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        gMap.clear();

        }
    }

