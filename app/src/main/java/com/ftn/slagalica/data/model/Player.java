package com.ftn.slagalica.data.model;

public class Player {

    private String username;
    private String email;
    private String password;

    private int pointsCurrentRank;
    private int currentPointsInMatch;
    private int stars;
    private int tokens;

    private String picture;

    public Player(String username, String email, String password, String picture, int pointsCurrentRank, int stars, int tokens) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.pointsCurrentRank = pointsCurrentRank;
        this.stars = stars;
        this.tokens = tokens;
    }
    public Player() {

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPointsCurrentRank() {
        return pointsCurrentRank;
    }

    public void setPointsCurrentRank(int pointsCurrentRank) {
        this.pointsCurrentRank = pointsCurrentRank;
    }
    public int getCurrentPointsInMatch() {
        return currentPointsInMatch;
    }

    public void setCurrentPointsInMatch(int currentPointsInMatch) {
        this.currentPointsInMatch = currentPointsInMatch;
    }
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
//    Todo
//      make it a "toJSON()"

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pointsCurrentRank=" + pointsCurrentRank +
                ", currentPointsInMatch=" + currentPointsInMatch +
                ", stars=" + stars +
                ", picture='" + picture + '\'' +
                '}';
    }

    public void spendToken() {
        this.tokens -= 1;
    }
}
