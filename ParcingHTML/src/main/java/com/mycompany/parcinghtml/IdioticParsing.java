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
    
    public void someParsing(int firstSeason, int lastSeason) throws IOException, SQLException {
        int matchID = 1;
        DatabaseManager db = new DatabaseManager(dataSource);
        Parsing parse = new Parsing(db);
        ParsingPlayerStats pps = new ParsingPlayerStats(dataSource);
        for (int i = firstSeason; i <= lastSeason; i++) {

            String url = "http://www.hcsparta.cz/zapas.asp" + "?sezona=" + i;
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
