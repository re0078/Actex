package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.account.Account;
import com.mobiledevelopment.actex.models.lists.MovieList;
import com.mobiledevelopment.actex.models.request_bodies.FavouriteMovie;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistMovie;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountApiEndpointInterface {

    @GET("account/{account_id}/favorite/movies")
    Call<MovieList> getFavouriteMovies(@Path("account_id") int accountId, @Query("api_key") String apikey, @Query("session_id") String sessionId);

    @GET("account")
    Call<Account> getAccountDetail(@Query("api_key") String apikey, @Query("session_id") String sessionId);

    @GET("account/{account_id}/watchlist/movies")
    Call<MovieList> getWatchListMovies(@Path("account_id") int accountId, @Query("api_key") String apikey, @Query("session_id") String sessionId);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("account/{account_id}/favorite")
    Call<Object> addToFav(@Path("account_id") int accountId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body FavouriteMovie favouriteMovie);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("account/{account_id}/watchlist")
    Call<Object> addToWatchList(@Path("account_id") int accountId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body WatchlistMovie watchlistMovie);

    @GET("list/{list_id}")
    Call<MovieList> getCustomList(@Path("list_id") int listId, @Query("api_key") String apiKey, @Query("language") String lang);
}
