package com.timecat.component.data.model.api;


public class SearchEngine {
    public String title;
    public String url;

    public SearchEngine(String string, String string1) {
        title = string;
        url = string1;
    }

    @Override
    public String toString() {
        return "SearchEngine{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}