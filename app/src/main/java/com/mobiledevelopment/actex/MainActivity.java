package com.mobiledevelopment.actex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobiledevelopment.actex.utils.ApiUtil;
import com.mobiledevelopment.actex.utils.UIUtils;
import com.mobiledevelopment.actex.views.MovieDetailActivity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private ApiUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiUtil = ApiUtil.getInstance();
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> loginProcess());
        UIUtils.setupOnTouchListener(loginButton);
        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(view -> signUpProcess());
        UIUtils.setupOnTouchListener(signUpButton);
    }

    private void loginProcess() {
        String name = ((EditText) findViewById(R.id.login_username_input)).getText().toString();
        String pass = ((EditText) findViewById(R.id.login_password_input)).getText().toString();
        CompletableFuture<Boolean> accessFuture = new CompletableFuture<>();
        runOnUiThread(() -> apiUtil.login(name, pass, getString(R.string.api_key), accessFuture));
        accessFuture.whenComplete((access, t) -> {
            if (!Objects.isNull(access)) {
                if (access) {
                    Intent intent = new Intent(this, MovieDetailActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(this, "Authorization failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void signUpProcess() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.sign_up_url)));
        startActivity(intent);
    }
}