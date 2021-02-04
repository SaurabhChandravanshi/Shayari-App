package com.i3developer.shayari;

import java.util.HashMap;
import java.util.Map;

public class PublicPost {
    private String ownerUID,imagePath;
    private Map<String,String> commentMap = new HashMap<>();
    private Map<String,String> clapsMap = new HashMap<>();

    public PublicPost(String ownerUID, String imagePath, Map<String, String> commentMap, Map<String, String> clapsMap) {
        this.ownerUID = ownerUID;
        this.imagePath = imagePath;
        this.commentMap = commentMap;
        this.clapsMap = clapsMap;
    }

    public Map<String, String> getClapsMap() {
        return clapsMap;
    }

    public void setClapsMap(Map<String, String> clapsMap) {
        this.clapsMap = clapsMap;
    }

    public Map<String, String> getCommentMap() {
        return commentMap;
    }

    public void setCommentMap(Map<String, String> commentMap) {
        this.commentMap = commentMap;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
