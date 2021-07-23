package com.mobiledevelopment.actex.models.network;

import com.google.gson.annotations.SerializedName;

public class Token {
    public boolean success;
    @SerializedName("expires_at")
    public String expiresAt;
    @SerializedName("request_token")
    public String requestToken;
}
