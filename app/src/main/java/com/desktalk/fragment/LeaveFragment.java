package com.desktalk.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.ClassDetailsModel;
import com.desktalk.Model.DatesModel;
import com.desktalk.Model.LeaveDetailsModel;
import com.desktalk.Model.StudentListModel;
import com.desktalk.activity.AttendenceHistoryActivity;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.adapter.Leave_Adapter;
import com.desktalk.adapter.ViewPagerAdapter;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.desktalk.util.PublicMethods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LeaveFragment extends Fragment implements View.OnClickListener {

    Button Start_Date, End_Date, submit_apply;
    ImageView start_date_picker, end_date_picker;
    ImageView choosen_image;
    TextView no_file_choosen;
    private boolean daterange_boolean = false;
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;

    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String resStardate, resEndate;
    private OnLeaveFragmentBackPress mListener;
    DateFormat dateFormatter;

    private static final String TAG = LeaveFragment.class.getSimpleName();
    private Spinner mSpinnerClass;
    private ArrayList<ClassDetailsModel> modelArrayList = new ArrayList<ClassDetailsModel>();
    public static ArrayList<LeaveDetailsModel> pendingLeavesList = new ArrayList<LeaveDetailsModel>();
    public static ArrayList<LeaveDetailsModel> approvedList = new ArrayList<LeaveDetailsModel>();
    public static ArrayList<LeaveDetailsModel> rejectList = new ArrayList<LeaveDetailsModel>();
    private SharedPreferences mSharedPreferences;
    private static ViewPager viewPager;
    static ViewPagerAdapter adapter;
    static TabLayout tabLayout;
    private static String token, mSelectedClass;
    private AlertDialog alertDialog;
    private ArrayList<String> mArrayListStudentList = new ArrayList<String>();
    private ArrayList<StudentListModel> arrayStudentListModel = new ArrayList<StudentListModel>();
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = getContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leave, container, false);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Leave Application");
        mSpinnerClass = (Spinner) rootView.findViewById(R.id.spinner_class);

        viewPager = (ViewPager) rootView.findViewById(R.id.leave_viewpager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fragment_leave_fab);
        CardView cardView = (CardView) rootView.findViewById(R.id.card_selectclass);
        fab.bringToFront();

        mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);
        token = mSharedPreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");
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

        if (Constants.USER_ID == Constants.USER_TEACHER) {
            cardView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

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

        } else {
            cardView.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            SharedPreferences mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_STUDENT_LIST, Context.MODE_PRIVATE);
            String jsonList = mSharedPreferences.getString(Constants.PREFERENCE_KEY_STUDENTLIST_JSON, "");
            arrayStudentListModel = getStudentList(jsonList);
            for (int i = 0; i < arrayStudentListModel.size(); i++) {
                mArrayListStudentList.add(arrayStudentListModel.get(i).getFname() + " " + arrayStudentListModel.get(i).getLname());
            }

            if ((!token.contentEquals("") || token != null) && (!UID.contentEquals("") || UID != null)) {

                if (Connectivity.isConnected(getActivity())) {
                    getLeavesByClass(token, null, false, true);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "token null");
                Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
            }
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialodView = new AlertDialog.Builder(getActivity());
                View dialog = getActivity().getLayoutInflater().inflate(R.layout.apply_leave, null);
                dialodView.setView(dialog);
                alertDialog = dialodView.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                final EditText editTextTitle = (EditText) dialog.findViewById(R.id.editTextTitle);
                final EditText editTextReason = (EditText) dialog.findViewById(R.id.EdittextReason);
                final Spinner spinnerList = (Spinner) dialog.findViewById(R.id.spinnerStudentList);

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayListStudentList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinnerList.setAdapter(spinnerArrayAdapter);

                Button cancel_apply = (Button) dialog.findViewById(R.id.btn_cancel_apply);
                submit_apply = (Button) dialog.findViewById(R.id.btn_submit_apply);

                Start_Date = (Button) dialog.findViewById(R.id.startdate);
                start_date_picker = (ImageView) dialog.findViewById(R.id.startdatepicker);
                End_Date = (Button) dialog.findViewById(R.id.enddate);
                end_date_picker = (ImageView) dialog.findViewById(R.id.enddatepicker);

                setDateTimeField();
                cancel_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                submit_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (String.valueOf(Start_Date.getText()).contentEquals("Start Date") && String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                            Toast.makeText((Context) mListener, "Select Date range", Toast.LENGTH_LONG).show();
                            daterange_boolean = false;
                        } else if (String.valueOf(Start_Date.getText()).contentEquals("Start Date")) {
                            Toast.makeText((Context) mListener, "Select Start Date", Toast.LENGTH_LONG).show();
                            daterange_boolean = false;
                        } else if (String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                            Toast.makeText((Context) mListener, "Select End Date", Toast.LENGTH_LONG).show();
                            daterange_boolean = false;
                        } else {
                            /*daterange_boolean = true;
                            if (daterange_boolean == true) {


                                daterange_boolean = false;
                            } else {
                                Toast.makeText(getContext(), "Daterange is not updated", Toast.LENGTH_SHORT).show();
                            }*/
                            String stuId = arrayStudentListModel.get(spinnerList.getSelectedItemPosition()).getUser_id();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("student_id", stuId);
                            hashMap.put("title", editTextTitle.getText().toString());
                            hashMap.put("reason", editTextReason.getText().toString());
                            hashMap.put("from_date", Start_Date.getText().toString());
                            hashMap.put("to_date", End_Date.getText().toString());
                            if (Connectivity.isConnected(getActivity())) {
                                PublicMethods.applyForLeave(getActivity(), TAG, token, hashMap);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLeaveFragmentBackPress) {
            mListener = (OnLeaveFragmentBackPress) context;
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
    public void onClick(View v) {
        if (v == start_date_picker || v == Start_Date) {
            fromDatePickerDialog.show();
        } else if (v == end_date_picker || v == End_Date) {
            toDatePickerDialog.show();
        }
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
    public interface OnLeaveFragmentBackPress {
        // TODO: Update argument type and name
        void OnLeaveFragmentBackPress();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            try {
                no_file_choosen.setVisibility(View.GONE);
                choosen_image.setVisibility(View.VISIBLE);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                choosen_image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void setDateTimeField() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        start_date_picker.setOnClickListener(this);
        end_date_picker.setOnClickListener(this);
        Start_Date.setOnClickListener(this);
        End_Date.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                // fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                resStardate = dateFormatter.format(newDate.getTime());
                String[] startDate = resStardate.split("-");

                Start_Date.setText(startDate[2] + "-" + startDate[1] + "-" + startDate[0]);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                //  toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//disable future dates in calender
                resEndate = dateFormatter.format(newDate.getTime());
                String[] endDate = resEndate.split("-");
                End_Date.setText(endDate[2] + "-" + endDate[1] + "-" + endDate[0]);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void getClassDetails(final String token, String UID) {
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
                                mSelectedClass = modelArrayList.get(mSpinnerClass.getSelectedItemPosition()).getClass_id();
                                getLeavesByClass(token, mSelectedClass, false, false);
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

    public static void getLeavesByClass(String token, String ClassID, final boolean fromAdapter, final boolean isFroParent) {
        pendingLeavesList = new ArrayList<LeaveDetailsModel>();
        approvedList = new ArrayList<LeaveDetailsModel>();
        rejectList = new ArrayList<LeaveDetailsModel>();

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService;
        if (!isFroParent)
            mService = mInterfaceService.getLeavesbyClass(token, ClassID);
        else
            mService = mInterfaceService.getLeaveListforParent(token);

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
                                    LeaveDetailsModel leaveDetailsModel = gson.fromJson(array.getString(i), LeaveDetailsModel.class);
                                    if (leaveDetailsModel.getStatus().contentEquals(Constants.LEAVE_STATUS_PENDING)) {
                                        pendingLeavesList.add(leaveDetailsModel);
                                    } else if (leaveDetailsModel.getStatus().contentEquals(Constants.LEAVE_STATUS_APPROVED)) {
                                        approvedList.add(leaveDetailsModel);
                                    } else {
                                        rejectList.add(leaveDetailsModel);
                                    }
                                }
                                if (!fromAdapter)
                                    setFragments();
                                else {
//                                    adapter.notifyDataSetChanged();
//                                    viewPager.invalidate();
                                    setFragments();
                                }


                            } else {
                                //showProgress(false);
                                //Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    //Toast.makeText(getActivity(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
                    //getActivity().finish();
                    //Constants.startLogin(getActivity());
                } else {
                    //showProgress(false);
                    //Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private static void setFragments() {
        // Add Fragments to adapter one by one
        Bundle Syllabusbundle = new Bundle();
        Syllabusbundle.putString("Value", "Pending");
        Syllabusbundle.putString("token", token);
        LeaveStatusFragment pendingFragment = new LeaveStatusFragment();
        pendingFragment.setArguments(Syllabusbundle);

        adapter.mFragmentList.clear();

        Bundle Papersbundle = new Bundle();
        Papersbundle.putString("Value", "Approved");
        Papersbundle.putString("token", token);
        LeaveStatusFragment ApprovedFragment = new LeaveStatusFragment();
        ApprovedFragment.setArguments(Papersbundle);

        Bundle Notesbundle = new Bundle();
        Notesbundle.putString("Value", "Rejected");
        Notesbundle.putString("token", token);
        LeaveStatusFragment RejectedFragment = new LeaveStatusFragment();
        RejectedFragment.setArguments(Notesbundle);

        adapter.addFragment(pendingFragment, "Pending");
        adapter.addFragment(ApprovedFragment, "Approved");
        adapter.addFragment(RejectedFragment, "Rejected");

        viewPager.removeAllViews();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        adapter.notifyDataSetChanged();
    }

    /*private void applyForLeave(String token, HashMap<String, String> data) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.applyLeave(token, data);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Log.d(TAG, "Leave response " + response.body().toString());
                            Toast.makeText(getActivity(), "Leave applied successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            alertDialog.dismiss();
                        }
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(getActivity(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    Constants.startLogin(getActivity());
                } else {
                    //showProgress(false);
                    Toast.makeText(getActivity(), "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }*/

    private ArrayList<StudentListModel> getStudentList(String jsonData) {
        try {
            ArrayList<StudentListModel> listModels = new ArrayList<StudentListModel>();
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++) {
                Gson gson = new Gson();
                StudentListModel studentListModel = gson.fromJson(array.getString(i), StudentListModel.class);
                listModels.add(studentListModel);
            }
            Log.d(TAG, array.toString());
            return listModels;

        } catch (Exception e) {
            Log.d(TAG, "getStudentList Exception " + e.getMessage());
            return null;
        }

    }
}
