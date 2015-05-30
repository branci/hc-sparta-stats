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
public interface MatchManager {   
    
    //return all matches
    List<Match> getMatches(int year) throws RuntimeException;
    
    //vrati zapasi proti jednotlivym druzstvam
    //isPlayoff 0=playoff, 1=zakladna cast, 2=vsetko dokopy
    List<MatchesTeam> getMatchesOpponent(int year, int isPlayoff) throws RuntimeException;
    
    //Vrati jednotlive zapasi proti zvolenemu druzstvu
    List<Match> getMatchesVSSingleOpponent(int year, String opponent, int isPlayoff);
}
