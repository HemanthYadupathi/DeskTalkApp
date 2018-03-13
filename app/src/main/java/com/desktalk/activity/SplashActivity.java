package com.desktalk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.util.Apis;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences, mPreferencesInitSetup;
    private SharedPreferences.Editor editor;
    private final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_test);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LaunchInitSetUp();
            }
        }, 1500);
    }

    private void launchNextActivity() {
        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if (sharedpreferences != null) {
            String username = sharedpreferences.getString(Constants.PREFERENCE_KEY_USER_NAME, "");
            String pwd = sharedpreferences.getString(Constants.PREFERENCE_KEY_USER_PWD, "");
            String deviceToken = sharedpreferences.getString(Constants.PREFERENCE_KEY_DEVICE_TOKEN, "");

            if (username != null && (!username.contentEquals("")) && pwd != null && (!pwd.contentEquals(""))
                    && deviceToken != null && (!deviceToken.contentEquals(""))) {
                Log.i(TAG, "USER Logged IN " + username + " " + deviceToken);
                if (Connectivity.isConnected(getApplicationContext())) {
                    showProgress();
                    login(username, pwd);
                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                    clearActivity();
                }
            } else {
                clearActivity();
            }
        } else {
            clearActivity();
        }

    }

    private void LaunchInitSetUp() {
        mPreferencesInitSetup = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_INITIAL_SETUP, Context.MODE_PRIVATE);
        if (mPreferencesInitSetup != null) {
            if (!mPreferencesInitSetup.getBoolean(Constants.PREFERENCE_KEY_FIRST_USER, false)) {
                startActivity(new Intent(SplashActivity.this, FirstLoginActivity.class));
                finish();
            } else {
                launchNextActivity();
            }
        } else {
            startActivity(new Intent(SplashActivity.this, FirstLoginActivity.class));
            finish();
        }

    }

    private void showProgress() {
        final View view = findViewById(R.id.layout_progress);

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        view.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener(null);
    }

    private void login(final String user, final String password) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final String android_id = Settings.Secure.getString(SplashActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Map<String, String> requestBodyMap = new HashMap<>();

        requestBodyMap.put("username", String.valueOf(user));
        requestBodyMap.put("password", String.valueOf(password));
        requestBodyMap.put("devicetoken", android_id);

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.Authenticate(requestBodyMap);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                if (response1.code() == 200) {
                    if (response1.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response1.body().toString());
                            if (String.valueOf(jsonObject.get("status")).contentEquals("success")) {
                                if (String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("4")
                                        || String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("6")
                                        || String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("7")) {

                                    editor = sharedpreferences.edit();
                                    editor.putString(Constants.PREFERENCE_KEY_USERDATA, String.valueOf(jsonObject.getJSONObject("response")));
                                    editor.putString(Constants.PREFERENCE_KEY_TOKEN, String.valueOf(jsonObject.getJSONObject("response").get("token")));
                                    editor.putString(Constants.PREFERENCE_KEY_USER_NAME, user);
                                    editor.putString(Constants.PREFERENCE_KEY_USER_PWD, password);
                                    editor.putString(Constants.PREFERENCE_KEY_DEVICE_TOKEN, android_id);
                                    editor.commit();
                                    editor.apply();

                                    if (String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("4")) {
                                        Constants.USER_ID = Constants.USER_TEACHER;
                                    } else if (String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("6")) {
                                        Constants.USER_ID = Constants.USER_PARENT;
                                    } else if (String.valueOf(jsonObject.getJSONObject("response").get("role")).contentEquals("7")) {
                                        Constants.USER_ID = Constants.USER_ATTENDER;
                                    }
                                    Toast.makeText(SplashActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                                    finish();
                                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    /*if (Constants.USER_ID == Constants.USER_ATTENDER || Constants.USER_ID == Constants.USER_TEACHER) {
                                        Intent intent = new Intent(SplashActivity.this, AttendanceMainActivity.class);
                                        startActivity(intent);
                                    } else if (Constants.USER_ID == Constants.USER_PARENT) {
                                        Intent intent = new Intent(SplashActivity.this, BusTrackMapActivity.class);
                                        startActivity(intent);
                                    }*/
                                } else {
                                    Toast.makeText(SplashActivity.this, "Sorry, App doesn't support for this User", Toast.LENGTH_SHORT).show();
                                    clearActivity();
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                clearActivity();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            clearActivity();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "Error logging, please try again", Toast.LENGTH_SHORT).show();
                        clearActivity();
                    }
                } else if (response1.code() == 400) {
                    Toast.makeText(SplashActivity.this, "Invalid username/password, please try again", Toast.LENGTH_SHORT).show();
                    clearActivity();
                } else if (response1.code() == 402) {
                    Toast.makeText(SplashActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    clearActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                clearActivity();
            }

        });
    }

    private void clearActivity() {
        finish();
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
