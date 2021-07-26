package com.mobiledevelopment.actex.views;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.mobiledevelopment.actex.models.movie_details.ProductionCountry;
import com.mobiledevelopment.actex.models.movie_details.cast.Cast;
import com.mobiledevelopment.actex.models.movie_details.cast.CastsList;
import com.mobiledevelopment.actex.models.movie_details.review.Review;
import com.mobiledevelopment.actex.models.movie_details.review.Reviews;
import com.mobiledevelopment.actex.models.request_bodies.FavouriteMovie;
import com.mobiledevelopment.actex.models.request_bodies.Rate;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistMovie;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MovieFragment extends Fragment {

    private Movie movie;
    public Resources res;
    private final String imageBaseUrl = "https://image.tmdb.org/t/p/w342";
    private ImageView fav_empty;
    private ImageView fav_full;


    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
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

        res = getResources();
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        TextView name = view.findViewById(R.id.movie_name);
        name.setText(movie.getTitle());

        RatingBar ratingBar = view.findViewById(R.id.item_rating_bar);
        setupRatingBar(ratingBar);

        TextView summary = view.findViewById(R.id.movie_summary);
        summary.setText(movie.getOverview());


//        TextView pop = view.findViewById(R.id.popularityText);
//        pop.setText(movie.getPopularity().intValue() + "");

        TextView brief_attrs = view.findViewById(R.id.country_year_length_text_view);
        String country = "-";
        if (movie.getProductionCountries() != null)
            country = ((ProductionCountry) movie.getProductionCountries().get(0)).getName();
        String year = movie.getReleaseDate();
        String length = "- min";
        if (movie.getRuntime() != null)
            length = movie.getRuntime().toString() + " min";
        brief_attrs.setText(String.format("%s, %s, %s", country, year, length));

        TextView ratings = view.findViewById(R.id.rating_value);
        ratings.setText(String.format("%s/10", movie.getVoteAverage()));

        fav_empty = view.findViewById(R.id.fav_empty);
        fav_full = view.findViewById(R.id.fav_full);
        setupFavButtons(fav_empty, fav_full);

        ImageView addToList = view.findViewById(R.id.add_to_list);
        setUpCustomListBtn(addToList);

        ImageView imageView = view.findViewById(R.id.movie_img);
        Picasso.get().load(imageBaseUrl + movie.getPosterPath()).into(imageView);

//        casts = view.findViewById(R.id.actors);
//        reviews = view.findViewById(R.id.reviewText);
//        setMovieCastsAndReviews();
//        Log.e("hello", "s");

        return view;
    }

    private void setUpCustomListBtn(ImageView customListImgBtn) {
        customListImgBtn.setOnClickListener(v -> showAddToListDialog());
    }

    private void showAddToListDialog() {
        AddToListDialogFragment addToListDialogFragment = AddToListDialogFragment.newInstance("Some Title", movie.getId());
        addToListDialogFragment.show(getChildFragmentManager(), "fragment_edit_name");
    }

    //todo: clean this section
    private void setupWatchListBtn(final ImageView watchListImgBtn) {
        watchListImgBtn.setOnClickListener(v -> {
            AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
            Call<Object> addToFav = api.addToWatchList(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new WatchlistMovie("movie", movie.getId(), true));
            addToFav.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed rating", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "added to watch list!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupFavButtons(final ImageView fav_img_empty, final ImageView fav_img_full) {
        fav_img_empty.setOnClickListener(v -> {
            AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
            Call<Object> addToFav = api.addToFav(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new FavouriteMovie("movie", movie.getId(), true));
            addToFav.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed rating", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getContext(), "added to favourites!", Toast.LENGTH_LONG).show();
                    fav_img_empty.setVisibility(View.INVISIBLE);
                    fav_img_full.setVisibility(View.VISIBLE);
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                }
            });
        });

        // TODO remove from fav
    }

    private void setupRatingBar(RatingBar ratingBar) {
        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            MovieDetailsApi api = RetrofitBuilder.getMovieDetailApi();
            Call<Object> rate = api.rateMovie(movie.getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new Rate((int) v * 2));
            rate.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed rating", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getContext(), "rated", Toast.LENGTH_LONG).show();
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                }
            });

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