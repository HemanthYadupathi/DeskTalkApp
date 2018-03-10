package com.desktalk.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.ClassDetailsModel;
import com.desktalk.activity.AttendenceHistoryActivity;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.activity.MarkAttendenceActivity;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mButtonMarkAttendance, mButtonAttendanceHistory;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences mSharedPreferences;
    private static final String TAG = AttendanceFragment.class.getSimpleName();
    private Spinner mSpinnerClass;
    private ArrayList<ClassDetailsModel> modelArrayList = new ArrayList<ClassDetailsModel>();
    private NavigationView navigationView;


    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Attendance");

        mButtonAttendanceHistory = (Button) rootView.findViewById(R.id.button_attendanceHistory);
        mButtonMarkAttendance = (Button) rootView.findViewById(R.id.button_markAtt);
        mSpinnerClass = (Spinner) rootView.findViewById(R.id.spinner_class);


        mButtonAttendanceHistory.setOnClickListener(this);
        mButtonMarkAttendance.setOnClickListener(this);

        mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);
        String token = mSharedPreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");
        String UID = null;

        String userData = mSharedPreferences.getString(Constants.PREFERENCE_KEY_USERDATA, "");

        if (!userData.contentEquals("") || userData != null) {
            try {
                JsonObject userDataObject = new JsonParser().parse(userData).getAsJsonObject();
                UID = userDataObject.get("user_id").getAsString();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        if ((!token.contentEquals("") || token != null) && (!UID.contentEquals("") || UID != null)) {

            if (Connectivity.isConnected(getActivity())) {
                getClassDetails(token, UID);
            } else {
                Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "token null");
            Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
        }
        return rootView;
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

    @Override
    public void onClick(View view) {
        /*Fragment fragment = null;
        switch (view.getId()) {

            case R.id.button_markAtt:
                try {
                    fragment = MarkAttendanceFragment.class.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.attendance_framelayout, fragment).commit();
                }
                break;
            case R.id.button_attendanceHistory:
                try {
                    fragment = AttendanceHistoryFragment.class.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.attendance_framelayout, fragment).commit();
                }
                break;
        }*/
        String classID = null, classname = null;
        if (modelArrayList.size() != 0) {
            classID = modelArrayList.get(mSpinnerClass.getSelectedItemPosition()).getClass_id();
            classname = modelArrayList.get(mSpinnerClass.getSelectedItemPosition()).getClass_name() + " " + modelArrayList.get(mSpinnerClass.getSelectedItemPosition()).getSection();
        }
        switch (view.getId()) {
            case R.id.button_attendanceHistory:
                if (classID != null) {
                    Log.d(TAG, "Passing Class ID: " + classID);
                    Intent intent = new Intent(getActivity(), AttendenceHistoryActivity.class);
                    intent.putExtra("classID", classID);
                    intent.putExtra("className", classname);
                    startActivity(intent);
                }
                break;
            case R.id.button_markAtt:
                if (classID != null) {
                    Log.d(TAG, "Passing Class ID: " + classID);
                    Intent intent = new Intent(getActivity(), MarkAttendenceActivity.class);
                    intent.putExtra("classID", classID);
                    intent.putExtra("className", classname);
                    startActivity(intent);
                }
                break;
        }
    }

    private void getClassDetails(String token, String UID) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.getClassDetails(token, UID);

        final ArrayList<String> classes = new ArrayList<String>();
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (String.valueOf(jsonObject.get("status").toString()).contentEquals("success")) {
                                JSONArray array = jsonObject.getJSONArray("response");
                                for (int i = 0; i < array.length(); i++) {
                                    Gson gson = new Gson();
                                    ClassDetailsModel classModel = gson.fromJson(array.getString(i), ClassDetailsModel.class);
                                    modelArrayList.add(classModel);
                                    classes.add(modelArrayList.get(i).getClass_name() + " " + modelArrayList.get(i).getSection());
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, classes);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                mSpinnerClass.setAdapter(spinnerArrayAdapter);
                                Log.d(TAG, "Class : " + classes.toString());
                            } else {
                                //showProgress(false);
                                Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(getActivity(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    Constants.startLogin(getActivity());
                } else {
                    //showProgress(false);
                    Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
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
