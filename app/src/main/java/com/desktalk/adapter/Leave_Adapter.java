package com.desktalk.adapter;

/**
 * Created by Pavan.Chunchula on 25-10-2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.desktalkapp.R;
import com.desktalk.util.Constants;

import java.util.ArrayList;


public class Leave_Adapter extends RecyclerView.Adapter<Leave_Adapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private Context mContext;
    private String category;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLayoutText;
        public TextView mTextView;
        public ImageView leave_image_edit, leave_image_download, leave_image_delete;

        public MyViewHolder(View v) {
            super(v);

            mLayoutText = (LinearLayout) v.findViewById(R.id.textheading_leave);
            mTextView = (TextView) v.findViewById(R.id.leave_title);
            leave_image_edit = (ImageView) v.findViewById(R.id.leave_image_edit);
            leave_image_download = (ImageView) v.findViewById(R.id.leave_image_download);
            leave_image_delete = (ImageView) v.findViewById(R.id.leave_image_delete);
            if (category.equals("Approved")) {
                leave_image_edit.setVisibility(View.INVISIBLE);

            } else if (category.equals("Rejected")) {
                leave_image_edit.setVisibility(View.INVISIBLE);

            }
            if (Constants.USER_ID == Constants.USER_PARENT) {
                leave_image_download.setVisibility(View.VISIBLE);
                leave_image_delete.setVisibility(View.VISIBLE);
            } else if (Constants.USER_ID == Constants.USER_TEACHER) {
                leave_image_download.setVisibility(View.INVISIBLE);
                leave_image_delete.setVisibility(View.INVISIBLE);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Leave_Adapter(ArrayList<String> myDataset, Context myContextt, String clicked) {
        category = clicked;
        mDataset = myDataset;
        mContext = myContextt;
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
        holder.mTextView.setText(mDataset.get(position));
        holder.mLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset.get(position);
                Log.d("CardView", "CardView Clicked: " + currentValue);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.view_leave);
                dialog.setTitle("Accepted");
//                dialog.setCanceledOnTouchOutside(false);
                Button dismis = (Button) dialog.findViewById(R.id.dismis_view);

                TextView title_for_leave = (TextView) dialog.findViewById(R.id.title_for_leave_status);
                TextView leave_tiltle = (TextView) dialog.findViewById(R.id.leave_tiltle);
                TextView leave_description = (TextView) dialog.findViewById(R.id.leave_description);

                leave_tiltle.setText(String.valueOf(mDataset.get(position)));
                title_for_leave.setText(String.valueOf(category));


                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // set the custom dialog components - text, image and button


                dialog.show();
            }
        });
        holder.leave_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("edit", mDataset.get(position) + " Clicked_Edit");
            }
        });
//        holder.syllabus_image_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("edit", mDataset.get(position) + " Clicked_Download");
//            }
//        });
//        holder.syllabus_image_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("edit", mDataset.get(position) + " Clicked_Delete");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
