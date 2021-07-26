package com.mobiledevelopment.actex;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobiledevelopment.actex.clients.AuthApiEndpointInterface;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;
import com.mobiledevelopment.actex.views.MovieDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.mobiledevelopment.actex.clients.RetrofitBuilder.getAuthApi;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = getResources();
        Button loginButton = findViewById(R.id.login_button);
        EditText username = findViewById(R.id.login_username_input);
        EditText password = findViewById(R.id.login_password_input);
        loginButton.setOnClickListener(view -> {
            String name = username.getText().toString();
            String pass = password.getText().toString();
            login(name, pass);
        });
        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(view -> loadSignUpPage());
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
        Call<Token> call = myAuthApi.getRequestToken(apiKey);

        call.enqueue(new Callback<Token>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    validateRequestToken(myAuthApi, apiKey);
                    //TODO handle showing movies list
                    Toast.makeText(getApplicationContext(), "Logged in successfully",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "failed to connect!"
                            + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void validateRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        String username = User.getInstance().getUsername();
        String password = User.getInstance().getPassword();
        Log.i(TAG, "validateRequestToken");

        Call<Token> call = myAuthApi.getValidateToken(apiKey, username, password, requestToken);
        call.enqueue(new Callback<Token>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    getSessionId(myAuthApi, apiKey);
                } else {
                    Toast.makeText(getApplicationContext(), "failed to connect!" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("salam", "validateRequestTok " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getSessionId(AuthApiEndpointInterface myAuthApi, final String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        Log.i(TAG, requestToken);

        Call<Session> call = myAuthApi.getUserSession(apiKey, requestToken);

        call.enqueue(new Callback<Session>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Session> call, Response<Session> response) {
                Log.i("succeed", response.message());

                if (response.isSuccessful()) {
                    User.getInstance().setSessionToken(response.body());
                    User.getInstance().setLoginSuccess(User.LoginSuccess.SUCCEED);
                } else {
                    Toast.makeText(getApplicationContext(), "failed to connect!" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            @EverythingIsNonNull
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
            @EverythingIsNonNull
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.code() == 200) {
                    User.getUser().setLoggedIn(false);
                    Toast.makeText(getApplicationContext(), "you logged out successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}