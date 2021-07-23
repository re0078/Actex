package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.SerializedName;
import com.mobiledevelopment.actex.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    public List<Movie> results = new ArrayList<>();
    public List<Movie> items = new ArrayList<>();
    @SerializedName("created_by")
    public String createdBy;
    public String description;
    @SerializedName("favourite_count")
    public int favouriteCount;
    public String id;
    @SerializedName("item_count")
    public int itemCount;
    @SerializedName("iso_639_1")
    public String lang;
    public String name;
    @SerializedName("poster_path")
    public String posterPath;

    public List<Movie> getResults() {
        return items.isEmpty() ? results : items;
    }

    public int page;
}
