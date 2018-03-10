package com.desktalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.desktalkapp.R;
import com.desktalk.activity.AttendenceHistoryActivity;

import java.util.ArrayList;


public class AttendanceHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AttendanceHistoryRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLayout;
        private TextView mTextView;
        private ImageView mImageView;

        public MyViewHolder(View v) {
            super(v);

            mLayout = (LinearLayout) v.findViewById(R.id.layout_click);
            mTextView = (TextView) v.findViewById(R.id.list_item_text);
            mImageView = (ImageView) v.findViewById(R.id.list_item_imageview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AttendanceHistoryRecyclerViewAdapter(ArrayList<String> myDataset, Context myContextt) {
        mDataset = myDataset;
        mContext = myContextt;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AttendanceHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position));
        holder.mImageView.setBackground(mContext.getDrawable(R.mipmap.ic_edit_blue));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset.get(position);
                Log.d("CardView", "CardView Clicked: " + currentValue);
                mContext.startActivity(new Intent(mContext, AttendenceHistoryActivity.class));
            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("edit", mDataset.get(position) + " Clicked_Download");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
