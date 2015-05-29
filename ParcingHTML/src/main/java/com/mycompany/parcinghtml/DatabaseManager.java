/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.sql.DataSource;

/*
 * @author kristian
 */
//Trieda sluzi na ukladanie dat do databazy
public class DatabaseManager {
    
    private final DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
    final static Logger log = Logger.getLogger(DatabaseManager.class.getName());
        
    
    public DatabaseManager(DataSource dataSource) throws SQLException{
            this.dataSource = dataSource;
             configureLogger();
    }
    private void configureLogger(){
        FileHandler fh;
        log.setUseParentHandlers(false);
        try {
            fh = new FileHandler("Database_logger.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            System.out.println("Handler failed");
        }
    }
    public void addMatch(Match match) throws SQLException {
        String sql = "INSERT INTO \"MATCH\"(MATCH_ID,DATE,OPPONENT,SPARTA_GOALS,OPPONET_GOALS,SPARTA_SHOTS,OPPONET_SHOTS,SPARTA_PENALTY,OPPONET_PENALTY,HOME,BEST_PLAYER,PLAYOFF,SEASON) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, match.getId());
                st.setDate(2,  match.getDate());
                st.setString(3, match.getOpponent());
                st.setInt(4,  match.getSpartaGoals());
                st.setInt(5, match.getOpponentGoals());
                st.setInt(6,  match.getSpartaShots());
                st.setInt(7, match.getOpponentShots());
                st.setInt(8,  match.getSpartaPenalty());
                st.setInt(9, match.getOpponentPenalty());
                st.setString(10,  match.getHome());
                st.setString(11, match.getBestPlayer());             
                st.setString(12,match.getPlayoff());
                st.setInt(13, match.getSeason());
                st.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Cannot add match: " + match.toString(), ex);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "db connection problem", ex);
        }
    }
    public void addPlayed(Played played) throws SQLException {
        String sql = "INSERT INTO PLAYED(MATCH_ID,PLAYER_NAME, PLAYERID) VALUES(?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, played.getMatch());
                st.setString(2,  played.getPlayer()); 
                st.setInt(3, played.getPlayerID());
                st.executeUpdate();
                conn.commit();
            }catch(SQLException ex){
                log.log(Level.SEVERE, "Cannot add played: " + played.toString(), ex);
            }
        }catch(SQLException ex){
            log.log(Level.SEVERE, "db connection problem", ex);
        }           
    }
    
    public void addGoal(Goal goal) throws SQLException {
        String sql = "INSERT INTO GOAL(GOAL_ID,MATCH_ID,PLAYER_NAME, \"MINUTE\", PLAYERID) VALUES(?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {

                st.setInt(1, goal.getId());
                st.setInt(2, goal.getMatch());
                st.setString(3, goal.getPlayer());
                st.setInt(4, goal.getMinit());
                st.setInt(5, goal.getPlayerID());
                st.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Cannot add goal: " + goal.toString(), ex);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "db connection problem", ex);
        }
}
public void addAssist(Assist assist) throws SQLException {
        String sql = "INSERT INTO ASSIST(ASSIST_ID,MATCH_ID,PLAYER_NAME,GOAL_ID, PLAYERID) VALUES(?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, assist.getId());
                st.setInt(2, assist.getMatch());
                st.setString(3, assist.getPlayer());
                st.setInt(4, assist.getGoal());
                st.setInt(5, assist.getPlayerID());
                st.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Cannot add assist: " + assist.toString(), ex);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "db connection problem", ex);
        }
    }

    
    
}
