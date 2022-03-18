package com.maggicco.go4lunch.model;

import java.io.Serializable;

public class WorkMate implements Serializable {

    String mateId;
    String userImageUrl;
    String userName;

    public WorkMate() {
    }


    public String getMateId() {
        return mateId;
    }

    public void setMateId(String mateId) {
        this.mateId = mateId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
