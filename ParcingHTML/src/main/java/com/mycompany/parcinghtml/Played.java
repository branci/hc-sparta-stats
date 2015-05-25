/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

/**
 *
 * @author kristian
 */
public class Played {
    private int match;
    private String player;

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.match;
        hash = 37 * hash + this.player.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "Played{" + "match=" + match + ", player=" + player + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Played other = (Played) obj;
        if (this.match != other.match) {
            return false;
        }
        if (this.player != other.player) {
            return false;
        }
        return true;
    }
    
}
