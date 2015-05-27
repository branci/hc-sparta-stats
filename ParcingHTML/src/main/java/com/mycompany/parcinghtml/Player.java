/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

/**
 *
 * @author savco
 */
public class Player {
    private Integer id;
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
    private Integer playerNum;
    private Integer position;
    
    private Integer goals;
    private Integer assist;
    private Integer penalty;
    private Integer shots;
    private Integer hits;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getPlayerNum() {
        return playerNum;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getGoals() {
        return goals;
    }

    public Integer getAssist() {
        return assist;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public Integer getShots() {
        return shots;
    }

    public Integer getHits() {
        return hits;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public void setAssist(Integer assist) {
        this.assist = assist;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public void setShots(Integer shots) {
        this.shots = shots;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }
    
    
    

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setPlayerNum(Integer playerNum) {
        this.playerNum = playerNum;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", goals=" + goals + ", assist=" + assist + ", penalty=" + penalty + ", shots=" + shots + ", hits=" + hits + '}';
    }
    
    public String toStringInfo() {
        return "Player{" + "name=" + name + ", age=" + age + ", weight=" + weight + ", height=" + height + ", position=" + position + ", playernum=" + playerNum + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
}
