package com.soongwei.shareme.objects;

/**
 * Created by SoongWei on 15-Jan-17.
 */

public class GalleryImageObject {
    private long id;
    private String title;
    private String uri;
    private String uriO;
    private boolean selected;

    public GalleryImageObject(long id, String title, String uri,String uriO, boolean selected) {
        this.id = id;
        this.title = title;
        this.uri = uri;
        this.uriO = uriO;
        this.selected = selected;
    }

    public String getUriO() {
        return uriO;
    }

    public void setUriO(String uriO) {
        this.uriO = uriO;
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
