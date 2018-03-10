package com.desktalk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.adapter.CardFragmentPagerAdapter;
import com.desktalk.adapter.MarkAttendanceStudentAdapter;
import com.desktalk.util.ShadowTransformer;

import static com.desktalk.fragment.HomeFragment.dpToPixels;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MarkAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarkAttendanceFragment extends Fragment {
    private Toolbar mToolbar;
    private ViewPager viewPager;

    public static MarkAttendanceFragment newInstance(String param1, String param2) {
        MarkAttendanceFragment fragment = new MarkAttendanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        /*MarkAttendanceStudentAdapter pagerAdapter = new MarkAttendanceStudentAdapter(getActivity().getSupportFragmentManager(), dpToPixels(2, getActivity()));
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Attendance");*/

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
