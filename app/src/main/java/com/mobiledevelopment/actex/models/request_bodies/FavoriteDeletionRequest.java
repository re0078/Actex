package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

public class FavoriteDeletionRequest extends BaseDeletionRequest {
    @SerializedName("media_type")
    private String mediaType;
    private boolean favorite;

    public FavoriteDeletionRequest(Integer mediaId) {
        super(mediaId);
    }

    public FavoriteDeletionRequest(Integer mediaId, String mediaType, boolean favorite) {
        super(mediaId);
        this.mediaType = mediaType;
        this.favorite = favorite;
    }
}
