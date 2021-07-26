package com.mobiledevelopment.actex.views;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.AccountApiEndpointInterface;
import com.mobiledevelopment.actex.clients.MovieDetailsApi;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.lists.ListResult;
import com.mobiledevelopment.actex.models.movie_details.ProductionCountry;
import com.mobiledevelopment.actex.models.movie_details.cast.Cast;
import com.mobiledevelopment.actex.models.movie_details.cast.CastsList;
import com.mobiledevelopment.actex.models.movie_details.review.Review;
import com.mobiledevelopment.actex.models.movie_details.review.Reviews;
import com.mobiledevelopment.actex.models.request_bodies.AddToListBody;
import com.mobiledevelopment.actex.models.request_bodies.FavouriteMovie;
import com.mobiledevelopment.actex.models.request_bodies.Rate;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MovieFragment extends Fragment {

    private Movie movie;
    private Resources res;
    private TextView casts;
    private TextView reviews;
    private AddToListAdapter addToListAdapter;

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            movie = (Movie) getArguments().getSerializable("movie");
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.R)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        res = getResources();
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        TextView name = view.findViewById(R.id.movie_name);
        name.setText(movie.getTitle());

        RatingBar ratingBar = view.findViewById(R.id.imdb_rating_bar);
        setupRatingBar(ratingBar);

        TextView summary = view.findViewById(R.id.movie_summary);
        summary.setText(movie.getOverview());

        TextView brief_attrs = view.findViewById(R.id.country_year_length_text_view);
        String country = "-";
        if (movie.getProductionCountries() != null)
            country = ((ProductionCountry) movie.getProductionCountries().get(0)).getName();
        String year = movie.getReleaseDate().split("-")[0];
        String length = "- min";
        if (movie.getRuntime() != null)
            length = movie.getRuntime().toString() + " min";
        brief_attrs.setText(String.format("%s, %s, %s", country, year, length));

        TextView ratings = view.findViewById(R.id.rating_value);
        ratings.setText(String.format("%s/10", movie.getVoteAverage()));

        ImageView fav_empty = view.findViewById(R.id.fav_empty);
        ImageView fav_full = view.findViewById(R.id.fav_full);
        setupFavButtons(fav_empty, fav_full);

        setupAddToList(view, inflater, container);

        ImageView imageView = view.findViewById(R.id.movie_img);
        Picasso.get().load(res.getString(R.string.img_base_url) + movie.getPosterPath()).into(imageView);
        imageView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.shimmer_movie_img).setVisibility(View.INVISIBLE);

        List<TextView> genreViews = List.of((TextView) view.findViewById(R.id.movie_genre1),
                (TextView) view.findViewById(R.id.movie_genre2),
                (TextView) view.findViewById(R.id.movie_genre3));

        if (movie.getGenres() != null)
            for (int i = 0; i < 3; i += 1) {
                TextView genreTextView = genreViews.get(i);
                genreTextView.setText(movie.getGenres().get(i).getName());
            }

        // TODO set fav icon based on favourite list

        return view;
    }

    private void setupAddToList(View outerView, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.add_to_list_popup, null, false);
        RecyclerView popupListRecyclerView = view.findViewById(R.id.popup_add_to_list);
        addToListAdapter = new AddToListAdapter(new ArrayList<>());
        addToListAdapter.setOnListItemClickedListener(listResult -> {
            addToList(listResult, movie.getId());
        });
        popupListRecyclerView.setAdapter(addToListAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        popupListRecyclerView.setLayoutManager(mLayoutManager);

        ImageView addToList = outerView.findViewById(R.id.add_to_list);
        // TODO
        addToList.setOnClickListener(v -> {
            PopupWindow pw = new PopupWindow(view, 600, 600, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
        getLists();
    }

    public void getLists() {
        Call<com.mobiledevelopment.actex.models.lists.List> myList = RetrofitBuilder.getCreateListApi()
                .getLists(User.getUser().getAccount().getId(), getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), 1);
        myList.enqueue(new Callback<com.mobiledevelopment.actex.models.lists.List>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<com.mobiledevelopment.actex.models.lists.List> call, Response<com.mobiledevelopment.actex.models.lists.List> response) {
                Log.e("list error", response.code() + "");
                if (response.isSuccessful()) {
                    Log.e("list comp", response.code() + "");
                    assert response.body() != null;
                    addToListAdapter.addAll(response.body());
                } else {
                    Log.e("list error", response.code() + "");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<com.mobiledevelopment.actex.models.lists.List> call, Throwable t) {
                Log.e("list error", t.getMessage());
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCustomListBtn(ImageView customListImgBtn) {
        customListImgBtn.setOnClickListener(v -> showAddToListDialog());
    }

    private void showAddToListDialog() {
        AddToListDialogFragment addToListDialogFragment = AddToListDialogFragment.newInstance("Some Title", movie.getId());
        addToListDialogFragment.show(getChildFragmentManager(), "fragment_edit_name");
    }

    private void addToList(final ListResult listResult, int movieId) {
        Call<Object> addToList = RetrofitBuilder.getCreateListApi().addMovieToList(listResult.getId().toString(), User.getApiKey(), User.getUser().getSessionToken().getSessionId(), new AddToListBody(movieId));
        addToList.enqueue(new Callback<Object>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "movie added to list" + listResult.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("list error", response.code() + "");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("list error", t.getMessage());
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

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
        fav_img_full.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Was not removed from favourite! (DEBUG)", Toast.LENGTH_LONG).show();
            fav_img_empty.setVisibility(View.VISIBLE);
            fav_img_full.setVisibility(View.INVISIBLE);
        });
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