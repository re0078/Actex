package com.mobiledevelopment.actex.clients;

import android.util.Log;

import com.mobiledevelopment.actex.models.TrendListType;
import com.mobiledevelopment.actex.models.lists.MovieList;
import com.mobiledevelopment.actex.views.TrendListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListBuilder {
    private String apiKey;

    public MovieListBuilder(String apiKey) {
        this.apiKey = apiKey;
    }


    public void getMovieList(TrendListType trendListType, TrendListAdapter adapter) {
        Call<MovieList> movieList;
        MovieListsApiEndpointInterface movieListsApi = RetrofitBuilder.getMovieApi();
        switch (trendListType) {
            case UPCOMING:
                movieList = movieListsApi.getUpcomingMovies(apiKey);
                break;
            case TOP_RATED:
                movieList = movieListsApi.getTopRated(apiKey);
                break;
            case MOST_POPULAR:
                movieList = movieListsApi.getPopularMovies(apiKey);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + trendListType);
        }
        setEnqueue(movieList, adapter);
    }

    private void setEnqueue(Call<MovieList> enqueueList, final TrendListAdapter adapter) {
        enqueueList.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        adapter.addAll(response.body().results);
                    }
                } else Log.e("NOt", "" + response.code());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i("failes", t.getMessage());
            }
        });
    }

}
