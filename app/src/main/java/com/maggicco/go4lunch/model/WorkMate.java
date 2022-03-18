package com.maggicco.go4lunch.model;

import java.io.Serializable;

public class WorkMate implements Serializable {

    String userId;
    String userImageUrl;
    String userName;

    public WorkMate() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
