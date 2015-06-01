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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author savco
 */
public class PlayerManagerImpl implements PlayerManager {
    
    private DataSource dataSource;
    
    private static final Logger logger = Logger.getLogger(
            PlayerManagerImpl.class.getName());

    
    public PlayerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        

    }
    public void initFunction(){
        ParsingClassPlayers psp = new ParsingClassPlayers(dataSource);
        try {      
            psp.downloadSource();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        ParsingAllMatches parsing = new ParsingAllMatches(dataSource);                

        try {
            parsing.parsingMatches(2010,2015);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(PlayerManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
                   
        static List<Player> executeQueryForMultiplePlayers(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Player> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowToPlayer(rs));
        }
       
        return result;
    }
        private static Player rowToPlayer(ResultSet rs) throws SQLException {
        Player result = new Player();
        result.setId(rs.getInt("PLAYERID"));
        result.setName(rs.getString("NAME"));
        result.setGoals(rs.getInt("GOALS"));
        result.setAssist(rs.getInt("ASSISTS")); 
        result.setPenalty(rs.getInt("PENALTY_MINUTES"));
        result.setShots(rs.getInt("SHOTS"));
        result.setHits(rs.getInt("HITS"));
        double d = (((double)result.getGoals())/result.getShots());
        result.setShotEffectivity((int)Math.round((d*100)));
        return result;
    } 

    @Override
    public List<Player> getAllPlayers(int year,String orderBy,boolean ascending, int isPlayoff , int position) throws RuntimeException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        String SQL = null;
        String playoff = null;
        switch (isPlayoff) {
            case 0: playoff = "AND PLAYOFF = 'playoff'";
                    break;
            case 1: playoff = "AND PLAYOFF IS NULL";
                    break;
            case 2: playoff = "";    
        }
        String pos = null;
        switch (position) {
            case 0: pos = "";
                    break;
            case 1: pos = "AND POSITION = 1";
                    break;
            case 2: pos = "AND POSITION = 2";                   
        }
        if (ascending) {
        SQL = "SELECT PLAYERID, players.NAME,sum(GOALS) as GOALS, sum(ASSISTS) as ASSISTS, sum(PENALTY_MINUTES) as PENALTY_MINUTES, sum(SHOTS) as SHOTS, sum(HITS) as HITS" +
"                            from STATS NATURAL INNER JOIN PLAYERS " +
"                            INNER JOIN \"MATCH\" ON match_id = matchid" +
"                            WHERE season = ? " + playoff + " " + pos + "" +
"                            GROUP BY NAME, PLAYERID ORDER BY " + orderBy + "";
        }
        else {
        SQL = "SELECT PLAYERID, players.NAME,sum(GOALS) as GOALS, sum(ASSISTS) as ASSISTS, sum(PENALTY_MINUTES) as PENALTY_MINUTES, sum(SHOTS) as SHOTS, sum(HITS) as HITS" +
"                            from STATS NATURAL INNER JOIN PLAYERS " +
"                            INNER JOIN \"MATCH\" ON match_id = matchid" +
"                            WHERE season = ?" +  playoff + " " + pos + "" +
"                            GROUP BY NAME, PLAYERID ORDER BY " + orderBy + " DESC";
        }
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement(SQL);
            st.setInt(1, year);          
            return executeQueryForMultiplePlayers(st);

        } catch (SQLException ex) {
            String msg = "Error when getting player with id from DB. Error in method getAllPlayers";
            logger.log(Level.SEVERE, msg, ex);
            throw new RuntimeException(msg, ex);            
        } finally {
            DBUtils.closeQuietly(conn, st);
        }             
    }
    
    private static Player rowToPlayerInfo(ResultSet rs) throws SQLException {
        Player result = new Player();
        result.setId(rs.getInt("PLAYERID"));
        result.setName(rs.getString("NAME"));
        result.setAge(rs.getInt("AGE"));
        result.setHeight(rs.getInt("HEIGHT")); 
        result.setWeight(rs.getInt("WEIGHT"));
        result.setPosition(rs.getInt("POSITION"));
        result.setPlayerNum(rs.getInt("PLAYERNUM"));
        return result;
    }

    @Override
    public Player getPlayerInfo(Integer id, int year) throws RuntimeException {
        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT PLAYERID, NAME, AGE, HEIGHT, WEIGHT, PLAYERNUM, POSITION FROM PLAYERS WHERE PLAYERID = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            Player result = new Player();
            while (rs.next()) {
                result = rowToPlayerInfo(rs);
            }
            
            String SQL = null;
            String SQL1 = null;
            String SQL3 = null;
            String SQL2 = null;            
                SQL = "SELECT COUNT(GOAL_ID) from GOAL NATURAL INNER JOIN \"MATCH\"  NATURAL INNER JOIN PLAYERS" +
"                    WHERE SEASON = " + year + " AND \"MINUTE\" < 21 AND PLAYERID = " + id + "";
                SQL1 = "SELECT COUNT(GOAL_ID) from GOAL NATURAL INNER JOIN \"MATCH\"  NATURAL INNER JOIN PLAYERS" +
"                    WHERE SEASON = " + year + " AND \"MINUTE\" < 41 AND \"MINUTE\" > 20 AND PLAYERID = " + id + "";
                SQL2 = "SELECT COUNT(GOAL_ID) from GOAL NATURAL INNER JOIN \"MATCH\"  NATURAL INNER JOIN PLAYERS" +
"                    WHERE SEASON = " + year + " AND \"MINUTE\" < 61 AND \"MINUTE\" > 40 AND PLAYERID = " + id + "";
                SQL3 = "SELECT COUNT(GOAL_ID) from GOAL NATURAL INNER JOIN \"MATCH\"  NATURAL INNER JOIN PLAYERS" +
"                    WHERE SEASON = " + year + " AND \"MINUTE\" > 60 AND PLAYERID = " + id + "";
               
                st = conn.prepareStatement(SQL);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.setFirstThird(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting goals in 1.third from DB");
                    throw new RuntimeException("Error when getting goals in 1.third from DB");
                }                
                st = conn.prepareStatement(SQL1);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.setSecondThird(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting goals in 2.third from DB");
                    throw new RuntimeException("Error when getting goals in 2.third from DB");
                }
                
                st = conn.prepareStatement(SQL2);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.setThirdThird(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting goals in 3.third from DB");
                    throw new RuntimeException("Error when getting goals in 3.third from DB");
                }
                
                st = conn.prepareStatement(SQL3);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.setExtraTime(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting goals in extra time from DB");
                    throw new RuntimeException("Error when getting goals in extra time from DB");
                }
                
                                    
            return result;
        } catch (SQLException ex) {
            String msg = "Error when getting player with id = " + id + " from DB. Error in method getPlayerInfo";
            logger.log(Level.SEVERE, msg, ex);
            throw new RuntimeException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }                
    }
   
    static Player executeQueryForSinglePlayer(PreparedStatement st) throws SQLException, RuntimeException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Player result = rowToPlayerInfo(rs);                
            if (rs.next()) {
                throw new RuntimeException(
                        "Internal integrity error: more players with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }
    
    @Override
    public List<Player> getAllPlayersVSTeams(String opponent,int year,String orderBy,boolean ascending,int isPlayoff, int position) throws RuntimeException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        String SQL = null;
        String playoff = null;
        switch (isPlayoff) {
            case 0: playoff = "AND PLAYOFF = 'playoff'";
                    break;
            case 1: playoff = "AND PLAYOFF IS NULL";
                    break;
            case 2: playoff = "";    
        }
        String pos = null;
        switch (position) {
            case 0: pos = "";
                    break;
            case 1: pos = "AND POSITION = 1";
                    break;
            case 2: pos = "AND POSITION = 2";                   
        }
        if (ascending) {
        SQL = "SELECT PLAYERID, PLAYERS.NAME,sum(GOALS) as GOALS, sum(ASSISTS) as ASSISTS, sum(PENALTY_MINUTES) as PENALTY_MINUTES, sum(SHOTS) as SHOTS, sum(HITS) as HITS" +
"                            from STATS NATURAL INNER JOIN PLAYERS " +
"                            INNER JOIN \"MATCH\" ON match_id = matchid" +
"                            WHERE season = ? AND OPPONENT ='" + opponent + "' " + playoff + " " + pos + "" +
"                            GROUP BY NAME, PLAYERID ORDER BY " + orderBy + "";
        }
        else {
        SQL = "SELECT PLAYERID, PLAYERS.NAME,sum(GOALS) as GOALS, sum(ASSISTS) as ASSISTS, sum(PENALTY_MINUTES) as PENALTY_MINUTES, sum(SHOTS) as SHOTS, sum(HITS) as HITS" +
"                            from STATS NATURAL INNER JOIN PLAYERS " +
"                            INNER JOIN \"MATCH\" ON match_id = matchid" +
"                            WHERE season = ? AND OPPONENT ='" + opponent + "' " + playoff + " " + pos + "" +
"                            GROUP BY NAME, PLAYERID ORDER BY " + orderBy + " DESC";
        }
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                   SQL);
            st.setInt(1, year);          
            return executeQueryForMultiplePlayers(st);
        } catch (SQLException ex) {
            String msg = "Error when getting player from DB. Error in method getAllPlayersVSTeams";
            logger.log(Level.SEVERE, msg, ex);
            throw new RuntimeException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }             
    }       

}
