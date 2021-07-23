package com.mobiledevelopment.actex.clients;

import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiEndpointInterface {
    @GET("authentication/token/new")
    Call<Token> getRequestToken(@Query("api_key") String apiKey);

    @FormUrlEncoded
    @POST("authentication/token/validate_with_login")
    Call<Token> getValidateToken(@Query("api_key") String apiKey,
                                 @Field("username") String username, @Field("password") String password, @Field("request_token") String requestToken);

    @FormUrlEncoded
    @POST("authentication/session/new")
    Call<Session> getUserSession(@Query("api_key") String apiKey, @Field("request_token") String requestToken);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    Call<Object> logout(@Query("api_key") String apiKey, @Field("session_id") String sessionId);
}
