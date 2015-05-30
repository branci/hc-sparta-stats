/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.util.List;

/**
 *  
 * @author savco
 */
public interface PlayerManager {
    
    //Vrati vsetkych hracov aktualne ulozenych v tabulke Players
    //orderBy vybere atribut podla ktoreho budu hraci zoradeni
    //NAME, GOALS, ASSISTS, PENALTY_MINUTES, SHOTS, HITS su mozne hodnoty   
    //ascending true = od najmensej hodnoty po najvacsiu, false opacne
    //isPlayoff 0=playoff, 1=zakladna cast, 2=vsetko dokopy
    //Position 0=vsetci 1=obranca 2=utocnik 
    List<Player> getAllPlayers(int year, String orderBy, boolean ascending, int isPlayoff, int Position) throws RuntimeException;
    
    Player getPlayerInfo(Integer id,int year) throws RuntimeException;
    
    //To iste ako getAllplayers() akurat ze vrati len statistiky proti jednemu konkretnemu superovi
    List<Player> getAllPlayersVSTeams(String opponent,int year,String orderBy,boolean ascending, int isPlayoff, int Position) throws RuntimeException;
}
