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
    List<Player> getAllPlayers(int year) throws RuntimeException;
    
    Player getPlayerInfo(Integer id) throws RuntimeException;
}
