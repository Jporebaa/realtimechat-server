package com.raven.model;

import java.sql.Timestamp;
import java.util.Date;

public class Model_Receive_Message {


    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Model_Receive_Image getDataImage() {
        return dataImage;
    }

    public void setDataImage(Model_Receive_Image dataImage) {
        this.dataImage = dataImage;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Model_Receive_Message(int messageType, int fromUserID, String text, Model_Receive_Image dataImage, Timestamp createDate) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.text = text;
        this.dataImage = dataImage;
        this.createDate = createDate;
    }

    public Model_Receive_Message(int messageType, int fromUserID, String text, Model_Receive_Image dataImage) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.text = text;
        this.dataImage = dataImage;
    }

    public Model_Receive_Message() {
    }

    private int messageType;
    private int fromUserID;
    private String text;
    private Model_Receive_Image dataImage;
    private Timestamp createDate;
}