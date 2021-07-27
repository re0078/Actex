package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class FavouriteMovie {
    @SerializedName("media_type")
    private final String mediaType;
    @SerializedName("media_id")
    private final Integer mediaId;
    @SerializedName("favorite")
    private final Boolean favorite;


    public FavouriteMovie(String mediaType, Integer mediaId, Boolean favorite) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
    }
}
