package com.maggicco.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.maggicco.go4lunch.BuildConfig;
import com.maggicco.go4lunch.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback{

    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;
    private Location location;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private static final String TAG = MapsViewFragment.class.getSimpleName();
    private List<Place> placeList;
    // The entry point to the Places API.
    private PlacesClient placesClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    Activity activity;

    // A default location (Paris, France) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-48.51529776, 2.20564504);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    Bitmap bitmapRed;
    Bitmap bitmapGreen;
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private List<String> idList;

    // The entry point to the Places API.
    private String placeId;



    public MapsViewFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_view, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Build the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ((LoggedInActivity)getActivity()).setToolbarNavigation();
        ((LoggedInActivity) getActivity()).getSupportActionBar().setTitle("I'm hungry");


//        // Retrieve location and camera position from saved instance state.
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Construct a PlacesClient
        Places.initialize(getActivity(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(getContext());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        idList = new ArrayList<>();
        placeList = new ArrayList<>();


    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.search_menu, menu);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;


        // Sets mGoogleMap's style without poi.
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.json_poi_style));

        map.setOnMarkerClickListener(marker -> {
            if (marker.getTitle().equals(marker.getTag())){
                marker.setTag(null);
                Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
                intent.putExtra("RESTAURANT_NAME", marker.getTitle());
                startActivity(intent);
            }
            else{
                marker.showInfoWindow();
                marker.setTag(marker.getTitle());
            }
            return true;
        });


        // Prompt the user for permission.
        getLocationPermission();

        //Get the current places around device location
        showCurrentPlace();

//        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

//        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }

                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }
        if (locationPermissionGranted) {

            // Use fields to define the data types to return.
            //List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME,Place.Field.TYPES, Place.Field.ID,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest placeRequest = FindCurrentPlaceRequest.builder(placeFields).build();

            // Call findCurrentPlace and handle the response (first check that the user has granted permission).
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(placeRequest);
                placeResponse.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                            if (placeLikelihood.getPlace().getTypes() != null &&
                                    (placeLikelihood.getPlace().getTypes().contains(Place.Type.RESTAURANT)
                                            || placeLikelihood.getPlace().getTypes().contains(Place.Type.BAR) ||
                                            placeLikelihood.getPlace().getTypes().contains(Place.Type.MEAL_TAKEAWAY))) {

                                Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                        placeLikelihood.getPlace().getName(),
                                        placeLikelihood.getLikelihood()));

                                idList.add(placeLikelihood.getPlace().getId());
                                Log.d(TAG, "showCurrentPlace in list : " + placeLikelihood.getPlace().getId());
                                fetchRestaurantDetails(placeLikelihood.getPlace().getId());

                            }
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                });
            } else {
                // A local method to request required permissions;
                // See https://developer.android.com/training/permissions/requesting
                getLocationPermission();
            }
        }
    }

    /**
     * Fetches place's details according to the place's Id passed in argument.
     *
     * @param placeId The place's id that we want details.
     */
    private void fetchRestaurantDetails(String placeId) {
//        Defines witch fields we want in the query's response.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID,Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI,
                Place.Field.OPENING_HOURS, Place.Field.RATING);
//        Creates the request with the defined fields about the given place.
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, fields).build();
//        Requests GoogleMap's server to fetch place's details.
        placesClient.fetchPlace(placeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Place place = (task.getResult()).getPlace();
                placeList.add(place);
            }
            if (placeList.size() == idList.size())
                addMarkerOnMap();
        });
    }

    /**
     * Adds a marker on map according to the place's location and the place's name.
     */
    private void addMarkerOnMap() {
        if(map != null)
            map.clear();
        if (placeList != null) {
            for (Place place : placeList) {
                Log.d(TAG, "Place in list addMarkerOnMap: " + place);

                if (place.getLatLng() != null) {
                    map.addMarker(new MarkerOptions().position(place.getLatLng()).snippet(place.getId())
                            .title(place.getName()).icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
//                else{
//                    map.addMarker(new MarkerOptions().position(place.getLatLng()).snippet(place.getId())
//                            .title(place.getName()).icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//                }
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                addMarkerOnMap();
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}