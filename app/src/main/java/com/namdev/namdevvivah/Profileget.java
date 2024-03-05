package com.namdev.namdevvivah;

public class Profileget {
    private final String id;
    private final String username;
    private final String email;
    private final  String image;

    public Profileget(String id, String username, String email,String img) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.image = img;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return email;
    }
    public String getImage() {
        return image ;
    }
}