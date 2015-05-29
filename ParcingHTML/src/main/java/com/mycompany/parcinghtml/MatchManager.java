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
    
    List<MatchesTeam> getMatchesOpponent(int year) throws RuntimeException;
    
}
