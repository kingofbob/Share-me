package com.soongwei.shareme.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.soongwei.shareme.R;
import com.soongwei.shareme.apis.FlickrHelper;
import com.soongwei.shareme.apis.GetOAuthTokenTask;
import com.soongwei.shareme.apis.OAuthTask;

import java.util.Locale;

import me.yokeyword.fragmentation.SupportActivity;


public class SplashActivity extends SupportActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


//        OAuth oauth = FlickrHelper.getOAuthToken(SplashActivity.this);


    }



    private void goToMain(){


        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(Intent intent) {
        //this is very important, otherwise you would get a null Scheme in the onResume later on.
        setIntent(intent);
    }



    public void onOAuthDone(OAuth result) {
        Log.d(SplashActivity.class.getSimpleName(), "No saved token:onOAuthDone:");
        if (result == null) {
            Toast.makeText(this,
                    "Authorization failed", //$NON-NLS-1$
                    Toast.LENGTH_LONG).show();
        } else {
            User user = result.getUser();
            OAuthToken token = result.getToken();
            if (user == null || user.getId() == null || token == null
                    || token.getOauthToken() == null
                    || token.getOauthTokenSecret() == null) {
                Toast.makeText(this,
                        "Authorization failed", //$NON-NLS-1$
                        Toast.LENGTH_LONG).show();
                return;
            }
            String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", //$NON-NLS-1$
                    user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            Toast.makeText(this,
                    message,
                    Toast.LENGTH_LONG).show();
            FlickrHelper.saveOAuthToken(SplashActivity.this, user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            goToMain();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String scheme = intent.getScheme();
        OAuth savedToken = FlickrHelper.getOAuthToken(SplashActivity.this);
        if (FlickrHelper.CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
            Log.d(SplashActivity.class.getSimpleName(), "No saved token");
            Uri uri = intent.getData();
            String query = uri.getQuery();
            Log.d(SplashActivity.class.getSimpleName(), "Returned Query: {}" + " : " +  query); //$NON-NLS-1$
            String[] data = query.split("&"); //$NON-NLS-1$
            if (data != null && data.length == 2) {
                String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
                String oauthVerifier = data[1]
                        .substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
                Log.d(SplashActivity.class.getSimpleName(),"OAuth Token: {}; OAuth Verifier: {}" +" : " +  oauthToken + " : " +  oauthVerifier); //$NON-NLS-1$

                OAuth oauth = FlickrHelper.getOAuthToken(SplashActivity.this);
                if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
                    GetOAuthTokenTask task = new GetOAuthTokenTask(SplashActivity.this);
                    task.execute(oauthToken, oauth.getToken().getOauthTokenSecret(), oauthVerifier);
                }
            }
        }else{
            if (savedToken == null || savedToken.getUser() == null) {
                new AlertDialog.Builder(SplashActivity.this)
                        .setMessage(R.string.permission3)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                OAuthTask task = new OAuthTask(SplashActivity.this);
                                task.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        }).setCancelable(false).show();

            } else {
                goToMain();
            }
        }




    }


}
