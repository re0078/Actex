package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class WatchlistDeletionRequest extends BaseDeletionRequest {
    @SerializedName("media_type")
    private String mediaType;
    private boolean watchlist;

    public WatchlistDeletionRequest(Integer mediaId) {
        super(mediaId);
    }

    public WatchlistDeletionRequest(Integer mediaId, String mediaType, boolean watchlist) {
        super(mediaId);
        this.mediaType = mediaType;
        this.watchlist = watchlist;
    }
}
