package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.request_bodies.AddToListBody;
import com.mobiledevelopment.actex.models.request_bodies.CreateListBody;
import com.mobiledevelopment.actex.models.lists.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreateListApiEndpointInterface {
    @GET("account/{account_id}/lists")
    Call<List> getLists(@Path("account_id") int accountId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Query("page") int page);

    @POST("list")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Object> createList(@Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body CreateListBody name);

    @DELETE("list/{list_id}")
    Call<Object> deleteList(@Path("list_id") String listId, @Query("api_key") String apiKey, @Query("session_id") String sessionId);

    @POST("list/{list_id}/add_item")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Object> addMovieToList(@Path("list_id") String listId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body AddToListBody body);
}
