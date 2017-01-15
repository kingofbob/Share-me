package com.soongwei.shareme.objects;

/**
 * Created by SoongWei on 15-Jan-17.
 */

public class GalleryImageObject {
    private long id;
    private String title;
    private String uri;
    private boolean selected;

    public GalleryImageObject(long id, String title, String uri, boolean selected) {
        this.id = id;
        this.title = title;
        this.uri = uri;
        this.selected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
