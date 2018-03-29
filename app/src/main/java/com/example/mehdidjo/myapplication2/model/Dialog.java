package com.example.mehdidjo.myapplication2.model;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehdi Djo on 28/02/2018.
 */

public class Dialog implements IDialog {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Author> users;
    private Message lastMessage;

    private int unreadCount;

    public Dialog(String id, String name, String photo,
                  ArrayList<Author> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {

        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }
}
