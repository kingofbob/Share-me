/**
 * 
 */
package com.soongwei.shareme.apis;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.photosets.Photoset;
import com.googlecode.flickrjandroid.photosets.Photosets;
import com.googlecode.flickrjandroid.photosets.PhotosetsInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GetPhotosetsTask extends AsyncTask<String, Integer, Photosets> {

	private Context context;

	public GetPhotosetsTask(Context context) {
		this.context = context;
	}

	@Override
	protected Photosets doInBackground(String... params) {
		String userId = params[0];
		Log.d(GetPhotosetsTask.class.getSimpleName(), "User id: " + userId);

		OAuth token = FlickrHelper.getOAuthToken(context);

		Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getToken().getOauthToken(),
				token.getToken().getOauthTokenSecret());
		PhotosetsInterface photosInterface = f.getPhotosetsInterface();
		try {
			Collection<Photoset> photosetWithPhotos = new ArrayList<>();


			Photosets photosets = photosInterface.getList(userId);
			for (Photoset photoset:  photosets.getPhotosets()){
				Log.d(GetPhotosetsTask.class.getSimpleName(), "Running ... " + photoset.getId());
				photosetWithPhotos.add(photosInterface.getPhotos(photoset.getId(), -1, -1));
			}

			photosets.setPhotosets(photosetWithPhotos);

			return photosets;
		} catch (Exception e) {
			Log.e(GetPhotosetsTask.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		}

	}

	@Override
	protected void onPostExecute(Photosets result) {
		if (context != null) {
			EventBus.getDefault().post(result);
		}
	}


}
