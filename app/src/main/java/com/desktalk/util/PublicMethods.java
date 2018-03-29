package com.desktalk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.desktalk.Model.ClassDetailsModel;
import com.desktalk.fragment.LeaveFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 3/26/2018.
 */

public class PublicMethods {

    public static void getStudentList(Context context, final String TAG, String token) {

        final SharedPreferences sharedpreferencesStudentList;
        sharedpreferencesStudentList = context.getSharedPreferences(Constants.PREFERENCE_STUDENT_LIST, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferencesStudentList.edit();
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.studentListForParent(token);

        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (String.valueOf(jsonObject.get("status").toString()).contentEquals("success")) {
                                JSONArray array = jsonObject.getJSONArray("response");
                                editor.putString(Constants.PREFERENCE_KEY_STUDENTLIST_JSON, String.valueOf(array));
                                //TODO:Add to DB
                                editor.commit();
                                editor.apply();
                                Log.d(TAG, "getStudentList " + String.valueOf(array));
                            } else {
                                Log.d(TAG, "getStudentList Something went wrong" + response.body().getAsString());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Log.e(TAG, "Session expired, please login again");

                } else {
                    Log.e(TAG, "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public static void applyForLeave(final Context context, final String TAG, final String token, HashMap<String, String> data) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.applyLeave(token, data);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Log.d(TAG, "Leave response " + response.body().toString());
                            LeaveFragment.getLeavesByClass(token, null, true);
                            Toast.makeText(context, "Leave applied successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Session expired, please login again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public static void updateLeave(final Context context, final String TAG, final String token, HashMap<String, String> data) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.updateLeave(token, data);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Log.d(TAG, "Leave response " + response.body().toString());
                            LeaveFragment.getLeavesByClass(token, null, true);
                            Toast.makeText(context, "Leave updated successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Session expired, please login again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public static void deleteLeave(final Context context, final String TAG, final String token, final String leaveId) {

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.deleteLeaveById(token, leaveId);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Log.d(TAG, "Leave response " + response.body().toString());
                            LeaveFragment.getLeavesByClass(token, null, true);
                            Toast.makeText(context, "Leave deleted successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Session expired, please login again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong, please login again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

}
