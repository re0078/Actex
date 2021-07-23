package com.mobiledevelopment.actex.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static String baseUrl = "https://api.themoviedb.org/3/";

    static Gson gson = new GsonBuilder().setLenient().create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();


    public static AuthApiEndpointInterface getAuthApi() {
        return retrofit.create(AuthApiEndpointInterface.class);
    }

}

