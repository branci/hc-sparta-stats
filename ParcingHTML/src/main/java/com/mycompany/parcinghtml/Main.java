/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.client.am.PreparedStatement;
import org.apache.derby.client.am.ResultSet;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/**
 *
 * @author kristian
 */
public class Main {
    
    
     public static DataSource dataSource() {
       
        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .addScript("classpath:SQLscript.sql")
                .build();
    } 
    
    public static void main(String[] args) throws SQLException, IOException{
        DataSource primaryDS = dataSource();       
        
        ParsingClassPlayers psp = new ParsingClassPlayers(primaryDS);
        psp.downloadSource();
        
        ParsingAllMatches parsing = new ParsingAllMatches(primaryDS);
        parsing.parsingMatches(2014,2014);
        Connection con = primaryDS.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement("SELECT * FROM \"MATCH\"");
         java.sql.ResultSet rs = ps.executeQuery();
         while(rs.next()){
          
          System.out.println(rs.getInt("season"));
         }
        MatchManagerImpl pl = new MatchManagerImpl(primaryDS);
        System.out.print(pl.getMatchesVSSingleOpponent(2014, "Chomutov", 1).get(0).toString());
    }
   
}
