package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.request_bodies.Rate;
import com.mobiledevelopment.actex.models.movie_details.cast.CastsList;
import com.mobiledevelopment.actex.models.movie_details.review.Reviews;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDetailsApi {
    @GET("movie/{movie_id}/credits")
    Call<CastsList> getCastsList(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("movie/{movie_id}/rating")
    Call<Object> rateMovie(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body Rate rate);
}
