package com.mobiledevelopment.actex.utils;

import static com.mobiledevelopment.actex.clients.RetrofitBuilder.getListApi;

import android.util.Log;

import com.mobiledevelopment.actex.clients.ListsApiEndpointInterface;
import com.mobiledevelopment.actex.models.request_bodies.BaseDeletionRequest;
import com.mobiledevelopment.actex.models.ListType;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.Playlist;
import com.mobiledevelopment.actex.models.SimpleResponse;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.lists.ListResponse;
import com.mobiledevelopment.actex.models.lists.PlaylistResponse;
import com.mobiledevelopment.actex.models.request_bodies.FavoriteDeletionRequest;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistDeletionRequest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ListApiUtil {
    private final User user;
    private static final ListApiUtil LIST_API_UTIL = new ListApiUtil(User.getInstance());
    private static final String TAG = "LIST_API_UTIL";
    private static final String CONTENT_TYPE = "application/json;charset=utf-8";


    public static ListApiUtil getInstance() {
        return LIST_API_UTIL;
    }

    public void deletePlaylist(String listId, String apiKey, CompletableFuture<Boolean> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<SimpleResponse> getListsCall = listsApi.deletePlaylist(listId, apiKey,
                user.getSessionToken().getSessionId());
        getListsCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                future.complete(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                future.complete(false);
            }
        });
    }

    public void getPlaylists(String apiKey, CompletableFuture<ListResponse<Playlist>> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<ListResponse<Playlist>> getListsCall = listsApi.getPlaylists(user.getAccount().getId(), apiKey,
                user.getSessionToken().getSessionId());
        getListsCall.enqueue(new Callback<ListResponse<Playlist>>() {
            @Override
            public void onResponse(Call<ListResponse<Playlist>> call, Response<ListResponse<Playlist>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Received lists");
                    assert response.body() != null;
                    List<Playlist> playlists = response.body().getResults();
                    AtomicInteger checkedPlaylists = new AtomicInteger(0);
                    playlists.forEach(playlist -> {
                        CompletableFuture<PlaylistResponse> playlistFuture = new CompletableFuture<>();
                        fetchPlaylistMovies(playlist.getId(), apiKey, playlistFuture);
                        playlistFuture.whenComplete((pResp, t) -> {
                            playlist.setMovies(pResp.getItems());
                            if (checkedPlaylists.incrementAndGet() == playlists.size()) {
                                future.complete(response.body());
                            }
                        });
                    });
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Playlist>> call, Throwable t) {
                future.complete(null);
            }
        });
    }

    public void getFavoriteMovies(String apiKey, CompletableFuture<ListResponse<Movie>> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<ListResponse<Movie>> getListsCall = listsApi.getFavoriteMovies(user.getAccount().getId(), apiKey,
                user.getSessionToken().getSessionId());
        getListsCall.enqueue(new Callback<ListResponse<Movie>>() {
            @Override
            public void onResponse(Call<ListResponse<Movie>> call, Response<ListResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Received favorites");
                    Objects.requireNonNull(response.body()).getResults().forEach(movie ->
                            movie.setListType(ListType.FAVORITE));
                    future.complete(response.body());
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Movie>> call, Throwable t) {
                future.complete(null);
            }
        });
    }

    public void getWatchlistMovies(String apiKey, CompletableFuture<ListResponse<Movie>> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<ListResponse<Movie>> getListsCall = listsApi.getWatchlistMovies(user.getAccount().getId(), apiKey,
                user.getSessionToken().getSessionId());
        getListsCall.enqueue(new Callback<ListResponse<Movie>>() {
            @Override
            public void onResponse(Call<ListResponse<Movie>> call, Response<ListResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Received watchlist");
                    Objects.requireNonNull(response.body()).getResults().forEach(movie ->
                            movie.setListType(ListType.WATCHLIST));
                    future.complete(response.body());
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Movie>> call, Throwable t) {
                future.complete(null);
            }
        });
    }

    public void fetchPlaylistMovies(int listId, String apiKey, CompletableFuture<PlaylistResponse> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<PlaylistResponse> getListsCall = listsApi.getPlaylistMovies(listId, apiKey, "en");
        getListsCall.enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Received playlist " + listId);
                    Objects.requireNonNull(response.body()).getItems().forEach(movie ->
                    {
                        movie.setListType(ListType.CUSTOM);
                        movie.setListId(Integer.valueOf(response.body().getId()));
                    });
                    future.complete(response.body());
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                future.complete(null);
            }
        });
    }

    public void deleteListItem(int movieId, int listId, String apiKey, CompletableFuture<Boolean> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<SimpleResponse> getListsCall = listsApi.deleteListItem(listId, apiKey,
                user.getSessionToken().getSessionId(), new BaseDeletionRequest(movieId));
        getListsCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                future.complete(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                future.complete(false);
            }
        });
    }

    public void deleteFavoriteItem(int movieId, String apiKey, CompletableFuture<Boolean> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<SimpleResponse> deletionCall = listsApi.deleteFavoriteMovies(user.getAccount().getId(),
                apiKey, user.getSessionToken().getSessionId(),
                new FavoriteDeletionRequest(movieId, "movie", false));
        deletionCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                future.complete(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                future.complete(false);
            }
        });
    }

    public void deleteWatchlistItem(int movieId, String apiKey, CompletableFuture<Boolean> future) {
        ListsApiEndpointInterface listsApi = getListApi();
        Call<SimpleResponse> deletionCall = listsApi.deleteWatchlistMovies(user.getAccount().getId(),
                apiKey, user.getSessionToken().getSessionId(),
                new WatchlistDeletionRequest(movieId, "movie", false));
        deletionCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                future.complete(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                future.complete(false);
            }
        });
    }


}
