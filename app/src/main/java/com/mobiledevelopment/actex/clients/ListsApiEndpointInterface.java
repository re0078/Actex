package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.SimpleResponse;
import com.mobiledevelopment.actex.models.lists.ListResponse;
import com.mobiledevelopment.actex.models.Playlist;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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

    @DELETE("list/{list_id}")
    Call<SimpleResponse> deletePlaylist(@Path("list_id") String listId,
                                        @Query("api_key") String apiKey,
                                        @Query("session_id") String sessionId);

}
