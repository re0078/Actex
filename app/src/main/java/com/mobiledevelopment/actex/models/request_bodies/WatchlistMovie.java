package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class WatchlistMovie {
    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("media_id")
    private Integer mediaId;
    @SerializedName("watchlist")
    private Boolean watchlist;


    public WatchlistMovie(String mediaType, Integer mediaId, Boolean watchlist) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.watchlist = watchlist;
    }
}
