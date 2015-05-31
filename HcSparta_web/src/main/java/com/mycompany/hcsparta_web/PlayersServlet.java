/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hcsparta_web;

import com.mycompany.parcinghtml.MatchManager;
import com.mycompany.parcinghtml.MatchesTeam;
import com.mycompany.parcinghtml.Player;
import com.mycompany.parcinghtml.PlayerManager;
import com.mycompany.parcinghtml.PlayerManagerImpl;
import com.mycompany.parcinghtml.ServiceFailureException;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Branislav Smik <xsmik @fi.muni>
 */
@WebServlet( PlayersServlet.URL_MAPPING + "/*")
public class PlayersServlet extends HttpServlet {

    public static final String URL_MAPPING = "/players";
    private final static Logger log = LoggerFactory.getLogger(PlayersServlet.class);
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        showPlayersList(request, response, 2015, "NAME", true, 2, 0);
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
        String action = request.getPathInfo();
        request.setCharacterEncoding("utf-8");
        
        if (action.equals("/order")) {
            int season = Integer.parseInt(request.getParameter("seasonItem"));
            int position = Integer.parseInt(request.getParameter("positionItem"));
            int games = Integer.parseInt(request.getParameter("gamesItem"));
            String orderBy = request.getParameter("orderItem");
            boolean ascending =Boolean.parseBoolean(request.getParameter("ascItem"));
            
            showPlayersList(request, response, season, orderBy, ascending, games, position);
            
            return;
        } else if (action.equals("/player")) {
            int id = Integer.parseInt(request.getParameter("idItem"));
            
            showPlayerInfo(request, response, id, 2015);
            
            return;
        } else if (action.equals("/player/year")) {
            int id = Integer.parseInt(request.getParameter("idItem"));
            int season = Integer.parseInt(request.getParameter("seasonItem"));
            
            showPlayerInfo(request, response, id, season);
        }
    }
    
    private PlayerManager getPlayerManager() {
        return (PlayerManager) getServletContext().getAttribute("playerManager");
    }
    
    
    private void showPlayersList (HttpServletRequest request, HttpServletResponse response, int year, String orderBy, boolean ascending, int isPlayoff, Integer position) 
            throws ServletException, IOException {
        try {
            List<Player> result = getPlayerManager().getAllPlayers(year, orderBy, ascending, isPlayoff, position);
            request.setAttribute("players", result);
             request.getRequestDispatcher("/list.jsp").forward(request, response);

        } catch (ServiceFailureException ex) {
            log.error("cannot show players", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }  
    
    private void showPlayerInfo (HttpServletRequest request, HttpServletResponse response, Integer id, int year) 
            throws ServletException, IOException {
        try {
            Player result = getPlayerManager().getPlayerInfo(id, year);
            request.setAttribute("player", result);
            request.getRequestDispatcher("/list3.jsp").forward(request, response);

        } catch (ServiceFailureException ex) {
            log.error("cannot show player", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }     
}
