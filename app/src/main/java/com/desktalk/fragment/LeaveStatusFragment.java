package com.desktalk.fragment;

/**
 * Created by Pavan.Chunchula on 16-03-2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Model.LeaveDetailsModel;
import com.desktalk.adapter.Leave_Adapter;
import com.desktalk.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

;

public class LeaveStatusFragment extends Fragment {
    String Clicked = " ";
    private ArrayList<LeaveDetailsModel> leaveDetailsModels = new ArrayList<LeaveDetailsModel>();

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
        String token = this.getArguments().getString("token");
        TextView textNoLeaveMsg = (TextView) rootView.findViewById(R.id.textNoLeaveMsg);
        if (category.equals("Pending")) {
            Clicked = "Pending";
            if (LeaveFragment.pendingLeavesList.size() != 0) {
                leaveDetailsModels = LeaveFragment.pendingLeavesList;
            } else {
                textNoLeaveMsg.setText("Great !!! No Pending Leaves");
            }
        } else if (category.equals("Approved")) {
            Clicked = "Approved";
            if (LeaveFragment.approvedList.size() != 0) {
                leaveDetailsModels = LeaveFragment.approvedList;
            } else {
                textNoLeaveMsg.setText("No Approved Leaves");
            }
        } else if (category.equals("Rejected")) {
            Clicked = "Rejected";
            if (LeaveFragment.rejectList.size() != 0) {
                leaveDetailsModels = LeaveFragment.rejectList;
            } else {
                textNoLeaveMsg.setText("Great !!! No Rejected Leaves");
            }
        }

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recylerview);
        rv.setHasFixedSize(true);
        Leave_Adapter adapter = new Leave_Adapter(leaveDetailsModels, getContext(), category, token);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}