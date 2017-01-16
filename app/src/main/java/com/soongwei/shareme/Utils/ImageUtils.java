package com.soongwei.shareme.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.soongwei.shareme.interfaces.OnPhoneImagesObtained;
import com.soongwei.shareme.objects.PhoneAlbum;
import com.soongwei.shareme.objects.PhonePhoto;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by SoongWei on 15-Jan-17.
 */

public class ImageUtils {

    public static ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    public static void getPhoneAlbums(Context context , OnPhoneImagesObtained listener ){
        // Creating vectors to hold the final albums objects and albums names
        Vector< PhoneAlbum > phoneAlbums = new Vector<>();
        Vector< String > albumsNames = new Vector<>();

        // which image properties are we querying
        String[] projection = new String[] {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };

        // content: style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"        // Ordering
        );

        if ( cur != null && cur.getCount() > 0 ) {
            Log.i("DeviceImageManager"," query count=" + cur.getCount());

            if (cur.moveToFirst()) {
                String bucketName;
                String data;
                String imageId;
                int bucketNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID );

                do {
                    // Get the field values
                    bucketName = cur.getString( bucketNameColumn );
                    data = cur.getString( imageUriColumn );
                    imageId = cur.getString( imageIdColumn );

                    // Adding a new PhonePhoto object to phonePhotos vector
                    PhonePhoto phonePhoto = new PhonePhoto();
                    phonePhoto.setAlbumName( bucketName );
                    phonePhoto.setPhotoUri( data );
                    phonePhoto.setId( Integer.valueOf( imageId ) );

                    if ( albumsNames.contains( bucketName ) ) {
                        for ( PhoneAlbum album : phoneAlbums ) {
                            if ( album.getName().equals( bucketName ) ) {
                                album.getAlbumPhotos().add( phonePhoto );
                                Log.i( "DeviceImageManager", "A photo was added to album => " + bucketName );
                                break;
                            }
                        }
                    } else {
                        PhoneAlbum album = new PhoneAlbum();
                        Log.i( "DeviceImageManager", "A new album was created => " + bucketName );
                        album.setId( phonePhoto.getId() );
                        album.setName( bucketName );
                        album.setCoverUri( phonePhoto.getPhotoUri() );
                        album.getAlbumPhotos().add( phonePhoto );
                        Log.i( "DeviceImageManager", "A photo was added to album => " + bucketName );

                        phoneAlbums.add( album );
                        albumsNames.add( bucketName );
                    }

                } while (cur.moveToNext());
            }

            cur.close();
            listener.onComplete( phoneAlbums );
        } else {
            listener.onError();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
