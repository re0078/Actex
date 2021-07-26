package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.SerializedName;
import com.mobiledevelopment.actex.models.Movie;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Playlist {
    private java.util.List<Movie> results = new ArrayList<>();
    private List<Movie> items = new ArrayList<>();
    @SerializedName("created_by")
    private String createdBy;
    private String description;
    @SerializedName("favourite_count")
    private int favouriteCount;
    private String id;
    @SerializedName("item_count")
    private int itemCount;
    @SerializedName("iso_639_1")
    private String lang;
    private String name;
    @SerializedName("poster_path")
    private String posterPath;
}
