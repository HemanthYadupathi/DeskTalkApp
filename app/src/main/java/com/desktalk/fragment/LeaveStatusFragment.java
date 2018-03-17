package com.desktalk.fragment;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.adapter.Leave_Adapter;
import com.desktalk.util.Constants;

import java.util.ArrayList;

;

public class LeaveStatusFragment extends Fragment {
    String Clicked = " ";

    public LeaveStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        String category = this.getArguments().getString("Value");
        ArrayList<String> list = new ArrayList<String>();
        if (category.equals("Pending")) {
            Clicked = "Pending";
            if (Constants.USER_ID == Constants.USER_PARENT) {
                list.clear();
                list.add("English");
                list.add("Kannada");
                list.add("Computer");
            } else {
                list.clear();
                list.add("English");
                list.add("Kannada");
                list.add("Computer");
            }
        } else if (category.equals("Approved")) {
            Clicked = "Approved";
            if (Constants.USER_ID == Constants.USER_PARENT) {
                list.clear();
                list.add("English");
                list.add("Kannada");
                list.add("Computer");
            } else {
                list.clear();
                list.add("English");
                list.add("Kannada");
                list.add("Computer");
            }
        } else if (category.equals("Rejected")) {
            Clicked = "Rejected";
            if (Constants.USER_ID == Constants.USER_PARENT) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            } else {
                list.clear();
                list.add("English 1");
                list.add("English 2");
            }
        }

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recylerview);
        rv.setHasFixedSize(true);
        if (Constants.USER_ID == Constants.USER_TEACHER) {
            Leave_Adapter adapter = new Leave_Adapter(list, getContext(),Clicked);
            rv.setAdapter(adapter);

        }
        if (Constants.USER_ID == Constants.USER_PARENT) {
            Leave_Adapter adapter = new Leave_Adapter(list, getContext(), Clicked);
            rv.setAdapter(adapter);

        } else {
            Leave_Adapter adapter = new Leave_Adapter(list, getContext(),Clicked);
            rv.setAdapter(adapter);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}