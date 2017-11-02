package com.desktalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mLayoutHobbies, mLayoutSkills;
    private LinearLayout mLayoutHobbyList, mLayoutSkillList;
    private AlertDialog alertDialog;
    /*private TextView hobby;
    private View inflatedLayout;*/
    private int hobbyIndex = 0, skillIndex = 11;
    private ImageView mImageDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Explode transition = new Explode();
        transition.excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().setEnterTransition(transition);
        getWindow().setReturnTransition(transition);

        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initialize();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Choose photo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.title_activity_edit_profile));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    private void initialize() {
        mLayoutHobbies = (RelativeLayout) findViewById(R.id.layoutHobbies);
        mLayoutSkills = (RelativeLayout) findViewById(R.id.layoutSkills);
        mLayoutHobbyList = (LinearLayout) findViewById(R.id.layoutHobbiesList);
        mLayoutSkillList = (LinearLayout) findViewById(R.id.layoutSkillsList);

        mLayoutHobbies.setOnClickListener(this);
        mLayoutSkills.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(EditProfileActivity.this);
        View dialodView = getLayoutInflater().inflate(R.layout.dialog_add, null);
        addDialog.setView(dialodView);
        alertDialog = addDialog.create();

        final EditText mEditTextAdd = (EditText) dialodView.findViewById(R.id.edittext_add);
        Button mButtonAdd = (Button) dialodView.findViewById(R.id.btn_add);

        /*LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
        inflatedLayout = inflater.inflate(R.layout.layout_text_clear, null, false);
        mImageDelete = (ImageView) inflatedLayout.findViewById(R.id.imageClear);


        mImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileActivity.this, "delete " + inflatedLayout.getId(), Toast.LENGTH_SHORT).show();
            }
        });*/
        switch (view.getId()) {
            case R.id.layoutHobbies:
                mEditTextAdd.setHint("Add your hobby");
                alertDialog.show();
                mButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(TextUtils.isEmpty(mEditTextAdd.getText().toString()))) {

                            LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
                            final View inflatedLayout = inflater.inflate(R.layout.layout_text_clear, null, false);
                            mImageDelete = (ImageView) inflatedLayout.findViewById(R.id.imageClear);
                            mImageDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((LinearLayout) inflatedLayout.getParent()).removeView(inflatedLayout);
                                    Toast.makeText(EditProfileActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            inflatedLayout.setId(hobbyIndex);
                            TextView hobby = (TextView) inflatedLayout.findViewById(R.id.textAdd);
                            mImageDelete.setId(hobbyIndex);
                            hobbyIndex++;
                            hobby.setText(mEditTextAdd.getText().toString());
                            mLayoutHobbyList.addView(inflatedLayout);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Please add your hobby", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.layoutSkills:
                mEditTextAdd.setHint("Add your skill");
                alertDialog.show();
                mButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(TextUtils.isEmpty(mEditTextAdd.getText().toString()))) {
                            LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
                            final View inflatedLayout = inflater.inflate(R.layout.layout_text_clear, null, false);
                            mImageDelete = (ImageView) inflatedLayout.findViewById(R.id.imageClear);
                            mImageDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((LinearLayout) inflatedLayout.getParent()).removeView(inflatedLayout);
                                    Toast.makeText(EditProfileActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            inflatedLayout.setId(skillIndex);
                            TextView hobby = (TextView) inflatedLayout.findViewById(R.id.textAdd);
                            mImageDelete.setId(skillIndex);
                            skillIndex++;
                            hobby.setText(mEditTextAdd.getText().toString());
                            mLayoutSkillList.addView(inflatedLayout);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Please add your skill", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}

