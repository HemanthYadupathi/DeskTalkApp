package com.desktalk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.desktalkapp.R;
import com.desktalk.activity.DashboardActivity;
import com.desktalk.util.Constants;
import com.desktalk.util.ViewPagerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcademicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcademicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView choosen_image;
    TextView no_file_choosen;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AcademicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcademicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcademicsFragment newInstance(String param1, String param2) {
        Log.d(param1, param2);
        AcademicsFragment fragment = new AcademicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_academics, container, false);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Academics");

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fragment_academics_fab);
        AppCompatSpinner spinner_class = (AppCompatSpinner) rootView.findViewById(R.id.spinner_class);
        fab.bringToFront();
        if (Constants.USER_ID == 0) {
            spinner_class.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        } else if (Constants.USER_ID == 2) {
            spinner_class.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_academics_info);
                dialog.setTitle("Title...");
                Button add_images = (Button) dialog.findViewById(R.id.add_images);
                choosen_image = (ImageView) dialog.findViewById(R.id.choosen_image);
                no_file_choosen = (TextView) dialog.findViewById(R.id.no_file_choosen);
                add_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        photoPickerIntent.setType("file/*");
                        startActivityForResult(photoPickerIntent, 1);
                    }
                });
                // set the custom dialog components - text, image and button
                /*ImageView dialogButton = (ImageView) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/

                dialog.show();
            }
        });

        // Add Fragments to adapter one by one
        Bundle Syllabusbundle = new Bundle();
        Syllabusbundle.putString("Value", "Syllabus");
        SyllabusFragment syllabusFragment = new SyllabusFragment();
        syllabusFragment.setArguments(Syllabusbundle);

        Bundle Papersbundle = new Bundle();
        Papersbundle.putString("Value", "Previous Question Papers");
        SyllabusFragment PapersFragment = new SyllabusFragment();
        PapersFragment.setArguments(Papersbundle);

        Bundle Notesbundle = new Bundle();
        Notesbundle.putString("Value", "Important Notes");
        SyllabusFragment NotesFragment = new SyllabusFragment();
        NotesFragment.setArguments(Notesbundle);


        adapter.addFragment(syllabusFragment, "Syllabus");
        adapter.addFragment(PapersFragment, "Previous Question Papers");
        adapter.addFragment(NotesFragment, "Important Notes");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            try {
                no_file_choosen.setVisibility(View.GONE);
                choosen_image.setVisibility(View.VISIBLE);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                choosen_image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
