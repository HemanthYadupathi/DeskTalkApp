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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.adapter.ViewPagerAdapter;
import com.desktalk.util.Constants;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcademicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private OnFragmentInteractionListener mListener;
    DateFormat dateFormatter;

    public LeaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcademicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaveFragment newInstance(String param1, String param2) {
        Log.d(param1, param2);
        LeaveFragment fragment = new LeaveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = getContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leave, container, false);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Leave Application");

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.leave_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fragment_leave_fab);
        if (Constants.USER_ID == Constants.USER_PARENT) {

        } else if (Constants.USER_ID == Constants.USER_TEACHER) {
            fab.setVisibility(View.GONE);
        }else if (Constants.USER_ID == Constants.USER_STUDENT) {
            fab.setVisibility(View.GONE);
        }
//        AppCompatSpinner spinner_class = (AppCompatSpinner) rootView.findViewById(R.id.spinner_class);
        fab.bringToFront();

//        if (Constants.USER_ID == Constants.USER_TEACHER) {
//            spinner_class.setVisibility(View.VISIBLE);
//            fab.setVisibility(View.VISIBLE);
//        } else if (Constants.USER_ID == Constants.USER_PARENT) {
//            spinner_class.setVisibility(View.GONE);
//            fab.setVisibility(View.GONE);
//        } else {
//            spinner_class.setVisibility(View.GONE);
//            fab.setVisibility(View.GONE);
//        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialodView = new AlertDialog.Builder(getActivity());
                View dialog = getActivity().getLayoutInflater().inflate(R.layout.apply_leave, null);
                dialodView.setView(dialog);
                final AlertDialog alertDialog = dialodView.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
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
                                                    public void onClick(View v) {
                                                        if (String.valueOf(Start_Date.getText()).contentEquals("Start Date") && String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                                                            Toast.makeText((Context) mListener, "Select Date range", Toast.LENGTH_LONG).show();
                                                            daterange_boolean = false;
                                                        } else if (String.valueOf(Start_Date.getText()).contentEquals("Start Date")) {
                                                            Toast.makeText((Context) mListener, "Select Start Date", Toast.LENGTH_LONG).show();
                                                            daterange_boolean = false;
                                                        } else if (String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                                                            Toast.makeText((Context) mListener, "Select End Date", Toast.LENGTH_LONG).show();

                                                            //  Snackbar.make(coordinatorLayout, "Select date range", Snackbar.LENGTH_LONG).show();
                                                            daterange_boolean = false;
                                                        } else {

                                                            Log.d("Startdate", resStardate);
                                                            Log.d("Enddate", resEndate);
                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                            editor.putString("Sdate", resStardate);
                                                            editor.putString("Edate", resEndate);
                                                            editor.commit();
//                                                            mCustomDialog.dismiss();
                   /* Intent i = new Intent(MainDashboard.this,ReachFragment.class);
                    i.putExtra("start_date",resStardate);
                    i.putExtra("end_date",resEndate);*/
                                                            daterange_boolean = true;
                                                            if (daterange_boolean == true) {
//                        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                                                                //Adding adapter to pager
//                        viewPager.setAdapter(adapter)

//                                                                adapter.notifyDataSetChanged();
//                                                                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                                                                daterange_boolean = false;
                                                                //  Snackbar.make(coordinatorLayout, "Daterange not upadted", Snackbar.LENGTH_LONG).show();
                                                                //   Toast.makeText(getApplicationContext(), "Date range Saved", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Toast.makeText(getContext(), "Daterange is not updated", Toast.LENGTH_SHORT).show();
                                                                // Snackbar.make(coordinatorLayout, "Daterange not upadted", Snackbar.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    }
                                                }

                );


//                choosen_image = (ImageView) dialog.findViewById(R.id.choosen_image);
//                no_file_choosen = (TextView) dialog.findViewById(R.id.no_file_choosen);

//                add_images.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                        photoPickerIntent.setType("file/*");
//                        startActivityForResult(photoPickerIntent, 1);
//                    }
//                });

//                final AlertDialog alertDialog = dialodView.create();
//                alertDialog.show();
            }
        });

        // Add Fragments to adapter one by one
        Bundle Syllabusbundle = new Bundle();
        Syllabusbundle.putString("Value", "Pending");
        LeaveStatusFragment pendingFragment = new LeaveStatusFragment();
        pendingFragment.setArguments(Syllabusbundle);

        Bundle Papersbundle = new Bundle();
        Papersbundle.putString("Value", "Approved");
        LeaveStatusFragment ApprovedFragment = new LeaveStatusFragment();
        ApprovedFragment.setArguments(Papersbundle);

        Bundle Notesbundle = new Bundle();
        Notesbundle.putString("Value", "Rejected");
        LeaveStatusFragment RejectedFragment = new LeaveStatusFragment();
        RejectedFragment.setArguments(Notesbundle);


        adapter.addFragment(pendingFragment, "Pending");
        adapter.addFragment(ApprovedFragment, "Approved");
        adapter.addFragment(RejectedFragment, "Rejected");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

//                Date Sdate = null;
//
//                Date today = new Date();
//                Date EndatefromButtton = null;
//                Calendar cal = Calendar.getInstance();
//
//                //   dateFormatter.format(cal.getTime());
//                try {
//                    Sdate = dateFormatter.parse(resStardate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    EndatefromButtton = dateFormatter.parse(resEndate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    today = dateFormatter.parse(dateFormatter.format(cal.getTime()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                if (today.after(Sdate) || today.equals(Sdate)) {
//                    if (!String.valueOf(End_Date.getText()).contentEquals("End Date")) {
//                        if (EndatefromButtton.after(Sdate)) {
//
//                        } else {
//
//
//                            if (EndatefromButtton.equals(Sdate)) {
//
//                            } else {
//                                Start_Date.setText("Start Date");
//                                Toast.makeText(getContext(), "Start Date must be before End Date", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }
//
//                } else {
//                    Start_Date.setText("Start Date");
//                    Toast.makeText(getContext(), "Start Date should be within today", Toast.LENGTH_SHORT).show();
//                }

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
//                Date Edate = null;
//
//                Date today = new Date();
//                Date startdateref = null;
//                Calendar cal = Calendar.getInstance();
//
//                try {
//                    today = dateFormatter.parse(dateFormatter.format(cal.getTime()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Edate = dateFormatter.parse(resEndate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    startdateref = dateFormatter.parse(resStardate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (today.after(Edate) || today.equals(Edate)) {
//                    if (startdateref != null && !String.valueOf(Start_Date.getText()).contentEquals("Start Date")) {
//                        if ((startdateref.after(Edate)) && !startdateref.before(Edate)) {
//                            End_Date.setText("End Date");
//                            Toast.makeText(getContext(), "End Date must be after Start Date", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        End_Date.setText("End Date");
//                        Toast.makeText(getContext(), "Select Start Date", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    End_Date.setText("End Date");
//                    Toast.makeText(getContext(), "End Date should be within today", Toast.LENGTH_SHORT).show();
//                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
