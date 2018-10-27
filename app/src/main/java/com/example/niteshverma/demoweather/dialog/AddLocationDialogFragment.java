package com.example.niteshverma.demoweather.dialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niteshverma.demoweather.DemoApplication;
import com.example.niteshverma.demoweather.R;
import com.example.niteshverma.demoweather.Utility.Utilities;
import com.example.niteshverma.demoweather.database.DBHelper;
import com.example.niteshverma.demoweather.fragment.BookmarkListFragment;
import com.example.niteshverma.demoweather.model.Bookmark;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddLocationDialogFragment extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private SupportMapFragment mapFragment = null;
    private SupportPlaceAutocompleteFragment autocompleteFragment = null;
    private GoogleMap googleMap;
    private Marker currentMarker;

    Handler handler;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST = 100;

    public static AddLocationDialogFragment getInstance() {
        AddLocationDialogFragment fragment = new AddLocationDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_location, container, false);
        handler = new Handler();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLocationProvider();
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapDialog);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteFragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addMarker(place.getLatLng(), place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                // Toast.makeText(getContext(), status.getStatusMessage() + " : " + status.getStatusCode(), Toast.LENGTH_SHORT).show();
                Utilities.printLog("AddLocationDialogFragment onError", status.getStatusMessage() + " : " + status.getStatusCode());
            }
        });

    }

    private void initLocationProvider() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionRequired = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissionRequired, PERMISSIONS_REQUEST);

            return;
        } else {
            addLastLocationListener();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST){
            for(int granted : grantResults){
                if(granted == PackageManager.PERMISSION_GRANTED){
                    addLastLocationListener();
                    break;
                }
            }
        }
    }

    private void addLastLocationListener(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                getLocationNameAndAddMarker(latLng);
                            }
                        }
                    });
        }
    }

    private void addLocation(LatLng latLng, String title){

        String lat = String.valueOf(latLng.latitude);
        String lon = String.valueOf(latLng.longitude);

        if(!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon) && !TextUtils.isEmpty(title)){
            Bookmark bookmark = new Bookmark();
            bookmark.setLat(lat);
            bookmark.setLon(lon);
            bookmark.setLocationName(title);

            long row = DBHelper.getInstance().addBookmark(bookmark, DemoApplication.get());
            if(row > 0){
                bookmark.setId(row);
                ((BookmarkListFragment) getParentFragment()).refreshWithLocation(bookmark);
            }
        }
    }
    private void dismissDialog(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                dismissAllowingStateLoss();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utilities.printLog("AddLocationDialogFragment", "On Map Ready");
        this.googleMap = googleMap;
        googleMap.setOnInfoWindowClickListener(this);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getLocationNameAndAddMarker(latLng);

            }
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(final Marker marker) {

                View markerView = getLayoutInflater().inflate(R.layout.marker, null);
                TextView tvLocationName = markerView.findViewById(R.id.tvLocationName);
                tvLocationName.setText(marker.getTitle());

                return markerView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(currentMarker != null && currentMarker.isVisible() && currentMarker.isInfoWindowShown()){
                    currentMarker.hideInfoWindow();
                }
            }
        });
    }

    private void getLocationNameAndAddMarker(LatLng latLng){
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list;
        try {
            list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            return;
        }

        if(list != null && list.size() > 0){
            Address address = list.get(0);
            addMarker(latLng, address.getLocality());

        }
    }

    private void addMarker(LatLng latLng, String title){

        if(googleMap != null && latLng != null){
            if(currentMarker != null){
                currentMarker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            currentMarker = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            currentMarker.showInfoWindow();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mapFragment){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(mapFragment)
                    .commit();
        }
        if(null != autocompleteFragment){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(autocompleteFragment)
                    .commit();
        }
        mapFragment = null;
        autocompleteFragment = null;
        googleMap = null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        dismissDialog();
        addLocation(marker.getPosition(), marker.getTitle());
    }
}
