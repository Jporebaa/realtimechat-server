package com.raven.model;


public class Model_All_Messages {

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }


    public Model_All_Messages(int fromUserID, int toUserID) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
    }

    public Model_All_Messages() {
    }

    private int fromUserID;
    private int toUserID;

}
