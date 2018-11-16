package com.justclack.legends_quotes;

public class AllCatsModel {
    String url, title;
    public static final String site = "https://www.brainyquote.com/";

    public AllCatsModel(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}