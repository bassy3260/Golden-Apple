package com.aaa.aaa;

public class writeInfo {
    String category
    String title;
    String uid;
    String date;
    String time;
    String content;

    public writeInfo(String Category,String title, String uid, String date,
                     String time, String content) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.date = date;
        this.uid = uid;
        this.category=category;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public String getCategory() {
        return category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
