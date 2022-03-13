package com.maggicco.go4lunch.model;

import java.io.Serializable;

public class WorkMate implements Serializable {

    String mateId;
    String matePhoto;
    String mateName;

    public WorkMate() {
    }

    public WorkMate(String mateId, String matePhoto, String mateName, String mateMail) {
        this.mateId = mateId;
        this.matePhoto = matePhoto;
        this.mateName = mateName;
    }

    public String getMateId() {
        return mateId;
    }

    public void setMateId(String mateId) {
        this.mateId = mateId;
    }

    public String getMatePhoto() {
        return matePhoto;
    }

    public void setMatePhoto(String matePhoto) {
        this.matePhoto = matePhoto;
    }

    public String getMateName() {
        return mateName;
    }

    public void setMateName(String mateName) {
        this.mateName = mateName;
    }

}
