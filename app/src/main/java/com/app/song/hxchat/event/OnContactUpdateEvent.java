package com.app.song.hxchat.event;

/**
 * Created by song on 2017/3/25 21:02
 */


public class OnContactUpdateEvent {
    public String contact;
    public boolean isAdded;

    public OnContactUpdateEvent(String contact, boolean isAdded) {
        this.contact = contact;
        this.isAdded = isAdded;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
