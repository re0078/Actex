package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.lists.MovieList;
import com.mobiledevelopment.actex.models.lists.PlaylistsResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListsApiEndpointInterface {
    @GET("account/{account_id}/lists")
    Call<PlaylistsResult> getPlaylists(@Path("account_id") int accountId,
                                       @Query("api_key") String apiKey,
                                       @Query("session_id") String sessionId);
}
