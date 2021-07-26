package com.mobiledevelopment.actex.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SimpleResponse {
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("status_message")
    private String statusMessage;
}
