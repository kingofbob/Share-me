package com.soongwei.shareme.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.interestingness.InterestingnessInterface;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photosets.PhotosetsInterface;

public final class FlickrHelper {


	public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
	public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
	public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
	public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
	public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
	public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$


	private static FlickrHelper instance = null;
	private static final String API_KEY = "7588721b8f93ca84fee2c4234aae4759"; //$NON-NLS-1$
	public static final String API_SEC = "963d52bd884eddf9"; //$NON-NLS-1$

	private FlickrHelper() {

	}

	public static FlickrHelper getInstance() {
		if (instance == null) {
			instance = new FlickrHelper();
		}

		return instance;
	}

	public Flickr getFlickr() {
		try {
			Flickr f = new Flickr(API_KEY, API_SEC, new REST());
			return f;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}

	public Flickr getFlickrAuthed(String token, String secret) {
		Flickr f = getFlickr();
		RequestContext requestContext = RequestContext.getRequestContext();
		OAuth auth = new OAuth();
		auth.setToken(new OAuthToken(token, secret));
		requestContext.setOAuth(auth);
		return f;
	}

	public InterestingnessInterface getInterestingInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getInterestingnessInterface();
		} else {
			return null;
		}
	}

	public PhotosInterface getPhotosInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getPhotosInterface();
		} else {
			return null;
		}
	}

	public PhotosetsInterface getPhotosetsInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getPhotosetsInterface();
		} else {
			return null;
		}
	}


	public static OAuth getOAuthToken(Context context) {
		//Restore preferences
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			Log.w(FlickrHelper.class.getSimpleName(), "No oauth token retrieved"); //$NON-NLS-1$
			return null;
		}
		OAuth oauth = new OAuth();
		String userName = settings.getString(KEY_USER_NAME, null);
		String userId = settings.getString(KEY_USER_ID, null);
		if (userId != null) {
			User user = new User();
			user.setUsername(userName);
			user.setId(userId);
			oauth.setUser(user);
		}
		OAuthToken oauthToken = new OAuthToken();
		oauth.setToken(oauthToken);
		oauthToken.setOauthToken(oauthTokenString);
		oauthToken.setOauthTokenSecret(tokenSecret);
		Log.d(FlickrHelper.class.getSimpleName(), "Retrieved token from preference store: oauth token={}, and token secret={}" + oauthTokenString +" : " + tokenSecret); //$NON-NLS-1$
		return oauth;
	}

	public static void saveOAuthToken(Context context, String userName, String userId, String token, String tokenSecret) {
		Log.d(FlickrHelper.class.getSimpleName(),"Saving userName=%s, userId=%s, oauth token={}, and token secret={}" + " : " + userName + " : " +  userId + " : " +  token+ " : " +  tokenSecret); //$NON-NLS-1$
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(KEY_OAUTH_TOKEN, token);
		editor.putString(KEY_TOKEN_SECRET, tokenSecret);
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
	}

	public static void cleanupOAuthToken(Context context) {
		//logger.debug("Saving userName=%s, userId=%s, oauth token={}, and token secret={}", new String[]{userName, userId, token, tokenSecret}); //$NON-NLS-1$
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(KEY_OAUTH_TOKEN);
		editor.remove(KEY_TOKEN_SECRET);
		editor.remove(KEY_USER_NAME);
		editor.remove(KEY_USER_ID);
		editor.commit();
	}

	public static String imageUrlGenerator(String farmId, String serverId, String id, String secret, String size){
		return "https://farm"+farmId+".staticflickr.com/"+serverId+"/"+id+"_"+secret+"_"+size+".jpg";
	}

}
