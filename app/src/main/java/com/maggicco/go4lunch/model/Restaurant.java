package com.maggicco.go4lunch.model;

import java.io.Serializable;

public class Restaurant implements Serializable {

    String restId;
    String restPhoto;
    String restName;
    String restAddress;
    String restPhone;
    String restWebsite;
    String restRating;

    public Restaurant() {
    }

    public Restaurant(String restId, String restPhoto, String restName, String restAddress, String restPhone, String restWebsite, String restRating) {
        this.restId = restId;
        this.restPhoto = restPhoto;
        this.restName = restName;
        this.restAddress = restAddress;
        this.restPhone = restPhone;
        this.restWebsite = restWebsite;
        this.restRating = restRating;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getRestPhoto() {
        return restPhoto;
    }

    public void setRestPhoto(String restPhoto) {
        this.restPhoto = restPhoto;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestAddress() {
        return restAddress;
    }

    public void setRestAddress(String restAddress) {
        this.restAddress = restAddress;
    }

    public String getRestPhone() {
        return restPhone;
    }

    public void setRestPhone(String restPhone) {
        this.restPhone = restPhone;
    }

    public String getRestWebsite() {
        return restWebsite;
    }

    public void setRestWebsite(String restWebsite) {
        this.restWebsite = restWebsite;
    }

    public String getRestRating() {
        return restRating;
    }

    public void setRestRating(String restRating) {
        this.restRating = restRating;
    }
}
