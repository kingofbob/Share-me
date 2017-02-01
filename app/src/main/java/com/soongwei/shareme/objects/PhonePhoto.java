package com.soongwei.shareme.objects;

public class PhonePhoto {

    private Long id;
    private String albumName;
    private String photoUri;
    private String photoOriginalUri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoOriginalUri() {
        return photoOriginalUri;
    }

    public void setPhotoOriginalUri(String photoOriginalUri) {
        this.photoOriginalUri = photoOriginalUri;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName( String name ) {
        this.albumName = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri( String photoUri ) {
        this.photoUri = photoUri;
    }
}