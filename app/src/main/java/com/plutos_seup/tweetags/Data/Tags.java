package com.plutos_seup.tweetags.Data;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Tags {

    private String cover_url;
    private String tag_name;
    private String tag_key;
    private String tag_date;
    private String sub_tag_count;

    public Tags() {
    }

    public String getSub_tag_count() {
        return sub_tag_count;
    }

    public void setSub_tag_count(String sub_tag_count) {
        this.sub_tag_count = sub_tag_count;
    }

    public String getTag_key() {
        return tag_key;
    }

    public void setTag_key(String tag_key) {
        this.tag_key = tag_key;
    }

    public String getTag_date() {
        return tag_date;
    }

    public void setTag_date(String tag_date) {
        this.tag_date = tag_date;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
