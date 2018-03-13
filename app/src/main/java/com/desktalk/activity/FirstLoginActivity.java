package com.desktalk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.util.Constants;

public class FirstLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonNumber, mButtonOTP, mButtonDone;
    private CardView mCardViewNumber, mCardViewOTP, mCardViewDone;
    private CheckBox mCheckBoxTandC;
    private SharedPreferences mPreferencesInitSetup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        initializeUI();
        mCheckBoxTandC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mButtonNumber.setBackgroundColor(getResources().getColor(R.color.colorMainApp));
                } else {
                    mButtonNumber.setBackgroundColor(getResources().getColor(R.color.colorGreyNonEdit));
                }
            }
        });
        toggleUI(true, false, false);
    }

    private void initializeUI() {
        mButtonNumber = (Button) findViewById(R.id.button_go);
        mButtonOTP = (Button) findViewById(R.id.button_verify);
        mButtonDone = (Button) findViewById(R.id.button_pwdDone);

        mButtonNumber.setOnClickListener(this);
        mButtonOTP.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);

        mCardViewNumber = (CardView) findViewById(R.id.cardview_number);
        mCardViewOTP = (CardView) findViewById(R.id.cardview_otp);
        mCardViewDone = (CardView) findViewById(R.id.cardview_pwd);
        mCheckBoxTandC = (CheckBox) findViewById(R.id.checkBoxTC);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go:
                if (mCheckBoxTandC.isChecked())
                    toggleUI(false, true, false);
                else
                    Toast.makeText(FirstLoginActivity.this, "Please check T&C", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_verify:
                toggleUI(false, false, true);
                break;
            case R.id.button_pwdDone:
                mPreferencesInitSetup = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_INITIAL_SETUP, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferencesInitSetup.edit();
                editor.putBoolean(Constants.PREFERENCE_KEY_FIRST_USER, true);
                editor.commit();
                editor.apply();
                finish();
                Intent intent = new Intent(FirstLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void toggleUI(boolean isShowNumberCard, boolean isShowOtpCard, boolean isShowFinalCard) {
        if (isShowNumberCard) {
            mCardViewNumber.setVisibility(View.VISIBLE);
        } else {
            mCardViewNumber.setVisibility(View.GONE);
        }
        if (isShowOtpCard) {
            mCardViewOTP.setVisibility(View.VISIBLE);
        } else {
            mCardViewOTP.setVisibility(View.GONE);
        }
        if (isShowFinalCard) {
            mCardViewDone.setVisibility(View.VISIBLE);
        } else {
            mCardViewDone.setVisibility(View.GONE);
        }
    }
}
