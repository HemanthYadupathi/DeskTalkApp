package com.desktalk.fragment;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import com.activity.desktalkapp.R;
import com.desktalk.util.MyAdapter;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        String category = this.getArguments().getString("Value");
        Integer selected_ID = this.getArguments().getInt("selected_ID");
        ArrayList<String> list = new ArrayList<String>();
        if (category.equals("Syllabus")) {
            if (selected_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            } else if (selected_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
            }
        } else if (category.equals("Previous Question Papers")) {
            if (selected_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            } else if (selected_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
            }
        } else if (category.equals("Important Notes")) {
            if (selected_ID == 2) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
                list.add("English 3");
                list.add("English 4");
                list.add("English 5");
                list.add("English 6");
            } else if (selected_ID == 0) {
                list.clear();
                list.add("English 1");
                list.add("English 2");
            }
        }
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recylerview);
        rv.setHasFixedSize(true);
        MyAdapter adapter = new MyAdapter(list, getContext());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}