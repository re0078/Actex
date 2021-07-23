package com.mobiledevelopment.actex.views;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.AccountApiEndpointInterface;
import com.mobiledevelopment.actex.clients.MovieDetailsApi;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.movie_details.cast.Cast;
import com.mobiledevelopment.actex.models.movie_details.cast.CastsList;
import com.mobiledevelopment.actex.models.movie_details.review.Review;
import com.mobiledevelopment.actex.models.movie_details.review.Reviews;
import com.mobiledevelopment.actex.models.request_bodies.FavouriteMovie;
import com.mobiledevelopment.actex.models.request_bodies.Rate;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistMovie;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private Movie movie;
    public Resources res;
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w342";
    public MovieFragment() {

    }

    public static com.mobiledevelopment.actex.views.MovieFragment newInstance(Movie movie) {
        com.mobiledevelopment.actex.views.MovieFragment fragment = new com.mobiledevelopment.actex.views.MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    TextView casts;
    TextView reviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        res = getResources();
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        return view;
    }

    private void setUpCustomeListBtn(ImageButton customeListImgBtn) {
        customeListImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToListDialog();
            }
        });
    }
    private void showAddToListDialog() {
        AddToListDialogFragment addToListDialogFragment = AddToListDialogFragment.newInstance("Some Title",movie.getId());
        addToListDialogFragment.show(getChildFragmentManager(), "fragment_edit_name");
    }

    //todo: clean this section
    private void setupWatchListBtn(final ImageButton watchListImgBtn) {
        watchListImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
                Call<Object> addToFav = api.addToWatchList(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new WatchlistMovie("movie", movie.getId(), true));
                addToFav.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                        }
                        //watchListImgBtn.setImageDrawable(res.getDrawable(R.drawable.ic_baseline_bookmark_24));
                        Toast.makeText(getContext(), "added to watch list!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUpFavBtn(final ImageButton favImgBtn) {
        favImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
                Call<Object> addToFav = api.addToFav(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new FavouriteMovie("movie", movie.getId(), true));
                addToFav.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                        }
                        //favImgBtn.setImageDrawable(res.getDrawable(R.drawable.ic_baseline_favorite_24));
                        Toast.makeText(getContext(), "added to favourites!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setupRatingBar(RatingBar ratingBar) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                MovieDetailsApi api = RetrofitBuilder.getMovieDetailApi();
                Call<Object> rate = api.rateMovie(movie.getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new Rate((int) v * 2));
                rate.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getContext(), "rated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void setMovieCastsAndReviews() {
        MovieDetailsApi movieDetailsApi = RetrofitBuilder.getMovieDetailApi();
        Call<CastsList> castsListCall = movieDetailsApi.getCastsList(movie.getId(), res.getString(R.string.api_key));
        Call<Reviews> reviewsCall = movieDetailsApi.getReviews(movie.getId(), res.getString(R.string.api_key));
        castsListCall.enqueue(new Callback<CastsList>() {
            @Override
            public void onResponse(Call<CastsList> call, Response<CastsList> response) {
                if (response.isSuccessful()) {
                    CastsList castsList = response.body();
                    if (castsList != null) {
                        setCasts(castsList.getCast().
                                subList(0, castsList.getCast().size() < 8 ? castsList.getCast().size() : 7));
                        Log.e("casts, get", String.valueOf(response.message()));
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<CastsList> call, Throwable t) {

            }
        });
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                if (reviews != null) {
                    setReviews(reviews.getReviews());
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    private void setCasts(List<Cast> casts) {
        StringBuilder builder = new StringBuilder();
        for (Cast cast : casts) {
            builder = builder.append(cast.getName()).append(" as : ").append(cast.getCharacter()).append("\n\n");
        }
        this.casts.setText(builder.toString());
        Log.e("setCasts", builder.toString());

    }

    private void setReviews(List<Review> reviews) {
        StringBuilder builder = new StringBuilder();
        for (Review review : reviews) {
            builder = builder.append(review.getAuthor()).append(" : ").append(review.getContent()).append("\n\n");
        }
        this.reviews.setText(builder.toString());
    }

}