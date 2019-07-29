package com.prashantdhiman.chatie.models;

public class ChatObject {
    private String chatId;
    private String name;

    public ChatObject(String chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
