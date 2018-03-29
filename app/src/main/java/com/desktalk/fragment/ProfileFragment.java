package com.desktalk.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.UserDataModel;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.activity.EditProfileActivity;
import com.desktalk.activity.ProfileActivity;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ProfileFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView prof_Fname, prof_Mname, prof_DOB, prof_BG, prof_Mnumber, prof_Address, prof_Hobby, prof_Skill, prof_Description;
    private SharedPreferences sharedpreferences;
    Editor editor;
    private OnFragmentInteractionListener mListener;

    Toolbar mToolbar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        //((DashboardActivity) getActivity()).setToolbar(mToolbar, "Fannie Hunt");

        View v = rootView.findViewById(R.id.id_content);

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), (View) fab, "profile");
                startActivity(intent, options.toBundle());

            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NestedScrollView nsv = (NestedScrollView) rootView.findViewById(R.id.nestedscrollView);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        prof_Fname = (TextView) rootView.findViewById(R.id.prof_Fname);
        prof_Mname = (TextView) rootView.findViewById(R.id.prof_Mname);
        prof_DOB = (TextView) rootView.findViewById(R.id.prof_DOB);
        prof_BG = (TextView) rootView.findViewById(R.id.prof_BG);
        prof_Mnumber = (TextView) rootView.findViewById(R.id.prof_Mnumber);
        prof_Address = (TextView) rootView.findViewById(R.id.prof_Address);
        prof_Hobby = (TextView) rootView.findViewById(R.id.prof_Hobby);
        prof_Skill = (TextView) rootView.findViewById(R.id.prof_Skill);
        prof_Description = (TextView) rootView.findViewById(R.id.textDesc);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = sharedpreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");
        if (!token.contentEquals("") || token != null) {

            if (Connectivity.isConnected(getActivity())) {
                showProgress(true);
                getUserProfile(token);
            } else {
                Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "token null");
            Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserProfile(String token) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.getUserProfile(token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (String.valueOf(jsonObject.get("status").toString()).contentEquals("success")) {
                                setProfileData(jsonObject.getJSONObject("response").toString());

                            } else {
                                showProgress(false);
                                Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    showProgress(false);
                    getActivity().finish();
                    Constants.startLogin(getActivity());
                    Toast.makeText(getActivity(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(false);
                    Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void setProfileData(String data) {
        Gson gson = new Gson();
        UserDataModel userData = gson.fromJson(data, UserDataModel.class);

        if (!userData.getFname().toString().contentEquals("") || !userData.getLname().toString().contentEquals("")) {
            ((DashboardActivity) getActivity()).setToolbar(mToolbar, userData.getFname().toString() + " " + userData.getLname().toString());
            /*setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();*/
        }

        if (!userData.getFather_name().toString().contentEquals("")) {
            prof_Fname.setText(userData.getFather_name().toString());
        } else {
            prof_Fname.setText(" -- ");
        }
        if (!userData.getMother_name().toString().contentEquals("")) {
            prof_Mname.setText(userData.getMother_name().toString());
        } else {
            prof_Mname.setText(" -- ");
        }
        if (!userData.getDate_of_birth().toString().contentEquals("")) {
            prof_DOB.setText(userData.getDate_of_birth().toString());
        } else {
            prof_DOB.setText(" -- ");
        }
        if (!userData.getBlood_group().toString().contentEquals("")) {
            prof_BG.setText(userData.getBlood_group().toString());
        } else {
            prof_BG.setText(" -- ");
        }
        if (!userData.getMobile().toString().contentEquals("")) {
            prof_Mnumber.setText(userData.getMobile().toString());
        } else {
            prof_Mnumber.setText(" -- ");
        }
        if (!userData.getAddress().toString().contentEquals("")) {
            prof_Address.setText(userData.getAddress().toString());
        } else {
            prof_Address.setText(" -- ");
        }
        if (!userData.getHobbies().toString().contentEquals("")) {
            prof_Hobby.setText(userData.getHobbies().toString().replace(",", "\n"));
        } else {
            prof_Hobby.setText(" -- ");
        }
        if (!userData.getSkills().toString().contentEquals("")) {
            prof_Skill.setText(userData.getSkills().toString().replace(",", "\n"));
        } else {
            prof_Skill.setText(" -- ");
        }
        if (!userData.getDescription().toString().contentEquals("")) {
            prof_Description.setText(userData.getDescription().toString());
        } else {
            //rootView.findViewById(R.id.card_view_description).setVisibility(View.GONE);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProfileForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mProfileForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProfileForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProfileForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
