package com.desktalk.adapter;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.LeaveDetailsModel;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.fragment.LeaveFragment;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.desktalk.util.PublicMethods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Leave_Adapter extends RecyclerView.Adapter<Leave_Adapter.MyViewHolder> {
    private ArrayList<LeaveDetailsModel> mDataset;
    private Context mContext;
    private String category;
    private String loginToken;
    private final static String TAG = Leave_Adapter.class.getSimpleName();
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    private String resStardate, resEndate;
    Button Start_Date;
    ImageView start_date_picker;
    Button End_Date;
    ImageView end_date_picker;

    private HashMap<String, Integer> monthMap = new HashMap<String, Integer>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextViewTitle, mTextViewDate;
        public ImageView leave_image_edit, leave_image_right, leave_image_cross, leave_image_delete;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.cardViewTitle);
            mTextViewTitle = (TextView) v.findViewById(R.id.leave_title);
            mTextViewDate = (TextView) v.findViewById(R.id.leave_range);
            leave_image_edit = (ImageView) v.findViewById(R.id.leave_image_edit);
            leave_image_right = (ImageView) v.findViewById(R.id.leave_right);
            leave_image_cross = (ImageView) v.findViewById(R.id.leave_cross);
            leave_image_delete = (ImageView) v.findViewById(R.id.leave_image_delete);

            dates();
            if (Constants.USER_ID == Constants.USER_PARENT) {
                leave_image_right.setVisibility(View.GONE);
                leave_image_cross.setVisibility(View.GONE);
                if (category.equals("Pending")) {
                    leave_image_edit.setVisibility(View.VISIBLE);
                    leave_image_delete.setVisibility(View.VISIBLE);
                } else {
                    leave_image_edit.setVisibility(View.GONE);
                    leave_image_delete.setVisibility(View.GONE);
                }
            } else if (Constants.USER_ID == Constants.USER_TEACHER) {
                if (category.equals("Pending")) {
                    leave_image_right.setVisibility(View.VISIBLE);
                    leave_image_cross.setVisibility(View.VISIBLE);
                } else {
                    leave_image_right.setVisibility(View.GONE);
                    leave_image_cross.setVisibility(View.GONE);
                }

                leave_image_delete.setVisibility(View.GONE);
                leave_image_edit.setVisibility(View.GONE);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Leave_Adapter(ArrayList<LeaveDetailsModel> myDataset, Context myContextt, String clicked, String token) {
        category = clicked;
        mDataset = myDataset;
        mContext = myContextt;
        loginToken = token;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextViewTitle.setText(mDataset.get(position).getTitle());
        holder.mTextViewDate.setText("From: " + mDataset.get(position).getFrom_date() + " " + "\nTo: " + mDataset.get(position).getTo_date());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset.get(position).getTitle();
                Log.d("CardView", "CardView Clicked: " + currentValue);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.view_leave);
                dialog.setTitle("Accepted");
                Button dismis = (Button) dialog.findViewById(R.id.dismis_view);

                TextView title_for_leave = (TextView) dialog.findViewById(R.id.title_for_leave_status);
                TextView leave_tiltle = (TextView) dialog.findViewById(R.id.leave_tiltle);
                TextView leave_description = (TextView) dialog.findViewById(R.id.leave_description);
                TextView fromDate = (TextView) dialog.findViewById(R.id.textFromdate);
                TextView toDate = (TextView) dialog.findViewById(R.id.textTodate);

                leave_tiltle.setText(String.valueOf(mDataset.get(position).getTitle()));
                leave_description.setText(String.valueOf(mDataset.get(position).getReason()));
                fromDate.setText(String.valueOf(mDataset.get(position).getFrom_date()));
                toDate.setText(String.valueOf(mDataset.get(position).getTo_date()));
                title_for_leave.setText(String.valueOf(category));

                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.leave_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position) + " Clicked_Edit");

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.apply_leave);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                final TextView mTextViewName = (TextView) dialog.findViewById(R.id.textChildname);
                mTextViewName.setText(String.valueOf(mDataset.get(position).getUser_id()));
                final EditText editTextTitle = (EditText) dialog.findViewById(R.id.editTextTitle);
                editTextTitle.setText(String.valueOf(mDataset.get(position).getTitle()));
                final EditText editTextReason = (EditText) dialog.findViewById(R.id.EdittextReason);
                editTextReason.setText(String.valueOf(mDataset.get(position).getReason()));
                final Spinner spinnerList = (Spinner) dialog.findViewById(R.id.spinnerStudentList);
                spinnerList.setVisibility(View.GONE);
                Button cancel_apply = (Button) dialog.findViewById(R.id.btn_cancel_apply);
                Button submit_apply = (Button) dialog.findViewById(R.id.btn_submit_apply);

                Start_Date = (Button) dialog.findViewById(R.id.startdate);
                Start_Date.setText(String.valueOf(mDataset.get(position).getFrom_date()));
                start_date_picker = (ImageView) dialog.findViewById(R.id.startdatepicker);
                End_Date = (Button) dialog.findViewById(R.id.enddate);
                End_Date.setText(String.valueOf(mDataset.get(position).getTo_date()));
                end_date_picker = (ImageView) dialog.findViewById(R.id.enddatepicker);

                setDateTimeField(Start_Date.getText().toString(), End_Date.getText().toString());
                cancel_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                submit_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (String.valueOf(Start_Date.getText()).contentEquals("Start Date") && String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                            Toast.makeText(mContext, "Select Date range", Toast.LENGTH_LONG).show();
                            //daterange_boolean = false;
                        } else if (String.valueOf(Start_Date.getText()).contentEquals("Start Date")) {
                            Toast.makeText(mContext, "Select Start Date", Toast.LENGTH_LONG).show();
                            //daterange_boolean = false;
                        } else if (String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                            Toast.makeText(mContext, "Select End Date", Toast.LENGTH_LONG).show();
                            //daterange_boolean = false;
                        } else {
                            /*daterange_boolean = true;
                            if (daterange_boolean == true) {


                                daterange_boolean = false;
                            } else {
                                Toast.makeText(getContext(), "Daterange is not updated", Toast.LENGTH_SHORT).show();
                            }*/
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("student_id", String.valueOf(mDataset.get(position).getUser_id()));
                            hashMap.put("title", editTextTitle.getText().toString());
                            hashMap.put("reason", editTextReason.getText().toString());
                            hashMap.put("from_date", Start_Date.getText().toString());
                            hashMap.put("to_date", End_Date.getText().toString());
                            hashMap.put("id", String.valueOf(mDataset.get(position).getLeave_id()));
                            dialog.dismiss();
                            if (Connectivity.isConnected(mContext)) {
                                PublicMethods.updateLeave(mContext, TAG, loginToken, hashMap);
                            } else {
                                Toast.makeText(mContext, mContext.getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                Start_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fromDatePickerDialog.show();
                    }
                });
                start_date_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fromDatePickerDialog.show();
                    }
                });
                End_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDatePickerDialog.show();
                    }
                });
                end_date_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDatePickerDialog.show();
                    }
                });
            }
        });

        holder.leave_image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position) + " Clicked_Edit");
                if (Connectivity.isConnected(mContext)) {
                    PublicMethods.deleteLeave(mContext, TAG, loginToken, String.valueOf(mDataset.get(position).getLeave_id()));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.leave_image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("leave_id", mDataset.get(position).getLeave_id());
                map.put("status", Constants.LEAVE_STATUS_APPROVED);
                String classID = mDataset.get(position).getClass_id();

                Constants.updatLeaveStatus(TAG, loginToken, map, classID);
/*                LeaveFragment.approvedList.add(mDataset.get(position));
                LeaveFragment.pendingLeavesList.remove(position);
                notifyDataSetChanged();
*/
            }
        });
        holder.leave_image_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("leave_id", mDataset.get(position).getLeave_id());
                map.put("status", Constants.LEAVE_STATUS_REJECTED);
                String classID = mDataset.get(position).getClass_id();
                Constants.updatLeaveStatus(TAG, loginToken, map, classID);
                LeaveFragment.rejectList.add(mDataset.get(position));
                LeaveFragment.pendingLeavesList.remove(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setDateTimeField(String fromDate, String toDate) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        String[] fromDateSet = fromDate.split("-");

        final int day = Integer.valueOf(fromDateSet[0]);
        final int month = Integer.valueOf(monthMap.get(fromDateSet[1]));
        final int mYear = Integer.valueOf(fromDateSet[2]);

        fromDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                // fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                resStardate = dateFormatter.format(newDate.getTime());
                String[] startDate = resStardate.split("-");

                Start_Date.setText(startDate[0] + "-" + startDate[1] + "-" + startDate[2]);

            }

        }, mYear, month, day);

        String[] toDateSet = toDate.split("-");

        final int today = Integer.valueOf(toDateSet[0]);
        final int tomonth = Integer.valueOf(monthMap.get(toDateSet[1]));
        final int tomYear = Integer.valueOf(toDateSet[2]);
        toDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                //  toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//disable future dates in calender
                resEndate = dateFormatter.format(newDate.getTime());
                String[] endDate = resEndate.split("-");
                End_Date.setText(endDate[0] + "-" + endDate[1] + "-" + endDate[2]);
            }

        }, tomYear, tomonth, today);
    }

    private void dates(){
        monthMap.put("Jan",0);
        monthMap.put("Feb",1);
        monthMap.put("Mar",2);
        monthMap.put("Apr",3);
        monthMap.put("May",4);
        monthMap.put("Jun",5);
        monthMap.put("Jul",6);
        monthMap.put("Aug",7);
        monthMap.put("Sep",8);

        monthMap.put("Oct",9);
        monthMap.put("Nov",10);
        monthMap.put("Dec",11);
    }
}
