package com.mobiledevelopment.actex.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static final String API_VER_3 = "https://api.themoviedb.org/3/";

    static Gson gson = new GsonBuilder().setLenient().create();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(API_VER_3)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();


    public static AuthApiEndpointInterface getAuthApi() {
        return RETROFIT.create(AuthApiEndpointInterface.class);
    }

    public static MovieListsApiEndpointInterface getMovieApi() {
        return RETROFIT.create(MovieListsApiEndpointInterface.class);
    }

    public static MovieDetailsApi getMovieDetailApi() {
        return RETROFIT.create(MovieDetailsApi.class);
    }

    public static AccountApiEndpointInterface getAccountApi() {
        return RETROFIT.create(AccountApiEndpointInterface.class);
    }

    public static CreateListApiEndpointInterface getCreateListApi() {
        return RETROFIT.create(CreateListApiEndpointInterface.class);
    }

    public static ListsApiEndpointInterface getListApi(){
        return RETROFIT.create(ListsApiEndpointInterface.class);
    }
}

