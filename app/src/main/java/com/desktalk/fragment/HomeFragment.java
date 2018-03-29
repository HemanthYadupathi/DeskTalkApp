package com.desktalk.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.StudentPerformanceModel;
import com.desktalk.adapter.CardFragmentPagerAdapter;
import com.desktalk.util.Apis;
import com.desktalk.util.Constants;
import com.desktalk.util.ShadowTransformer;
import com.desktalk.activity.DashboardActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private ArrayList<StudentPerformanceModel> studentPerformanceModels = new ArrayList<StudentPerformanceModel>();

    BarData data;
    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private ViewPager viewPager;
    private Timer timer;
    private int currentPage = 0;
    private ProgressBar mProgressBarBehaviour, mProgressBarAcademic, mProgressBarExtra;
    private TextView mTextViewBehaviour, mTextViewAcademic, mTextViewExtra;
    private SharedPreferences sharedpreferences;
    private CardView mCardViewPerformance;
    private String token;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(), dpToPixels(2, getActivity()));
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);

        mProgressBarBehaviour = (ProgressBar) view.findViewById(R.id.circularProgressbar);
        mProgressBarAcademic = (ProgressBar) view.findViewById(R.id.circularProgressbarAcd);
        mProgressBarExtra = (ProgressBar) view.findViewById(R.id.circularProgressbarExtra);

        mTextViewBehaviour = (TextView) view.findViewById(R.id.textBehValue);
        mTextViewAcademic = (TextView) view.findViewById(R.id.textAcdValue);
        mTextViewExtra = (TextView) view.findViewById(R.id.textExtraValue);

        mCardViewPerformance = (CardView) view.findViewById(R.id.card_view_stdPerformance);

        sharedpreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE); //1
        token = sharedpreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");

        getStudentPerformance(TAG, token);

        if (Constants.USER_ID == Constants.USER_TEACHER) {
            view.findViewById(R.id.card_view_timetable).setVisibility(View.GONE);
            view.findViewById(R.id.card_view_stdAttendence).setVisibility(View.GONE);
            view.findViewById(R.id.card_view_stdPerformance).setVisibility(View.GONE);
        } else {
            BarChart barChart = (BarChart) view.findViewById(R.id.barchart);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(8f, 0));
            entries.add(new BarEntry(2f, 1));
            entries.add(new BarEntry(5f, 2));
            entries.add(new BarEntry(20f, 3));
            entries.add(new BarEntry(30f, 4));
            entries.add(new BarEntry(19f, 5));
            entries.add(new BarEntry(2f, 6));
            entries.add(new BarEntry(5f, 7));
            entries.add(new BarEntry(20f, 8));
            entries.add(new BarEntry(15f, 9));
            entries.add(new BarEntry(19f, 10));


            BarDataSet bardataset = new BarDataSet(entries, "Cells");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Jan");
            labels.add("Feb");
            labels.add("Mar");
            labels.add("Apr");
            labels.add("May");
            labels.add("Jun");
            labels.add("Jul");
            labels.add("Aug");
            labels.add("Sep");
            labels.add("Oct");
            labels.add("Nov");

            data = new BarData(labels, bardataset);
            barChart.setData(data); // set the data and list of lables into chart
            barChart.setDescription(" ");  // set the description
            barChart.setScaleXEnabled(true);
            barChart.setScaleYEnabled(false);
            barChart.setExtraBottomOffset(10);
            barChart.animateXY(0, 1000);
            barChart.setPinchZoom(false);
            barChart.getLegend().setEnabled(false);
//        barChart.getXAxis().setTextSize(7f);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            barChart.isDrawValueAboveBarEnabled();//returns true or false

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setValueFormatter(new YAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, YAxis yAxis) {
                    return Math.round(value) + "";
                }
            });

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setEnabled(false);

            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

            barChart.animateY(5000);

        }
        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "DeskTalk");

        setupAutoPager();
        return view;
    }

    private void setupAutoPager() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

                viewPager.setCurrentItem(currentPage, true);
                if (currentPage == 4) {
                    currentPage = 0;
                } else {
                    ++currentPage;
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 2000);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
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

    private void getStudentPerformance(final String TAG, final String token) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.getStudentPerformance(token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (String.valueOf(jsonObject.get("status").toString()).contentEquals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    StudentPerformanceModel studentPerformanceModel = gson.fromJson(jsonArray.getString(i), StudentPerformanceModel.class);
                                    studentPerformanceModels.add(studentPerformanceModel);
                                    mCardViewPerformance.setVisibility(View.VISIBLE);
                                }
                                if (studentPerformanceModels.size() != 0) {
                                    mProgressBarBehaviour.setProgress(Integer.valueOf(studentPerformanceModels.get(0).getClass_behavior()));
                                    mProgressBarAcademic.setProgress(Integer.valueOf(studentPerformanceModels.get(0).getAcademic_performance()));
                                    mProgressBarExtra.setProgress(Integer.valueOf(studentPerformanceModels.get(0).getExtra_curricular_activities()));

                                    mTextViewBehaviour.setText(studentPerformanceModels.get(0).getClass_behavior() + "%");
                                    mTextViewAcademic.setText(studentPerformanceModels.get(0).getAcademic_performance() + "%");
                                    mTextViewExtra.setText(studentPerformanceModels.get(0).getExtra_curricular_activities() + "%");
                                } else {
                                    mCardViewPerformance.setVisibility(View.GONE);
                                }
                                Log.d(TAG, "Student performance response " + response.body().toString());
                            } else {
                                mCardViewPerformance.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Log.e(TAG, "Session expired, please login again");
                } else {
                    Log.e(TAG, "Something went wrong, please login again");
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
