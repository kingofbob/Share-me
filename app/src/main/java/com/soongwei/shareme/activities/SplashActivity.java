package com.soongwei.shareme.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.codepath.oauth.OAuthLoginActivity;
import com.soongwei.shareme.MyApplication;
import com.soongwei.shareme.R;
import com.soongwei.shareme.apis.FlickrClient;


public class SplashActivity extends OAuthLoginActivity<FlickrClient> {

    private FlickrClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        client = MyApplication.getRestClient();

        if (client.isAuthenticated()){
            goToMain();
        }else{
            new AlertDialog.Builder(SplashActivity.this)
                    .setMessage(R.string.permission3)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getClient().connect();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })

                    .show();
        }



    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoginSuccess() {
        goToMain();
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
