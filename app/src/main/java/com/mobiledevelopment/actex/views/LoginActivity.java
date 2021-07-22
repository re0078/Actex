package com.mobiledevelopment.actex.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.AuthApiEndpointInterface;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobiledevelopment.actex.clients.RetrofitBuilder.getAuthApi;


public class LoginActivity extends AppCompatActivity {
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = getResources();
    }

    private void loadSignUpPage() {
        String url = "https://www.themoviedb.org/signup";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    protected void login(String username, String password) {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        User.getInstance().setAuthDetails(username, password);
        User.getInstance().setLoginSuccess(User.LoginSuccess.IN_PROGRESS);
        getRequestToken(myAuthApi, res.getString(R.string.api_key));
    }

    protected void getRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey) {
        Log.i("salam", "getRequestToken");
        Call<Token> call = myAuthApi.getRequestToken(apiKey);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    validateRequestToken(myAuthApi, apiKey);
                } else {
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void validateRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        String username = User.getInstance().getUsername();
        String password = User.getInstance().getPassword();
        Log.i("salam", "validateRequestToken");

        Call<Token> call = myAuthApi.getValidateToken(apiKey, username, password, requestToken);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    getSessionId(myAuthApi, apiKey);
                } else {
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("salam", "validateRequestTok " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getSessionId(AuthApiEndpointInterface myAuthApi, final String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        Log.i("salam", requestToken);

        Call<Session> call = myAuthApi.getUserSession(apiKey, requestToken);

        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                Log.i("succeed", response.message());

                if (response.isSuccessful()) {
                    User.getInstance().setSessionToken(response.body());
                    User.getInstance().setLoginSuccess(User.LoginSuccess.SUCCEED);
                } else {
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout() {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        Log.i("Session token", User.getInstance().getSessionToken().getSessionId());

        Call<Object> logout = myAuthApi.logout(res.getString(R.string.api_key), User.getInstance().getSessionToken().getSessionId());
        logout.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.code() == 200) {
                    User.getUser().setLoggedIn(false);
                    Toast.makeText(getApplicationContext(), "you logged out successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}