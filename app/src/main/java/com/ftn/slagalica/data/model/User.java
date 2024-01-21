package com.ftn.slagalica.data.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    public String email;
    public String password;
    public String username;
    public String picture;
    public int tokens;
    public int stars;
    public int points;
    @Exclude
    private int pointsCurrentMatch;

    public int played;

    public int won;
    public int pointsWhoKnows;
    public int pointsConnectTwo;
    public int pointsAssociations;
    public int pointsJumper;
    public int pointsStepByStep;
    public int pointsMyNumber;
    public User(){ }

    public User(String email, String password, String username, String picture, int tokens, int stars, int points, int played, int won, int pointsWhoKnows, int pointsConnectTwo, int pointsAssociations, int pointsJumper, int pointsStepByStep, int pointsMyNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.picture = picture;
        this.tokens = tokens;
        this.stars = stars;
        this.points = points;
        this.played = played;
        this.won = won;
        this.pointsWhoKnows = pointsWhoKnows;
        this.pointsConnectTwo = pointsConnectTwo;
        this.pointsAssociations = pointsAssociations;
        this.pointsJumper = pointsJumper;
        this.pointsStepByStep = pointsStepByStep;
        this.pointsMyNumber = pointsMyNumber;
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.picture = null;
        this.tokens = 5;
        this.stars = 0;
        this.points = 0;
        this.played = 0;
        this.won = 0;
        this.pointsWhoKnows = 0;
        this.pointsConnectTwo = 0;
        this.pointsAssociations = 0;
        this.pointsJumper = 0;
        this.pointsStepByStep = 0;
        this.pointsMyNumber = 0;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        HashMap<String, Object> profileObj = new HashMap<>();
        profileObj.put("username", username);
        profileObj.put("email", email);
        profileObj.put("password", password);
        profileObj.put("points", points);
        profileObj.put("stars", stars);
        profileObj.put("tokens", tokens);
        profileObj.put("picture", picture);
        result.put("profile", profileObj);

        HashMap<String, Object> statsObj = new HashMap<>();
        statsObj.put("played",played);
        statsObj.put("won", won);
        statsObj.put("pointsWhoKnows", pointsWhoKnows);
        statsObj.put("pointsConnectTwo", pointsConnectTwo);
        statsObj.put("pointsAssociations", pointsAssociations);
        statsObj.put("pointsJumper", pointsJumper);
        statsObj.put("pointsStepByStep", pointsStepByStep);
        statsObj.put("pointsMyNumber", pointsMyNumber);
        result.put("stats", statsObj);

        return result;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getPointsWhoKnows() {
        return pointsWhoKnows;
    }

    public void setPointsWhoKnows(int pointsWhoKnows) {
        this.pointsWhoKnows = pointsWhoKnows;
    }

    public int getPointsConnectTwo() {
        return pointsConnectTwo;
    }

    public void setPointsConnectTwo(int pointsConnectTwo) {
        this.pointsConnectTwo = pointsConnectTwo;
    }

    public int getPointsAssociations() {
        return pointsAssociations;
    }

    public void setPointsAssociations(int pointsAssociations) {
        this.pointsAssociations = pointsAssociations;
    }

    public int getPointsJumper() {
        return pointsJumper;
    }

    public void setPointsJumper(int pointsJumper) {
        this.pointsJumper = pointsJumper;
    }

    public int getPointsStepByStep() {
        return pointsStepByStep;
    }

    public void setPointsStepByStep(int pointsStepByStep) {
        this.pointsStepByStep = pointsStepByStep;
    }

    public int getPointsMyNumber() {
        return pointsMyNumber;
    }

    public void setPointsMyNumber(int pointsMyNumber) {
        this.pointsMyNumber = pointsMyNumber;
    }
    @Exclude
    public int getPointsCurrentMatch() {
        return pointsCurrentMatch;
    }
    @Exclude
    public void setPointsCurrentMatch(int pointsCurrentMatch) {
        this.pointsCurrentMatch = pointsCurrentMatch;
    }
}
