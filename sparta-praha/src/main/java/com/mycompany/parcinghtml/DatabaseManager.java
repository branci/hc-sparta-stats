/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parcinghtml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/*
 * @author kristian
 */
public class DatabaseManager {
    
    private final DataSource dataSource;
    
        
    
    public DatabaseManager(DataSource dataSource) throws SQLException{
         
            this.dataSource = dataSource;
     
    }
    

    public void addMatch(Match match) throws SQLException {
        String sql = "INSERT INTO MATCHS(ID,DATE,OPPONENT,SPARTA_GOALS,OPPONET_GOALS,SPARTA_SHOTS,OPPONET_SHOTS,SPARTA_PENALTY,OPPONET_PENALTY,HOME,BEST_PLAYER) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
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
                st.executeUpdate();
                conn.commit();
            }
        }
    }
    public void addPlayed(Played played) throws SQLException {
        String sql = "INSERT INTO PLAYED(MATCHS,PLAYER) VALUES(?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, played.getMatch());
                st.setString(2,  played.getPlayer());             
                st.executeUpdate();
                conn.commit();
            }
        }
    }
    
    public void addGoal(Goal goal) throws SQLException {
        String sql = "INSERT INTO GOAL(ID,MATCHS,PLAYER, MINIT) VALUES(?,?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                
                st.setInt(1, goal.getId());
                st.setInt(2, goal.getMatch());
                st.setString(3, goal.getPlayer());
                st.setInt(4, goal.getMinit());
                st.executeUpdate();
                conn.commit();
            }
        }
    }
    public void addAssist(Assist assist) throws SQLException {
        String sql = "INSERT INTO ASSIST(ID,MATCHS,PLAYER,GOAL) VALUES(?,?,?,?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, assist.getId());
                st.setInt(2, assist.getMatch());
                st.setString(3, assist.getPlayer());
                st.setInt(4, assist.getGoal());
                st.executeUpdate();
                conn.commit();
            }
        }
    }
    
}
