/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.io.IOException;
import static java.lang.Character.isUpperCase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jsoup.nodes.Element;

/**
 *
 * @author kristian
 */
public class Parsing {
    private final DatabaseManager db;
    private int goalID = 1;
    private int assistID = 1;
    private ArrayList<String> playerNames;
            
    public Parsing(DatabaseManager db){
        this.db = db;
    }
    
    public ArrayList<String> getAllNames(Document doc)
    {
        ArrayList<String> apn = new ArrayList<String>();
        Elements statsTable = doc.getElementById("tab_box_1").child(0).child(0).children();
        for (Element player: statsTable)
        {   
            //System.out.println(player.child(0).text());
            String [] names = player.child(0).text().split(" ");
            if (names.length != 2) continue;
            apn.add(player.child(0).text());
        }
        return apn;       
    }
    
    public void parseDate(String date,Match match){
        String[] array = date.split(",");
        
        array = array[1].split(" ");
        
        array = array[2].split("\\.");
        java.sql.Date finalDate =  java.sql.Date.valueOf(array[2].substring(0, 4) + "-" + array[1] + "-" + array[0]); 
        match.setDate(finalDate);
    }
    
    public void parseMatch(String url, Match match) throws IOException, SQLException{

        Document doc = Jsoup.connect(url).get();
        Elements gameDate = doc.select("div.game_date > span");
        //Ziskanie datumu zapasu
        parseDate(gameDate.get(0).text(), match);
        playerNames = getAllNames(doc);
        
        Elements teams = doc.select("div.game_box");
        String gameDetails = doc.select("div.game_details").get(0).text();
        
        //Rozhoduje sa, ktore elementy sa budu parsovat pre koho. Dovod: Dva typy html, ked Sparta je doma, a ked je host 
        if (teams.get(0).text().contains("Sparta")) {
            //Ziskanie pocet golov Sparty a supisku zapasu
            parseSparta(teams.get(0), match);
            //Ziskanie pocet golov protihraca a nazov protihraca
            parseOpponent(teams.get(1), match);
            match.setHome("domaci");
            //Ziskanie poctu trestnych minut 
            match.setSpartaPenalty(Integer.parseInt(gameDetails.substring(gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length(), gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length())).replace(" ", "")));
            match.setOpponentPenalty(Integer.parseInt(gameDetails.substring(gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length()) + 1, gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length()) + 3).replace(",","").replace(".", "").replace(" ","")));
            //Ziskanie poctu striel
            match.setSpartaShots(Integer.parseInt(gameDetails.substring(gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length(), gameDetails.indexOf(":",gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length()))));
            match.setOpponentShots(Integer.parseInt(gameDetails.substring( gameDetails.indexOf(":",gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length()) + 1,  gameDetails.indexOf(" ", gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length())).replace(".", "")));
            //Ziskanie najlepsieho hraca zapasu
            if(gameDetails.contains("Nejlepší hráči zápasu: "))
                match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("Nejlepší hráči zápasu: ") + "Nejlepší hráči zápasu: ".length(), gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráči zápasu: ") + "Nejlepší hráči zápasu: ".length())).replace(" ", ""));
            if(gameDetails.contains("Nejlepší hráči utkání: "))
                match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("Nejlepší hráči utkání: ") + "Nejlepší hráči utkání: ".length(), gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráči utkání: ") + "Nejlepší hráči utkání: ".length())).replace(" ", ""));
            if(gameDetails.contains("Nejlepší hráč utkání: "))
                match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("Nejlepší hráč utkání: ") + "Nejlepší hráč utkání: ".length(), gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráč utkání: ") + "Nejlepší hráč utkání: ".length())).replace(" ", ""));
        } else {
            parseSparta(teams.get(1), match);
            parseOpponent(teams.get(0), match);
            match.setHome("hostia");
            
            match.setOpponentPenalty(Integer.parseInt(gameDetails.substring(gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length(), gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length()))));
            match.setSpartaPenalty(Integer.parseInt(gameDetails.substring(gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length()) + 1, gameDetails.indexOf(":",gameDetails.indexOf("Vyloučení: ") + "Vyloučení: ".length()) + 3).replace(",","").replace(".", "")));
            
            match.setOpponentShots(Integer.parseInt(gameDetails.substring(gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length(), gameDetails.indexOf(":",gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length())).replace(" ", "")));
            match.setSpartaShots(Integer.parseInt(gameDetails.substring( gameDetails.indexOf(":",gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length()) + 1,  gameDetails.indexOf(" ", gameDetails.indexOf("Střely na branku: ") + "Střely na branku: ".length())).replace(".", "").replace(" ", "")));
            
            if(gameDetails.contains("Nejlepší hráči zápasu: "))
                match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráči zápasu: ") + "Nejlepší hráči zápasu: ".length()) + 1).replace(" ", "").replace(".",""));
            if(gameDetails.contains("Nejlepší hráči utkání: "))
                match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráči utkání: ") + "Nejlepší hráči utkání: ".length()) + 1).replace(" ", "").replace(".",""));
            if(gameDetails.contains("Nejlepší hráč utkání: "))
               match.setBestPlayer(gameDetails.substring(gameDetails.indexOf("-", gameDetails.indexOf("Nejlepší hráč utkání: ") + "Nejlepší hráč utkání: ".length()) + 1).replace(" ", "").replace(".",""));
        }
        db.addMatch(match);
        System.out.println(match);
        if(match.getSpartaGoals() != 0)
           parseGoals(gameDetails.substring(gameDetails.indexOf("Branky a nahrávky: ") + "Branky a nahrávky: ".length(), gameDetails.indexOf("Rozhodčí:") - 2), match);
    }
    
    
    private int getIdOfPlayer(String name)
    {
        int playerID = 0;
        String fullName = "";
        String sql_find_player = "SELECT PLAYERID FROM PLAYERS WHERE (PLAYERS.NAME || '') LIKE ?";

        for (String pname: playerNames)
        {
            if (pname.contains(name)) fullName=pname; 
        }
        if (fullName.equals("")) return playerID;
        try (Connection conn = db.getDataSource().getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql_find_player))
            {
                
               String [] names = fullName.split(" ");
                names[1] = names[1].substring(0, 1);
                ps.setString(1, "%"+names[1] +"%" + names[0] +"%");
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    playerID = rs.getInt("PLAYERID");
                }
                
            }
        }catch(SQLException ex)
        {
            System.out.println("chyba pri hladani hraca");
        }
        return playerID;
    }
    
    public void parseGoals(String text, Match match) throws SQLException{

        if(match.getOpponentGoals() != 0){
            String[] firstParse = text.split(" – ");
            if( firstParse.length  != 2)
                firstParse = text.split("-");

            if(match.getHome().equals("domaci"))
                text = firstParse[0];
            else
                text = firstParse[1];
        }
        Goal goal = null;
        Assist assist = null;
        String[] goals = text.split(" ");
        String tmp = "";
        
        for(String part : goals){
            if(part.length() == 0) continue;
            part = part.replace(" ", "");
            
            if(part.matches(".*\\d+.")){
                goal = new Goal();
                goal.setId(goalID);
                goal.setMinit(Integer.parseInt(part.replace(".", "")));             
                goal.setMatch(match.getId());
                goalID++;
                continue;
            }
            if(part.contains(".") && part.length() == 2){
                tmp = " " + part;
                //System.out.println("CAST: "+ tmp);
                continue;
            }
                
            if(part.contains("(") || part.contains(")")){
                assist = new Assist();
                assist.setId(assistID);
                assist.setGoal(goal.getId());
                assist.setMatch(match.getId());
                assist.setPlayer(part.replace(")", "").replace(",","").replace("(", "") + tmp);
                tmp = ""; 
                assist.setPlayerID(getIdOfPlayer(assist.getPlayer()));
                db.addAssist(assist);
                if(assist.getPlayerID() == 0) System.out.println(assist);
                //System.out.println(assist);
                assistID++;
                continue;
            }
            if(!isUpperCase((int) part.charAt(0))) continue;
            
            goal.setPlayer(part.replace(",", "") + tmp);
            goal.setPlayerID(getIdOfPlayer(goal.getPlayer()));
            db.addGoal(goal);
            if(goal.getPlayerID() == 0) System.out.println(goal);
             //System.out.println(goal);
            tmp = "";
        }
    }
    
    
    public void parseOpponent(Element opponent, Match match){
        
        int goals = Integer.parseInt(opponent.child(1).text());
        match.setOpponentGoals(goals);       
        match.setOpponent(opponent.child(2).child(0).text().replace(":", ""));
    }
    
    public void parseSparta(Element sparta, Match match) throws SQLException{
        
        int goals = Integer.parseInt(sparta.child(1).text());
        match.setSpartaGoals(goals);
        System.out.println(sparta.child(2).text().indexOf("Trenér:") - 1);
        parsePlayers(sparta.child(2).text().substring(sparta.child(2).text().indexOf(":") + 1, sparta.child(2).text().indexOf("Trenér") - 1),match.getId());
    }
    
    public void parsePlayers(String text, int id) throws SQLException{
   
        String[] firstParcing;
        firstParcing = text.split("–");
        for(String first : firstParcing){  
            String[] secondParcing = first.split("-");
            for(String part : secondParcing){
              String[] player = part.split(",");
              String tmp = "";
              for(String p : player){
                     p = p.replace(" ","");
                     p = p.replace("(A)", "");
                     p = p.replace("(C)", "");
                     if(p.contains(".") && p.length() == 2){
                         tmp = ", ";
                         continue;
                     }
                     String[] ret;
                     ret = p.split("\\(");
                     ret[0] = ret[0].replace(".","");
                     if(ret[0].length() == 0) continue;
                     Played played = new Played();
                     played.setMatch(id);
                     played.setPlayer(ret[0] + tmp);
                     tmp = "";
                     played.setPlayerID(getIdOfPlayer(played.getPlayer()));
                     db.addPlayed(played);

                }       
            }
        }
       
    }        
}
