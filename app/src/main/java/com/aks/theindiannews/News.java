package com.aks.theindiannews;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class News extends RealmObject {
    /*
     * The News is an object which is shown in the list format
     * The required attributes are associated with each news and hence private modifier is used.
     * Realm database internally stores the objects to avoid redundant network calls and hence also enhances UI experience for the end user.
     *
     * */

    private String author;
    private String title;  ///// Either article, slideshow or video.
    private String description;
    private String url;
    private String urlToImg;
    private String publishedAt;
    private String contennt;

    /*
     * Creating getters and setters.
     * */
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImg() {
        return urlToImg;
    }

    public void setUrlToImg(String urlToImg) {
        this.urlToImg = urlToImg;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContennt() {
        return contennt;
    }

    public void setContennt(String contennt) {
        this.contennt = contennt;
    }



    /*
     * Setting properties of attributes in Realm database.
     *
     * */
    public final static String PROPERTY_AUTHOR="news_author";
    public final static String PROPERTY_TITLE="news_title";
    public final static String PROPERTY_DESCRIPTION="news_description";
    public final static String PROPERTY_URL="news_url";
    public final static String PROPERTY_URLTOIMG = "news_urlToImg";
    public final static String PROPERTY_PUBLISHEDAT = "news_publishedAt";
    public final static String PROPERTY_CONTENNT = "news_contennt";




    @PrimaryKey
    @Required
    public String news_author;
    public String news_title;
    public String news_description;
    public String news_url;
    public String news_urlToImg;
    public  String news_publishedAt;
    public  String news_contennt;


}
