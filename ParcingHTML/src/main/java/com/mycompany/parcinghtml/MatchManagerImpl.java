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
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
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
            return executeQueryForMatches(st);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when getting players from database", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }     
    
}

    private List<Match> executeQueryForMatches(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Match> result = new ArrayList<Match>();
        while (rs.next()) {
            result.add(rowToMatch(rs));
        }
        return result;    
    }

    private Match rowToMatch(ResultSet rs) throws SQLException {
        Match m = new Match();
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
    public List<MatchesTeam> getMatchesOpponent(int year) throws RuntimeException {        
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT OPPONENT, sum(SPARTA_GOALS) as SPARTA_GOALS, sum(OPPONET_GOALS) as OPPONET_GOALS, sum(SPARTA_SHOTS) as SPARTA_SHOTS," +
                "    sum(OPPONET_SHOTS) as OPPONET_SHOTS, sum(SPARTA_PENALTY) as SPARTA_PENALTY, sum(OPPONET_PENALTY) as OPPONET_PENALTY from \"MATCH\"" +
                "    WHERE SEASON = ? " +
                "    GROUP BY  OPPONENT");
            st.setInt(1, year);
            ResultSet rs = st.executeQuery();
            List<MatchesTeam> result = new ArrayList<MatchesTeam>();
            while (rs.next()) {
                result.add(rowToMatchTeam(rs));
            }
            
            String SQL = null;
            String SQL2 = null;
            for (int i=0; i< result.size(); i++){
                String opponent = result.get(i).getOpponent();
                SQL = "SELECT COUNT(OPPONENT) from \"MATCH\"" +
                    "WHERE SEASON = " + year + "AND SPARTA_GOALS < OPPONET_GOALS AND OPPONENT = '" + opponent + "'";
                SQL2 = "SELECT COUNT(OPPONENT) from \"MATCH\"" +
                    "WHERE SEASON = " + year + "AND SPARTA_GOALS > OPPONET_GOALS AND OPPONENT = '" + opponent + "'";
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
    
    
    
    }
