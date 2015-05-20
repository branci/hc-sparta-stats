/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author simon
 */
public class IdioticParsing {
    
    private DataSource dataSource;
    
    public IdioticParsing(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    
    public void someParsing() throws IOException, SQLException
    {
        int matchID = 1;

        
        String url = "http://www.hcsparta.cz/zapas.asp";
        
        
        
       
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a.ico_report");
        DatabaseManager db = new DatabaseManager(dataSource);
        Parsing parse = new Parsing(db);
        ParsingPlayerStats pps = new ParsingPlayerStats(dataSource);
        for(Element link : links){

            Match match = new Match();
            match.setId(matchID);
            parse.parseMatch(link.attr("abs:href"), match);
            db.addMatch(match);
            //len ciastocne osetrenie nacitavania jednej sutaze
            pps.downloadStats(matchID, link.attr("abs:href"));
           //System.out.println("INSERT INTO" + match.toString());
            if(matchID == 21) return;        
            matchID++;
        }
        
    }
            
    
}
