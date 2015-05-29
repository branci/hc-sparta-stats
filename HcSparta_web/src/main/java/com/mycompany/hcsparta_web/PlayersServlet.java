/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hcsparta_web;

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
        showPlayersList(request, response);
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
        /*
        if (action.equals("/add")) {
            
            String fullName = request.getParameter("fullName");
            
            String idCard = request.getParameter("idCard");
            
            if (fullName == null || fullName.length() == 0 || (request.getParameter("birthDate")).isEmpty() || !isValidDate(request.getParameter("birthDate")) || idCard == null || idCard.length() == 0 || idCard.isEmpty()) {
                request.setAttribute("fail", "All attributes must be filled!");
                showCustomersList(request, response);
                return;
            }
            Date birthDate = date(request.getParameter("birthDate"));
            
            try {
                Customer customer = new Customer();
                customer.setFullName(fullName);
                customer.setBirthDate(birthDate);
                customer.setIdCard(idCard);
                getCustomerManager().createCustomer(customer);
                log.debug("created {}",customer);
                response.sendRedirect(request.getContextPath()+ "/customers");
                return;
            } catch (ServiceFailureException ex) {
                log.error("Cannot add customer", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                return;
            }
        } else if (action.equals("/delete")) {
            try {
                Long id = Long.valueOf(request.getParameter("id"));
                getCustomerManager().deleteCustomer(getCustomerManager().getCustomerById(id));
                log.debug("deleted customer {}",id);
                response.sendRedirect(request.getContextPath()+"/customers");
                return;
            } catch (ServiceFailureException ex) {
                log.error("Cannot delete customer", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                return;
            } 
        } else if (action.equals("/update")) {
            try {
                Long id = Long.valueOf(request.getParameter("id"));
                Customer customer = getCustomerManager().getCustomerById(id);
                
                String fullName = request.getParameter("fullName");
                Date birthDate = date(request.getParameter("birthDate"));
                String idCard = request.getParameter("idCard");
                
                customer.setFullName(fullName);
                customer.setBirthDate(birthDate);
                customer.setIdCard(idCard);
                
                getCustomerManager().updateCustomer(customer);
                log.debug("updated customer {}", id);
                response.sendRedirect(request.getContextPath()+"/customers");
            } catch (ServiceFailureException ex) {
                log.error("Cannot update customer", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                return;
            } 
        } else {
            log.error("Unknown action" + action);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action );
        }
        */   
    }

    
    private java.sql.Date date(String date) {
        return Date.valueOf(date);
    }
    
    private PlayerManager getPlayerManager() {
        return (PlayerManager) getServletContext().getAttribute("playerManager");
    }
    
    private void showPlayersList (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
             request.setAttribute("players", getPlayerManager().getAllPlayers(2015, "NAME", true));
             request.getRequestDispatcher("/list.jsp").forward(request, response);

        } catch (ServiceFailureException ex) {
            log.error("cannot show players", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    
  
}
