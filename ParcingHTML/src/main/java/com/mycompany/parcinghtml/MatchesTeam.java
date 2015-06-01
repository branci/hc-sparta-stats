/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author savco
 */
public class MatchesTeam {
    private int win;
    private int lose;
    private String opponent;
    
    private int spartaGoals;
    private int opponentGoals;
    private int spartaShots;
    private int opponentShots;
    private int spartaPenalty;
    private int opponentPenalty;
    private int season;

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getWin() {
        return win;
    }

    public int getLose() {
        return lose;
    }

    public String getOpponent() {
        return opponent;
    }

    public int getSpartaGoals() {
        return spartaGoals;
    }

    public int getOpponentGoals() {
        return opponentGoals;
    }

    public int getSpartaShots() {
        return spartaShots;
    }

    public int getOpponentShots() {
        return opponentShots;
    }

    public int getSpartaPenalty() {
        return spartaPenalty;
    }

    public int getOpponentPenalty() {
        return opponentPenalty;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setSpartaGoals(int spartaGoals) {
        this.spartaGoals = spartaGoals;
    }

    public void setOpponentGoals(int opponentGoals) {
        this.opponentGoals = opponentGoals;
    }

    public void setSpartaShots(int spartaShots) {
        this.spartaShots = spartaShots;
    }

    public void setOpponentShots(int opponentShots) {
        this.opponentShots = opponentShots;
    }

    public void setSpartaPenalty(int spartaPenalty) {
        this.spartaPenalty = spartaPenalty;
    }

    public void setOpponentPenalty(int opponentPenalty) {
        this.opponentPenalty = opponentPenalty;
    }

    @Override
    public String toString() {
        return "MatchesTeam{" + "win=" + win + ", lose=" + lose + ", opponent=" + opponent + ", spartaGoals=" + spartaGoals + ", opponentGoals=" + opponentGoals + ", spartaShots=" + spartaShots + ", opponentShots=" + opponentShots + ", spartaPenalty=" + spartaPenalty + ", opponentPenalty=" + opponentPenalty + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.opponent);
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
        final MatchesTeam other = (MatchesTeam) obj;
        if (!Objects.equals(this.opponent, other.opponent)) {
            return false;
        }
        return true;
    }
    
    
}
