package com.desktalk;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Pavan.Chunchula on 28-02-2017.
 */

public interface Apis {


    @POST("login.php")
    @Headers({"Content-Type:  application/json"}
    )
    Call<JsonElement> Authenticate(
            @Body LoginRequestModel body);


    @GET
    Call<JsonElement> get(@Url String url,
                          @QueryMap Map<String, String> options);


    @GET("/Holidays")
    @Headers({"Accept:  application/json",
            "Content-Type:  application/json"}
    )
    Call<JsonElement> Holidays();

    @POST
    Call<JsonElement> post(@Url String url,
                           @Body JsonElement body);

}
