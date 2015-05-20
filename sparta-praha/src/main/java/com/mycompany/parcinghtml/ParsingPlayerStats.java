/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author simon
 */
public class ParsingPlayerStats {
    
    private DataSource dataSource;
    public ParsingPlayerStats(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    private int tryParsing(String intToPars)
    {
        int result;
        try
        {
           result =  Integer.parseInt(intToPars);
        }catch (NumberFormatException ex)
        {
            result = 0;
        }
        return result;
    }
    
    public void downloadStats(int matchID, String url)
    {
       
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            if (doc == null)
            {
                System.out.println("doc is null");
            }
            
            Elements statsTable = doc.getElementById("tab_box_1").child(0).child(0).children();
            String sql_find_player = "SELECT PLAYERID FROM PLAYERS WHERE (PLAYERS.NAME || '') LIKE ?";
            String sql_insert_stats = "INSERT INTO STATS VALUES (?,?,?,?,?,?,?)";
            for (Element player : statsTable)
            {
                try (Connection conn = dataSource.getConnection())
                {
                    int playerID = 0;
                    try (PreparedStatement st = conn.prepareStatement(sql_find_player))
                    {
                        String [] names = player.child(0).text().split(" ");
                        if (names.length != 2) continue;
                        names[1] = names[1].substring(0, 1);
                        st.setString(1, "%"+names[1] +"%" + names[0] +"%");
                        
                        int i = 0;
                        ResultSet rs = st.executeQuery();
                        if (rs.next())
                        {
                            playerID = rs.getInt("PLAYERID");
                        }
                        
                    }
                    
                    try (PreparedStatement st = conn.prepareStatement(sql_insert_stats))
                    {
                        st.setInt(1, playerID);
                        st.setInt(2, matchID);
                        st.setInt(3, tryParsing(player.child(1).text()));
                        st.setInt(4, tryParsing(player.child(2).text()));
                        st.setInt(5, tryParsing(player.child(4).text()));
                        st.setInt(6, tryParsing(player.child(5).text()));
                        st.setInt(7, tryParsing(player.child(6).text()));
                        st.executeUpdate();
                    }
                }catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
                
                //System.out.println(player.child(0).text());
                //System.out.println(player.child(1).text());
            }
            
            
            
        
    }
    
}
