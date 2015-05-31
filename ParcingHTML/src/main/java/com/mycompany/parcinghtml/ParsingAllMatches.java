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
public class ParsingAllMatches {
    
    private DataSource dataSource;
    
    public ParsingAllMatches(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    //Metoda prechadza  zapasy pomocou odkazov na podrobnejsie udaje
    //Parameter bere sezony, ktore sa maju prehladat 
    public void parsingMatches(int firstSeason, int lastSeason) throws IOException, SQLException {
        int matchID = 1;
        DatabaseManager db = new DatabaseManager(dataSource);
        ParsingMatch parse = new ParsingMatch(db);
        ParsingPlayerStats pps = new ParsingPlayerStats(dataSource);
        for (int i = lastSeason; i >= firstSeason; i--) {

            String url = "http://www.hcsparta.cz/zapas.asp?sezona=" + i;
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a.ico_report");
            for (Element link : links) {

                Match match = new Match();
                match.setId(matchID);
                match.setSeason(i);
                parse.parseMatch(link.attr("abs:href"), match);
                pps.downloadStats(matchID, link.attr("abs:href"));

                matchID++;
            }
        }
    }
}
