/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;


import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
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
        
        IdioticParsing idioticPars = new IdioticParsing(primaryDS);
        idioticPars.someParsing(2010,2014);
   
    }
   
}
