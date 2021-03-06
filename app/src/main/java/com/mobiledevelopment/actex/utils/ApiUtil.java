package com.mobiledevelopment.actex.utils;

import static com.mobiledevelopment.actex.clients.RetrofitBuilder.getAuthApi;

import android.util.Log;

import com.mobiledevelopment.actex.clients.AccountApiEndpointInterface;
import com.mobiledevelopment.actex.clients.AuthApiEndpointInterface;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.account.Account;
import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;

import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUtil {
    private final User user;
    private static final ApiUtil API_UTIL = new ApiUtil(User.getInstance());
    private static final String TAG = "API_UTIL";

    public static ApiUtil getInstance() {
        return API_UTIL;
    }

    public void login(String username, String password, String apiKey,
                      CompletableFuture<Boolean> future) {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        user.setAuthDetails(username, password);
        user.setLoginSuccess(User.LoginSuccess.IN_PROGRESS);
        getRequestToken(myAuthApi, apiKey, future);
    }

    private void getRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey,
                                 CompletableFuture<Boolean> future) {
        Call<Token> call = myAuthApi.getRequestToken(apiKey);
        call.enqueue(new Callback<Token>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    user.setRequestToken(response.body());
                    validateRequestToken(myAuthApi, apiKey, future);
                } else {
                    future.complete(false);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Token> call, Throwable t) {
                future.complete(false);
            }

        });
    }

    private void validateRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey,
                                      CompletableFuture<Boolean> future) {
        String requestToken = user.getRequestToken().requestToken;
        String username = user.getUsername();
        String password = user.getPassword();
        Log.i(TAG, "validateRequestToken");

        Call<Token> call = myAuthApi.getValidateToken(apiKey, username, password, requestToken);
        call.enqueue(new Callback<Token>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    user.setRequestToken(response.body());
                    getSessionId(myAuthApi, apiKey, future);
                } else {
                    future.complete(false);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("salam", "validateRequestTok " + t.getMessage());
                future.complete(false);
            }
        });
    }

    private void getSessionId(AuthApiEndpointInterface myAuthApi, final String apiKey,
                              CompletableFuture<Boolean> future) {
        String requestToken = user.getRequestToken().requestToken;
        Log.i(TAG, requestToken);

        Call<Session> call = myAuthApi.getUserSession(apiKey, requestToken);

        call.enqueue(new Callback<Session>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Session> call, Response<Session> response) {
                Log.i("succeed", response.message());
                if (response.isSuccessful()) {
                    user.setSessionToken(response.body());
                    user.setLoginSuccess(User.LoginSuccess.SUCCEED);
                    getAccountDetails(apiKey, future);
                } else {
                    future.complete(false);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Session> call, Throwable t) {
                future.complete(false);
            }
        });
    }

    private void getAccountDetails(String apiKey, CompletableFuture<Boolean> future) {
        AccountApiEndpointInterface accountApiEndpointInterface = RetrofitBuilder.getAccountApi();
        Call<Account> getAccount = accountApiEndpointInterface.getAccountDetail(apiKey,
                User.getUser().getSessionToken().getSessionId());
        getAccount.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    user.setAccount(response.body());
                    user.setLoggedIn(true);
                    future.complete(true);
                } else {
                    Log.i("getAccountDetail", "onResponse: " + response.errorBody());
                    future.complete(false);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                future.complete(false);
            }
        });
    }

    public void logout(String apiKey, CompletableFuture<Boolean> future) {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        Log.i("Session token", user.getSessionToken().getSessionId());

        Call<Object> logout = myAuthApi.logout(apiKey, user.getSessionToken().getSessionId());
        logout.enqueue(new Callback<Object>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.code() == 200) {
                    user.setLoggedIn(false);
                    future.complete(true);
                } else {
                    future.complete(false);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Object> call, Throwable t) {
                future.complete(false);
            }
        });

    }
}
