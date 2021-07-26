package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseDeletionRequest {
    @SerializedName("media_id")
    private Integer mediaId;
}
