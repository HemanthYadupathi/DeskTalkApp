package com.desktalk.util;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */
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


public class Teacher_Adapter extends RecyclerView.Adapter<Teacher_Adapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private Context  mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public ImageView syllabus_image_edit,syllabus_image_download,syllabus_image_delete;
        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            syllabus_image_edit=(ImageView) v.findViewById(R.id.syllabus_image_edit);
            syllabus_image_download=(ImageView) v.findViewById(R.id.syllabus_image_download);
            syllabus_image_delete=(ImageView) v.findViewById(R.id.syllabus_image_delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Teacher_Adapter(ArrayList<String>  myDataset, Context myContextt) {
        mDataset = myDataset;
        mContext=myContextt;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_card, parent, false);
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
                dialog.setTitle("Title...");
                // set the custom dialog components - text, image and button


                dialog.show();
            }
        });
        holder.syllabus_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position)+" Clicked_Edit");
            }
        });
        holder.syllabus_image_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position)+" Clicked_Download");
            }
        });
        holder.syllabus_image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position)+" Clicked_Delete");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
