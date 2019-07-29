package com.prashantdhiman.chatie.models;

public class MessageObject {

    private String chatId;
    private String messageId;
    private String senderId;
    private String senderName;
    private String message;


    public MessageObject(String chatId,String messageId, String senderId, String senderName, String message) {
        this.chatId=chatId;
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
