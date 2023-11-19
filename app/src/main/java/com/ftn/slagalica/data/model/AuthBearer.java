package com.ftn.slagalica.data.model;

public class AuthBearer {
    private String username;
    private String email;
    private String password;
    private String imageURI;

    public AuthBearer(String username, String email, String password, String imageURI) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageURI = imageURI;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
