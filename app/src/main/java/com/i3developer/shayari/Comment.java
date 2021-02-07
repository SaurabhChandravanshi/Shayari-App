package com.i3developer.shayari;

public class Comment {
    private String uid,commentText;

    public Comment(String uid, String commentText) {
        this.uid = uid;
        this.commentText = commentText;
    }

    public String getUid() {
        return uid;
    }

    public String getCommentText() {
        return commentText;
    }
}
