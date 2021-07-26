package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.SerializedName;
import com.mobiledevelopment.actex.models.Movie;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Playlist {
    private String description;
    @SerializedName("favourite_count")
    private int favouriteCount;
    private String id;
    @SerializedName("item_count")
    private int itemCount;
    @SerializedName("iso_639_1")
    private String lang;
    @SerializedName("list_type")
    private String listType;
    private String name;
    @SerializedName("poster_path")
    private String posterPath;
}
