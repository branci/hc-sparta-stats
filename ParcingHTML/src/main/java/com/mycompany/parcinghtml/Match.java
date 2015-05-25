/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;
import java.sql.Date;
/**
 *
 * @author kristian
 */
public class Match {

    
    private int id;
    private java.sql.Date date;
    private String opponent;
    private int spartaGoals;
    private int opponentGoals;
    private int spartaShots;
    private int opponentShots;
    private int spartaPenalty;
    private int opponentPenalty;
    private String home;
    private String bestPlayer;
    private int season;

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", date=" + date + ", opponent=" + opponent + ", spartaGoals=" + spartaGoals + ", opponentGoals=" + opponentGoals + ", spartaShots=" + spartaShots + ", opponentShots=" + opponentShots + ", spartaPenalty=" + spartaPenalty + ", opponentPenalty=" + opponentPenalty + ", home=" + home + ", bestPlayer=" + bestPlayer + ", season=" + season + '}';
    }


    
    public void setId(int id) {
        this.id = id;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int getSpartaGoals() {
        return spartaGoals;
    }

    public void setSpartaGoals(int spartaGoals) {
        this.spartaGoals = spartaGoals;
    }

    public int getOpponentGoals() {
        return opponentGoals;
    }

    public void setOpponentGoals(int opponentGoals) {
        this.opponentGoals = opponentGoals;
    }

    public int getSpartaShots() {
        return spartaShots;
    }

    public void setSpartaShots(int spartaShots) {
        this.spartaShots = spartaShots;
    }

    public int getOpponentShots() {
        return opponentShots;
    }

    public void setOpponentShots(int opponentShots) {
        this.opponentShots = opponentShots;
    }

    public int getSpartaPenalty() {
        return spartaPenalty;
    }

    public void setSpartaPenalty(int spartaPenalty) {
        this.spartaPenalty = spartaPenalty;
    }

    public int getOpponentPenalty() {
        return opponentPenalty;
    }

    public void setOpponentPenalty(int opponentPenalty) {
        this.opponentPenalty = opponentPenalty;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getBestPlayer() {
        return bestPlayer;
    }

    public void setBestPlayer(String bestPlayer) {
        this.bestPlayer = bestPlayer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Match other = (Match) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
