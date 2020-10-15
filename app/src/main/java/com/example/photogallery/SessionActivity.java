package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.TwitterException;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class SessionActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        loginButton = findViewById(R.id.login_button);

        Button logoutButton = findViewById(R.id.logout);
        TextView accountText = findViewById(R.id.loginText);

        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            loginButton.setVisibility(View.GONE);
            accountText.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);

            TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

            String userName = session.getUserName();
            accountText.setText("Logged in as: " + userName);

        } else {
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // restarts SessionActivity to apply login
                     startActivity(getIntent());
                     finish();
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TWITTER LOGIN", "FAILED");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    // log out user
    public void logOut(View v) {

        // check if there is an active session
        if (TwitterCore.getInstance().getSessionManager()
                .getActiveSession() != null) {

            // clears the active session
            TwitterCore.getInstance().getSessionManager().clearActiveSession();

            // restarts SessionActivity to apply logout
            startActivity(getIntent());
            finish();
        }
    }
}

