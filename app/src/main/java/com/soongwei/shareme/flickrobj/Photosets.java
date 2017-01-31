package com.soongwei.shareme.flickrobj;

import java.util.List;

public class Photosets {

public Integer total;
public Integer cancreate;
public Integer perpage;
public Integer page;
public List<Photoset> photoset = null;
public Integer pages;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCancreate() {
        return cancreate;
    }

    public void setCancreate(Integer cancreate) {
        this.cancreate = cancreate;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Photoset> getPhotoset() {
        return photoset;
    }

    public void setPhotoset(List<Photoset> photoset) {
        this.photoset = photoset;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}