package com.levqo.githubparsing.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 10.10.2018.
 */

public class Item {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("html_url")
    @Expose
    private String html_url;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    public Item(String id, String name, String html_url,String description, String full_name){
        this.name = name;
        this.html_url = html_url;
        this.description = description;
        this.full_name = full_name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getHtmlUrl(){
        return html_url;
    }

    public void setHtmlUrl(String html_url){
        this.html_url = html_url;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getFullName(){
        return full_name;
    }

    public void setFullName(String full_name){
        this.full_name = full_name;
    }

}
