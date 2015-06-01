/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import static com.mycompany.parcinghtml.PlayerManagerImpl.executeQueryForMultiplePlayers;
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
public class MatchManagerImpl implements MatchManager {

    private DataSource dataSource;
    
    private static final Logger logger = Logger.getLogger(
            MatchManagerImpl.class.getName());
    
    public MatchManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
        
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    //Kontrola spravnosti data source
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    @Override
    public List<Match> getMatches(int year) throws RuntimeException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT * from \"MATCH\"" +
                    "    WHERE SEASON = ? ");
            st.setInt(1, year);
            return executeQueryForMatches(st, 0);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when getting players from database", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }     
    
}

    /*
    postupne prechaza riadky ktore vrati prepared statment s databzi a uklada ich do listu
    argumentom rowVersion vyberam ktore stlpce ma zaujimaju. Prijatelne hodnoty su 0 alebo 1
    */
    private List<Match> executeQueryForMatches(PreparedStatement st, int rowVersion) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Match> result = new ArrayList<Match>();
        while (rs.next()) {
            switch (rowVersion) {
                case 0 : result.add(rowToMatch(rs));
                    break;
                case 1 : result.add(rowToMatch1(rs));
                    break;
                default : throw new IllegalArgumentException("Wrong paramter for executeQueryForMatches");    
            }        
        }
        return result;    
    }
    /*
    nacita riadok z tabulky a ulozi ho do instancie Match
    vracia prave hodnotu typu Match
    su 2. varianty tejto metody, vyberam na zaklade toho ktore hodnoty
    chcem s databaz precitat   
    */
    private Match rowToMatch(ResultSet rs) throws SQLException {
        Match m = new Match();
        m.setId(rs.getInt("MATCH_ID"));
        m.setOpponent(rs.getString("OPPONENT"));
        m.setOpponentGoals(rs.getInt("SPARTA_GOALS"));
        m.setOpponentPenalty(rs.getInt("OPPONET_PENALTY"));
        m.setOpponentShots(rs.getInt("OPPONET_SHOTS"));
        m.setSpartaGoals(rs.getInt("SPARTA_GOALS"));
        m.setSpartaPenalty(rs.getInt("SPARTA_PENALTY"));
        m.setSpartaShots(rs.getInt("SPARTA_SHOTS"));
        return m;
        
    }

    @Override
    public List<MatchesTeam> getMatchesOpponent(int year, int isPlayoff) throws RuntimeException {        
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        String playoff = null;
        switch (isPlayoff) {
            case 0: playoff = "AND PLAYOFF = 'playoff'";
                    break;
            case 1: playoff = "AND PLAYOFF IS NULL";
                    break;
            case 2: playoff = "";    
        }
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT OPPONENT, sum(SPARTA_GOALS) as SPARTA_GOALS, sum(OPPONET_GOALS) as OPPONET_GOALS, sum(SPARTA_SHOTS) as SPARTA_SHOTS," +
                "    sum(OPPONET_SHOTS) as OPPONET_SHOTS, sum(SPARTA_PENALTY) as SPARTA_PENALTY, sum(OPPONET_PENALTY) as OPPONET_PENALTY from \"MATCH\"" +
                "    WHERE SEASON = ? " + playoff + ""  +
                "    GROUP BY  OPPONENT");
            st.setInt(1, year);
            ResultSet rs = st.executeQuery();
            List<MatchesTeam> result = new ArrayList<MatchesTeam>();
            while (rs.next()) {
                MatchesTeam m = rowToMatchTeam(rs);
                m.setSeason(year);
                result.add(m);
            }
            
            String SQL = null;
            String SQL2 = null;
            for (int i=0; i< result.size(); i++){
                String opponent = result.get(i).getOpponent();
                SQL = "SELECT COUNT(OPPONENT) from \"MATCH\"" +
                    "WHERE SEASON = " + year + "AND SPARTA_GOALS > OPPONET_GOALS AND OPPONENT = '" + opponent + "' " + playoff + "";
                SQL2 = "SELECT COUNT(OPPONENT) from \"MATCH\"" +
                    "WHERE SEASON = " + year + "AND SPARTA_GOALS < OPPONET_GOALS AND OPPONENT = '" + opponent + "' " + playoff + "";
                st = conn.prepareStatement(SQL);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.get(i).setWin(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting wins from DB");
                    throw new RuntimeException("Error when getting wins from DB");
                }
                
                st = conn.prepareStatement(SQL2);
                rs = st.executeQuery();
                if (rs.next()) {
                    result.get(i).setLose(rs.getInt(1));
                } else {
                    logger.log(Level.SEVERE, "Error when getting lose from DB");
                    throw new RuntimeException("Error when getting lose from DB");
                }
                
            }                           
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException("Error when getting players from database", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }     
        
        
    }

    /*
    //nacita riadok z tabulky a ulozi ho do instancie MatchTeam
    //vracia prave hodnotu typu MatchTeam
    */
    private MatchesTeam rowToMatchTeam(ResultSet rs) throws SQLException{
        MatchesTeam team = new MatchesTeam();
        team.setOpponent(rs.getString("OPPONENT"));
        team.setOpponentGoals(rs.getInt("OPPONET_GOALS"));
        team.setOpponentPenalty(rs.getInt("OPPONET_PENALTY"));
        team.setOpponentShots(rs.getInt("OPPONET_SHOTS"));
        team.setSpartaGoals(rs.getInt("SPARTA_GOALS"));
        team.setSpartaPenalty(rs.getInt("SPARTA_PENALTY"));
        team.setSpartaShots(rs.getInt("SPARTA_SHOTS"));
        return team;
        
    }

    @Override
    public List<Match> getMatchesVSSingleOpponent(int year, String opponent, int isPlayoff) {
                checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        String playoff = null;        
        switch (isPlayoff) {
            case 0: playoff = "AND PLAYOFF = 'playoff'";
                    break;
            case 1: playoff = "AND PLAYOFF IS NULL";
                    break;
            case 2: playoff = "";    
        }
        try{
            conn = dataSource.getConnection();
            String SQL = "SELECT OPPONENT,DATE,SPARTA_GOALS, OPPONET_GOALS, BEST_PLAYER, HOME,SEASON from \"MATCH\"" +
                    "    WHERE SEASON = ? AND OPPONENT = '" + opponent + "' " + playoff + "";
            st = conn.prepareStatement(SQL);                    
            st.setInt(1, year);
            
            return executeQueryForMatches(st, 1);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when getting matches from database. Method getMatchesVSSingleOpponent");
            throw new RuntimeException("Error when getting matches from database. Method getMatchesVSSingleOpponent", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    //nacita riadok z tabulky a ulozi ho do instancie Match
    //vracia prave hodnotu typu Match
    private Match rowToMatch1(ResultSet rs) throws SQLException{
        Match m = new Match();
        m.setOpponent(rs.getString("OPPONENT"));
        m.setDate(rs.getDate("DATE"));       
        m.setSpartaGoals(rs.getInt("SPARTA_GOALS"));
        m.setOpponentGoals(rs.getInt("OPPONET_GOALS"));        
        m.setBestPlayer(rs.getString("BEST_PLAYER"));
        m.setHome(rs.getString("HOME"));
        m.setSeason(rs.getInt("SEASON"));
        return m;    
    }
     
   
    
    }
