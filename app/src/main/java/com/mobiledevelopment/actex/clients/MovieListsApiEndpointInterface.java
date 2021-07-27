package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.lists.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieListsApiEndpointInterface {

    @GET("movie/upcoming")
    Call<MovieList> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getTopRated(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieList> getSearchResults(@Query("api_key") String apikey, @Query("query") String query);
}
