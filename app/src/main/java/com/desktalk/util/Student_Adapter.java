package com.desktalk.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.desktalkapp.R;

import java.util.ArrayList;

/**
 * Created by Pavan.Chunchula on 03-11-2017.
 */

public class Student_Adapter extends RecyclerView.Adapter<Student_Adapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public ImageView student_image_download;
        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            student_image_download=(ImageView) v.findViewById(R.id.student_image_download);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Student_Adapter(ArrayList<String>  myDataset, Context myContextt) {
        mDataset = myDataset;
        mContext=myContextt;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset.get(position);
                Log.d("CardView", "CardView Clicked: " + currentValue);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dailog_custom);
                // set the custom dialog components - text, image and button


                dialog.show();
            }
        });
        holder.student_image_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position)+" Clicked_Download");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
