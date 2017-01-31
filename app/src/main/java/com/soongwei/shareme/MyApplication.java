package com.soongwei.shareme;

import android.app.Application;
import android.content.Context;

import com.soongwei.shareme.apis.FlickrClient;

public class MyApplication extends Application {
	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = this;

    }
    
    public static FlickrClient getRestClient() {
    	return (FlickrClient) FlickrClient.getInstance(FlickrClient.class, MyApplication.context);
    }
}