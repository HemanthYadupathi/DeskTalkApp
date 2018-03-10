package com.desktalk.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import com.desktalk.adapter.ViewPagerAdapter;
import com.desktalk.util.Constants;

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

    ImageView choosen_image;
    TextView no_file_choosen;

    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = getContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_academics, container, false);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((DashboardActivity) getActivity()).setToolbar(mToolbar, "Academics");

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fragment_academics_fab);
        AppCompatSpinner spinner_class = (AppCompatSpinner) rootView.findViewById(R.id.spinner_class);
        fab.bringToFront();

        if (Constants.USER_ID == Constants.USER_TEACHER) {
            spinner_class.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        } else {
            spinner_class.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialodView = new AlertDialog.Builder(getActivity());
                View dialog = getActivity().getLayoutInflater().inflate(R.layout.add_academics_info, null);
                dialodView.setView(dialog);
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

                final AlertDialog alertDialog = dialodView.create();
                alertDialog.show();
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
