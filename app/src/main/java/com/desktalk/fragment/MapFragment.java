package com.desktalk.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.activity.BusTrackMapActivity;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.adapter.CustomInfoWindowAdapter;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SharedPreferences mSharedPreferences;
    private Button mButtonReload;
    private TextView mTextViewLastUpdated;
    private static final String TAG = MapFragment.class.getSimpleName();
    private Handler mHandler;
    private final long updateLocationInterval = 5000;
    private Runnable runnable;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_bar_bus_track_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Bus Tracking");

        mButtonReload = (Button) rootView.findViewById(R.id.buttonReload);
        mTextViewLastUpdated = (TextView) rootView.findViewById(R.id.textLastUpdated);
        mSharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);

        final String token = mSharedPreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");

        if (Connectivity.isConnected(getActivity())) {
            getUpdatedLocation(token);
        } else {
            Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
        }

        mButtonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(getActivity())) {
                    getUpdatedLocation(token);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHandler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "Calling run");
                if (Connectivity.isConnected(getActivity())) {
                    getUpdatedLocation(token);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }
                if (mHandler != null)
                    mHandler.postDelayed(this, updateLocationInterval);
            }
        };
        mHandler.postDelayed(runnable, updateLocationInterval);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        mHandler = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
        mHandler.removeCallbacks(runnable);
        mHandler = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getUpdatedLocation(String token) {
        Log.d(TAG, "getUpdatedLocation");
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);

        Call<JsonElement> mService = mInterfaceService.getStudentLocation(token, token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "getUpdatedLocation onResponse " + response.toString());
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (String.valueOf(jsonObject.get("status").toString()).contentEquals("success")) {
                                JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
                                Log.e(TAG, response.toString());
                                mTextViewLastUpdated.setText("Last Updated : " + jsonObjectResponse.get("last_location_time").toString());
                                mMap.clear();

                                LatLng defaultLoc = new LatLng(Double.valueOf(jsonObjectResponse.get("lat").toString()), Double.valueOf(jsonObjectResponse.get("lng").toString()));
                                MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)).position(defaultLoc).title("Current bus location\n").snippet(jsonObjectResponse.get("bus_no").toString() + "\n"
                                        + jsonObjectResponse.get("fname").toString() + " " + jsonObjectResponse.get("lname").toString() + "\n");

                                //Set Custom InfoWindow Adapter
                                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
                                mMap.setInfoWindowAdapter(adapter);
                                mMap.addMarker(marker).showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage().toString());
                            LatLng defaultLoc = new LatLng(12.9319396, 77.613859);
                            mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Current bus location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                        }
                    }

                } else if (response.code() == 404) {
                    Toast.makeText(getActivity(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
                    mHandler = null;
                    getActivity().finish();
                    Constants.startLogin(getActivity());
                } else {
                    Toast.makeText(getActivity(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    LatLng defaultLoc = new LatLng(12.9319396, 77.613859);
                    mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Current bus location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage().toString());
                LatLng defaultLoc = new LatLng(12.9319396, 77.613859);
                mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Current bus location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            }
        });
    }
}
