package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.request_bodies.BaseDeletionRequest;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.SimpleResponse;
import com.mobiledevelopment.actex.models.lists.ListResponse;
import com.mobiledevelopment.actex.models.Playlist;
import com.mobiledevelopment.actex.models.lists.PlaylistResponse;
import com.mobiledevelopment.actex.models.request_bodies.FavoriteDeletionRequest;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistDeletionRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListsApiEndpointInterface {

    @GET("account/{account_id}/lists")
    Call<ListResponse<Playlist>> getPlaylists(@Path("account_id") int accountId,
                                              @Query("api_key") String apiKey,
                                              @Query("session_id") String sessionId);

    @GET("account/{account_id}/favorite/movies")
    Call<ListResponse<Movie>> getFavoriteMovies(@Path("account_id") int accountId,
                                                @Query("api_key") String apiKey,
                                                @Query("session_id") String sessionId);

    @GET("account/{account_id}/watchlist/movies")
    Call<ListResponse<Movie>> getWatchlistMovies(@Path("account_id") int accountId,
                                                 @Query("api_key") String apiKey,
                                                 @Query("session_id") String sessionId);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("account/{account_id}/favorite")
    Call<SimpleResponse> deleteFavoriteMovies(@Path("account_id") int accountId,
                                                   @Query("api_key") String apiKey,
                                                   @Query("session_id") String sessionId,
                                                   @Body FavoriteDeletionRequest request);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("account/{account_id}/watchlist")
    Call<SimpleResponse> deleteWatchlistMovies(@Path("account_id") int accountId,
                                                    @Query("api_key") String apiKey,
                                                    @Query("session_id") String sessionId,
                                                    @Body WatchlistDeletionRequest request);

    @GET("list/{list_id}")
    Call<PlaylistResponse> getPlaylistMovies(@Path("list_id") int listId, @Query("api_key") String apiKey, @Query("language") String lang);

    @DELETE("list/{list_id}")
    Call<SimpleResponse> deletePlaylist(@Path("list_id") String listId,
                                        @Query("api_key") String apiKey,
                                        @Query("session_id") String sessionId);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("list/{list_id}/remove_item")
    Call<SimpleResponse> deleteListItem(@Path("list_id") int listId,
                                        @Query("api_key") String apiKey,
                                        @Query("session_id") String sessionId,
                                        @Body BaseDeletionRequest request);
}
