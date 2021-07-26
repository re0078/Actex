package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.SerializedName;
import com.mobiledevelopment.actex.models.Movie;

import java.util.List;

import lombok.Data;

@Data
public class PlaylistResponse {
    private String description;
    private String id;
    private List<Movie> items;
    @SerializedName("item_count")
    private int itemCount;
    private String name;
    @SerializedName("poster_path")
    private String posterPath;
}
