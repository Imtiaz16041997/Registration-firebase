package com.example.signup;

public class dataHolder {
    private String name,course,contact,pimage;

    public dataHolder(String name, String course, String contact, String pimage) {
        this.name = name;
        this.course = course;
        this.contact = contact;
        this.pimage = pimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
