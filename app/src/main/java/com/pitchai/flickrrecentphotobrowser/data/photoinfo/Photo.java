package com.pitchai.flickrrecentphotobrowser.data.photoinfo;

import java.io.Serializable;

/**
 * Created by pgrajama on 12/10/16.
 */

public class Photo implements Serializable{
    String id;
    String owner;
    String secret;
    String server;
    int farm;
    String title;
    int ispublic;
    int isfriend;
    int isfamily;
    Urls urls;
    String ownername;
    String datetaken;

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public int isPublic() {
        return ispublic;
    }

    public int isFriend() {
        return isfriend;
    }

    public int isFamily() {
        return isfamily;
    }

    public Urls getUrls() {
        return  urls;
    }

    public String getOwnername() {
        return ownername;
    }

    public String getDatetaken() {
        return datetaken;
    }
}
