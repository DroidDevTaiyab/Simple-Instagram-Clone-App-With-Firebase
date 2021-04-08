package com.techpassmaster.simpleinstagramcloneapp.model;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class Post {

    private String postId;
    private String postImageUrl;
    private String description;
    private String currentuserId;
    private long postTimestamp;
    private String key = "";
    private boolean isShrink = true;

    public Post() {
    }

    public Post(String postId, String postImageUrl, String description, String currentuserId, long postTimestamp) {
        this.postId = postId;
        this.postImageUrl = postImageUrl;
        this.description = description;
        this.currentuserId = currentuserId;
        this.postTimestamp = postTimestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentuserId() {
        return currentuserId;
    }

    public void setCurrentuserId(String currentuserId) {
        this.currentuserId = currentuserId;
    }

    public long getPostTimestamp() {
        return postTimestamp;
    }

    public void setPostTimestamp(long postTimestamp) {
        this.postTimestamp = postTimestamp;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
