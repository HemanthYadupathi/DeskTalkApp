package com.desktalk.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.Apis;
import com.desktalk.Connectivity;
import com.desktalk.LoginRequestModel;
import com.desktalk.util.Constants;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_ACCESS_NETWORK_STATE = 0;

    String[] permissionArray;
    ArrayList<String> stringArrayList;
    int permissionPos = 0;
    private static final int INITIAL_PERMISSION_REQUESTCODE = 0;
    private SharedPreferences sharedpreferences;
    Editor editor;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mTextViewForgotPwd;
    private int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mTextViewForgotPwd = (TextView) findViewById(R.id.textForgotPwd);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();
//        runTimepermission();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (id == EditorInfo.IME_ACTION_DONE)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;

                if (Connectivity.isConnected(getApplicationContext())) {
                    if (!TextUtils.isEmpty(mEmailView.getText().toString()) && !TextUtils.isEmpty(mPasswordView.getText().toString())) {
                        login(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    } else {
                        if (TextUtils.isEmpty(mEmailView.getText().toString())) {
                            mEmailView.setError(getString(R.string.error_field_required));
                            focusView = mEmailView;
                            cancel = true;
                        } else if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                            mPasswordView.setError(getString(R.string.error_field_required));
                            focusView = mPasswordView;
                            cancel = true;
                        }
                        if (cancel) {
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                        }
                    }
                } else {

                }
//                attemptLogin();
            }
        });

        mTextViewForgotPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder pwdDialog = new AlertDialog.Builder(LoginActivity.this);
                View dialodView = getLayoutInflater().inflate(R.layout.dialog_forgotpwd, null);
                pwdDialog.setView(dialodView);
                pwdDialog.setCancelable(false);
                final AlertDialog alertDialog = pwdDialog.create();
                alertDialog.show();
                EditText mEditTextSchoolName = (EditText) dialodView.findViewById(R.id.schoolName);
                EditText mEditTextUserName = (EditText) dialodView.findViewById(R.id.userName);
                Button mButtonContinue = (Button) dialodView.findViewById(R.id.btn_send);
                Button mButtonCancel = (Button) dialodView.findViewById(R.id.btn_cancel);

                mButtonContinue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, "Button continue", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                mButtonCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, "Button cancel", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

            }
        });

    }

    private void login(String user, String password) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.desktalk.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        LoginRequestModel loginRequestModel = new LoginRequestModel();

        loginRequestModel.setUsername(user);
        loginRequestModel.setPassword(password);

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.Authenticate(loginRequestModel);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                if (response1.body() != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(response1.body().toString());
                        if (String.valueOf(jsonObject.get("result")).contentEquals("Success")) {
                            if (!String.valueOf(jsonObject.getJSONObject("userdata").get("role_name")).contentEquals("Admin")) {

                                editor = sharedpreferences.edit();
                                editor.putString("Log_User_Deatils", response1.body().toString());
                                editor.putString("role_name", String.valueOf(jsonObject.getJSONObject("userdata").get("role_name")));
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Admin Not yet Implemented", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }

        });
    }

    /*private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
*/

    /**
     * Callback received when a permissions request has been completed.
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (requestCode == REQUEST_ACCESS_NETWORK_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if (mEmail.contentEquals("demo") && mPassword.contentEquals("demo")) {
//                userID = 0;
                Constants.USER_ID = 0;
                return true;
            } else if (mEmail.contentEquals("pdemo") && mPassword.contentEquals("demo")) {
                userID = 1;
                Constants.USER_ID = 1;
                return true;
            } else if (mEmail.contentEquals("sdemo") && mPassword.contentEquals("demo")) {
                userID = 2;
                Constants.USER_ID = 2;
                return true;
            }


            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
  /*  private boolean runTimepermission() {
        *//*{}*//*
        stringArrayList = new ArrayList<String>();
        permissionArray = new String[]{};
        boolean isPermissionRequired = false;

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.INTERNET)) {

                }
            }
            stringArrayList.add(Manifest.permission.INTERNET);
            permissionPos = permissionPos + 1;
            isPermissionRequired = true;
        }
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                }
            }
            stringArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionPos = permissionPos + 1;
            isPermissionRequired = true;
        }
       *//* if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) && ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }

            stringArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionPos = permissionPos + 1;
            isPermissionRequired = true;
        }
*//*

        String[] permissionArray = stringArrayList.toArray(new String[permissionPos]);
        if (isPermissionRequired) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionArray,
                        INITIAL_PERMISSION_REQUESTCODE);
            }
        }
        return isPermissionRequired;
    }*/

}

