/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sparta.praha;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/**
 *
 * @author simon
 */
public class ParsingClassPlayers {
   
    //private DataSource ds;

    private static DataSource prepareDataSource()  {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby://localhost:1527/SPARTA");
        return ds;
    }
    
    public DataSource dataSource() {
       
        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .addScript("classpath:createTable.sql")
                .build();
    }
    private final DataSource ds = dataSource();
    
    public DataSource getDS()
    {
        return ds;
    }
           
    public void downloadSource() throws SQLException {
        //ds = prepareDataSource();
        String sql = "INSERT INTO PLAYERS(NAME,AGE,HEIGHT,WEIGHT,PLAYERNUM,POSITION) VALUES(?,?,?,?,?,?)";
        ArrayList<String> duplicity = new ArrayList<>();

        
        
        for (int i = 2015; i > 2004; i--) {
            
            Document doc = null;
            try 
            {
                doc = Jsoup.connect("http://www.hcsparta.cz/soupiska.asp?sezona=" + Integer.toString(i)).get();
            } catch (IOException e) 
            {
                System.out.println(e.getMessage());
            }
            if (doc==null)
            {
                System.out.println("doc is null");
                return;
            }
            
            Elements posNum;
            Elements elList;
            posNum = doc.getElementsByAttributeValueContaining("class", "soupiska");
            //elList = doc.getElementsByAttributeValueContaining("id", "soupiska");
            for (int j = 0; j<3; j++)
            {
                elList = posNum.get(j).getElementsByAttributeValueContaining("id", "soupiska");
                
                for (Element item : elList) 
                {
                    String [] secondName = item.child(2).text().split(" ");
                    if (duplicity.contains(item.child(2).text())) continue;
                    duplicity.add(item.child(2).text());
                    try (Connection conn = ds.getConnection()) 
                    {
                        
                        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
                        {
                            st.setString(1, item.child(2).text());
                            String[] age = item.child(4).text().split(" ");
                            st.setInt(2, Integer.parseInt(age[0]));
                            String[] height = item.child(5).text().split(" ");
                            st.setInt(3, Integer.parseInt(height[0]));
                            String[] weight = item.child(6).text().split(" ");
                            st.setInt(4, Integer.parseInt(weight[0]));
                            
                                      
                            try
                            {
                                st.setInt(5, Integer.parseInt(item.child(0).text()));
                            }catch(NumberFormatException ex)
                            {
                                st.setInt(5, 0);
                            }
                            st.setInt(6, j);
                            int addedRows = st.executeUpdate();
                            //System.out.println(item.child(2).text() + " " + item.child(4).text() + " " + item.child(5).text() + " " + item.child(6).text());
                        }
                    }catch (SQLException ex)
                    {
                       throw new SQLException (ex.getMessage(), ex.fillInStackTrace());
                    }
                    
                    
                    
                    
                }
            }
            

        }
        
        

    }



    
}
