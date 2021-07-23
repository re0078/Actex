package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class AddToListBody {
    @SerializedName("media_id")
    private Integer mediaId;

    public AddToListBody(Integer mediaId) {
        this.mediaId = mediaId;
    }
}
