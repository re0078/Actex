package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PlaylistsResult {
    private int page;
    private List<Playlist> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

}
