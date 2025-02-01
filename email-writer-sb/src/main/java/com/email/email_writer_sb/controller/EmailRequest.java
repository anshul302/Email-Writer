package com.email.email_writer_sb.controller;



public class EmailRequest {

    private String emailContent;
    private String tone; // Assuming you have a tone field here

    // Constructor, getters, and setters

    public EmailRequest(String emailContent, String tone) {
        this.emailContent = emailContent;
        this.tone = tone;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}

