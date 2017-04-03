package com.pitchai.flickrrecentphotobrowser.data.photoinfo;

/**
 * Created by pgrajama on 12/10/16.
 */

public class Photos {
    int page;
    int pages;
    int perpage;
    int total;
    Photo[] photo;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public int getTotal() {
        return total;
    }

    public Photo[] getPhoto() {
        return photo;
    }

}
