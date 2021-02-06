package com.i3developer.shayari;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicPost {
    private String postId,ownerUID,imagePath;
    private Map<String,String> commentMap = new HashMap<>();
    private List<String> likes = new ArrayList<>();

    public PublicPost(String postId, String ownerUID, String imagePath, Map<String, String> commentMap, List<String> likes) {
        this.postId = postId;
        this.ownerUID = ownerUID;
        this.imagePath = imagePath;
        this.commentMap = commentMap;
        this.likes = likes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public PublicPost() {
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
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
