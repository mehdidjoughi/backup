package com.example.mehdidjo.myapplication2.model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * Created by Mehdi Djo on 12/02/2018.
 */

public class Message implements IMessage ,  MessageContentType.Image {

    private String text;
    private String id;
    private Date createdAt;
    private Author author;
    private Image image;


    public Message(){

    }


    public void Date(){

    }
    public Message(String message, String id , Author author) {
        this.text = message;
        this.id = id;
        this.author = author;
        this.createdAt = new Date();
    }
    public Message(String message, String id , Author author , Date date) {
        this.text = message;
        this.id = id;
        this.author = author;
        //this.date = date;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }


    @Exclude
    @Override
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setDate() {
        this.createdAt = new Date();
    }

    public static class Image {
        private String url;
        public Image(String url) {
            this.url = url;
        }
    }
    public Author getAuthor() {
        return author;
    }
}
