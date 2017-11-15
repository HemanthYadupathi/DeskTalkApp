package com.desktalk.fragment;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import com.activity.desktalkapp.R;
import com.desktalk.adapter.Student_Adapter;
import com.desktalk.adapter.Teacher_Adapter;
import com.desktalk.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyllabusFragment extends Fragment {

    public SyllabusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedpreferences;
        SharedPreferences.Editor editor;


        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        String category = this.getArguments().getString("Value");
        ArrayList<String> list = new ArrayList<String>();
        if (category.equals("Syllabus")) {
            if (Constants.USER_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            }
            if (Constants.USER_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
            }
        } else if (category.equals("Previous Question Papers")) {

            if (Constants.USER_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            }
            if (Constants.USER_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 1");
                list.add("English 2");
            }
        } else if (category.equals("Important Notes")) {

            if (Constants.USER_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            }
            if (Constants.USER_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
            }
        }

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recylerview);
        rv.setHasFixedSize(true);
        try {

            if (sharedpreferences.getString("role_name", "").contentEquals("Student")) {
                Student_Adapter adapter = new Student_Adapter(list, getContext());
                rv.setAdapter(adapter);
            }
            if (sharedpreferences.getString("role_name", "").contentEquals("Teacher")) {
                Teacher_Adapter adapter = new Teacher_Adapter(list, getContext());
                rv.setAdapter(adapter);
            }
//        Teacher_Adapter adapter = new Teacher_Adapter(list, getContext());

        } catch (Exception e) {
            e.printStackTrace();
        }
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}