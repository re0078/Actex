package com.mobiledevelopment.actex.views;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.mobiledevelopment.actex.models.request_bodies.CreateListBody;
import com.mobiledevelopment.actex.models.request_bodies.FavouriteMovie;
import com.mobiledevelopment.actex.models.request_bodies.Rate;
import com.mobiledevelopment.actex.models.request_bodies.WatchlistMovie;
import com.mobiledevelopment.actex.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MovieFragment extends Fragment {

    private Movie movie;
    private Resources res;
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

        TextView casts = view.findViewById(R.id.movie_casts);
        setupCasts(casts);

        TextView reviews = view.findViewById(R.id.movie_reviews);
        setupReviews(reviews);

        TextView brief_attrs = view.findViewById(R.id.country_year_length_text_view);
        String country = "-";
        if (movie.getProductionCountries() != null)
            country = movie.getProductionCountries().get(0).getName();
        String year = movie.getReleaseDate().split("-")[0];
        String length = "- min";
        if (movie.getRuntime() != null)
            length = movie.getRuntime().toString() + " min";
        brief_attrs.setText(String.format("%s, %s, %s", country, year, length));

        TextView ratings = view.findViewById(R.id.rating_value);
        ratings.setText(String.format("%s/10", movie.getVoteAverage()));

        ImageView fav_empty = view.findViewById(R.id.fav_empty);
        ImageView fav_full = view.findViewById(R.id.fav_full);
        setupFavouriteButtons(fav_empty, fav_full);

        setupAddToList(view, inflater, container);

        ImageView imageView = view.findViewById(R.id.movie_img);
        Picasso.get().load(res.getString(R.string.img_base_url) + movie.getPosterPath()).into(imageView);
        imageView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.shimmer_movie_img).setVisibility(View.INVISIBLE);

        List<TextView> genreViews = List.of(view.findViewById(R.id.movie_genre1),
                view.findViewById(R.id.movie_genre2),
                view.findViewById(R.id.movie_genre3));

        if (movie.getGenres() != null)
            for (int i = 0; i < 3; i += 1) {
                TextView genreTextView = genreViews.get(i);
                genreTextView.setText(movie.getGenres().get(i).getName());
            }

        // TODO set fav icon based on favourite list

        ImageView shareIcon = view.findViewById(R.id.share_movie);
        shareIcon.setOnClickListener(v -> Toast.makeText(getContext(), "Coming Soon! Thanks for your patience!", Toast.LENGTH_SHORT).show());
        UIUtils.setupOnTouchListener(shareIcon);

        ImageView watchLaterIcon = view.findViewById(R.id.watch_later);
        setupWatchListBtn(watchLaterIcon);
        UIUtils.setupOnTouchListener(watchLaterIcon);

        ImageView playTrailerIcon = view.findViewById(R.id.playTrailer);
        playTrailerIcon.setOnClickListener(v -> Toast.makeText(getContext(), "Coming Soon! Thanks for your patience!", Toast.LENGTH_SHORT).show());
        UIUtils.setupOnTouchListener(playTrailerIcon);

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
            PopupWindow pw = new PopupWindow(view, 800, 1280, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
        getLists();

        ImageView new_list_btn = view.findViewById(R.id.create_list_button);
        UIUtils.setupOnTouchListener(new_list_btn);
        new_list_btn.setOnClickListener(btn -> {
            EditText editText = view.findViewById(R.id.create_list_edit_text);
            String[] text_split = editText.getText().toString().split("\n");
            String list_name = text_split[0];
            String list_desc = String.join("\n", Arrays.asList(text_split).subList(1, text_split.length));
            editText.setText("");
            createNewList(list_name, list_desc);
            getLists();
        });

    }

    private void createNewList(String name, String desc) {
        Log.e("ses", User.getInstance().getSessionToken().getSessionId());
        Call<Object> create = RetrofitBuilder.getCreateListApi().createList(getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new CreateListBody(name, desc, "en"));
        create.enqueue(new Callback<Object>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("create", response.code() + " " + response.message() + " " + response.errorBody());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "list created", Toast.LENGTH_SHORT).show();
                    getLists();
                } else {
                    Toast.makeText(getContext(), "try again! internet not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "try again! internet not connected", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupWatchListBtn(final ImageView watchListImgBtn) {
        watchListImgBtn.setOnClickListener(v -> {
            AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
            Call<Object> addToFav = api.addToWatchList(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new WatchlistMovie("movie", movie.getId(), true));
            addToFav.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed to add to watchlist", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Successfully added to watchlist!", Toast.LENGTH_SHORT).show();
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed to add to watchlist", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupFavouriteButtons(final ImageView fav_img_empty, final ImageView fav_img_full) {
        fav_img_empty.setOnClickListener(v -> {
            AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
            Call<Object> addToFav = api.addToFav(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new FavouriteMovie("movie", movie.getId(), true));
            addToFav.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed rating", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Successfully added to favourites!", Toast.LENGTH_SHORT).show();
                    fav_img_empty.setVisibility(View.INVISIBLE);
                    fav_img_full.setVisibility(View.VISIBLE);
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // TODO remove from fav
        fav_img_full.setOnClickListener(v -> {
            AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
            Call<Object> addToFav = api.addToFav(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new FavouriteMovie("movie", movie.getId(), false));
            addToFav.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("failed rating", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Successfully removed from favourites!", Toast.LENGTH_SHORT).show();
                    fav_img_empty.setVisibility(View.VISIBLE);
                    fav_img_full.setVisibility(View.INVISIBLE);
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupRatingBar(RatingBar ratingBar) {
        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            MovieDetailsApi api = RetrofitBuilder.getMovieDetailApi();
            Call<Object> rate = api.rateMovie(movie.getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new Rate((int) v));
            rate.enqueue(new Callback<Object>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!response.isSuccessful()) {
                        Log.e("RatingBar", response.message() + response.code() + response.body());
                        Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "This movie is rated.", Toast.LENGTH_SHORT).show();
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("RatingBar", Objects.requireNonNull(t.getMessage()));
                    Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupCasts(TextView castsTextView) {
        MovieDetailsApi movieDetailsApi = RetrofitBuilder.getMovieDetailApi();
        Call<CastsList> castsListCall = movieDetailsApi.getCastsList(movie.getId(), res.getString(R.string.api_key));
        castsListCall.enqueue(new Callback<CastsList>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CastsList> call, Response<CastsList> response) {
                if (response.isSuccessful()) {
                    CastsList castsList = response.body();
                    if (castsList != null) {
                        List<Cast> casts = castsList.getCast().
                                subList(0, castsList.getCast().size() < 8 ? castsList.getCast().size() : 7);
                        Log.d("casts, get", response.message());
                        StringBuilder builder = new StringBuilder();
                        for (Cast cast : casts) {
                            builder = builder.append(cast.getName()).append(" as : ").append(cast.getCharacter()).append("\n\n");
                        }
                        if (casts.isEmpty())
                            castsTextView.setText(res.getString(R.string.casts_unavailable));
                        else
                            castsTextView.setText(builder.toString());
                        Log.d("setCasts", builder.toString());
                    }
                } else castsTextView.setText(R.string.casts_unavailable);
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CastsList> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch casts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupReviews(TextView reviewsTextView) {
        MovieDetailsApi movieDetailsApi = RetrofitBuilder.getMovieDetailApi();
        Call<Reviews> reviewsCall = movieDetailsApi.getReviews(movie.getId(), res.getString(R.string.api_key));

        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                if (reviews != null) {
                    List<Review> reviewsList = reviews.getReviews();
                    StringBuilder builder = new StringBuilder();
                    for (Review review : reviewsList) {
                        builder = builder.append(review.getAuthor()).append(" : ").append(review.getContent()).append("\n\n");
                    }
                    if (reviewsList.isEmpty())
                        reviewsTextView.setText(res.getString(R.string.reviews_unavailable));
                    else
                        reviewsTextView.setText(builder.toString());
                } else reviewsTextView.setText(R.string.reviews_unavailable);
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Reviews> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch reviews", Toast.LENGTH_SHORT).show();
            }
        });


    }


}