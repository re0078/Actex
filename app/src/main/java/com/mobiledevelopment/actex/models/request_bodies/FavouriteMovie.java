package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class FavouriteMovie {
    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("media_id")
    private Integer mediaId;
    @SerializedName("favorite")
    private Boolean favorite;


    public FavouriteMovie(String mediaType, Integer mediaId, Boolean favorite) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
    }
}
