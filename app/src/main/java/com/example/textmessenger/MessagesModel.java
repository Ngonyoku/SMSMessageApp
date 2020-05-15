package com.example.textmessenger;

class MessagesModel {
    private String sender, information;

    public MessagesModel() {
    }

    public MessagesModel(String sender, String information) {
        this.sender = sender;
        this.information = information;
    }

    public String getSender() {
        return sender;
    }

    public String getInformation() {
        return information;
    }
}
