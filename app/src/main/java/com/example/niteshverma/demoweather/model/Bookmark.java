package com.example.niteshverma.demoweather.model;

import java.io.Serializable;

public class Bookmark implements Serializable {

    private long id;
    private String lat;
    private String lon;
    private String locationName;
    private int deleteIndex;
    private boolean isDeleted;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)id;
//        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Bookmark){
            if(id == ((Bookmark)obj).id){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDeleteIndex() {
        return deleteIndex;
    }

    public void setDeleteIndex(int deleteIndex) {
        this.deleteIndex = deleteIndex;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
