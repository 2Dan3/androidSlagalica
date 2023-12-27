package com.ftn.slagalica.data.model;

import androidx.annotation.Nullable;

public class GamePlayer {

    private String username;

    private int pointsThisGame;
    private int pointsCurrentRank;
    private int stars;
    private int tokens;

    private String picture;

    public GamePlayer(String username, int pointsThisGame, int pointsCurrentRank, int stars, int tokens, String picture)
    {
        this.username = username;
        this.pointsThisGame = pointsThisGame;
        this.pointsCurrentRank = pointsCurrentRank;
        this.stars = stars;
        this.tokens = tokens;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }

    public int getPointsCurrentRank() {
        return pointsCurrentRank;
    }

    public void setPointsCurrentRank(int pointsCurrentRank) {
        this.pointsCurrentRank = pointsCurrentRank;
    }
    public int getPointsThisGame() {
        return pointsThisGame;
    }

    public void addPointsThisGame(int morePoints) {
        this.pointsThisGame += morePoints;
    }
    public int getStars() {
        return stars;
    }

    public void addStars(int stars) {
        this.stars += stars;
    }

    public int getTokens() {
        return tokens;
    }

//    public void setTokens(int tokens) {
//        this.tokens = tokens;
//    }
//    public void spendToken() {
//        this.tokens -= 1;
//    }

    public boolean equals(@Nullable GamePlayer gamePlayer) {
        return this.getUsername().equals(gamePlayer.getUsername());
    }


//    Todo
//      make it a "toJSON()"

    @Override
    public String toString() {
        return "GamePlayer{" +
                "username='" + username + '\'' +
                ", pointsThisGame=" + pointsThisGame +
                ", pointsCurrentRank=" + pointsCurrentRank +
                ", stars=" + stars +
                ", tokens=" + tokens +
                ", picture='" + picture + '\'' +
                '}';
    }
}
