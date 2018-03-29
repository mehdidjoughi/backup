package com.example.mehdidjo.myapplication2.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by Mehdi Djo on 12/02/2018.
 */

public class Author implements IUser {

    private String id;
    private String name;
    private String email;
    private String photoUrl;

    public Author (){

    }

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Author(String id, String name ,String email , String photoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setId(String id){
        this.id=id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }
}
