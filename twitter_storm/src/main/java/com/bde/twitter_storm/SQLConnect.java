package com.bde.twitter_storm;

import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcStatement;

public class SQLConnect {

    public static Connection con;

    public static void setup() {

        Map<String, String> env = System.getenv();
        String user = env.get("BDE_SQL_USERNAME");
        String password = env.get("BDE_SQL_PASSWORD");

        String url = "jdbc:mysql://google/big-data-energy:us-central1:big-data-energy-mysql:3306/DEV?user=" + user
                + "&password=" + password + "&useSSL=false";

        try {

            con = DriverManager.getConnection(url, user, password);

            System.out.println("Connected!");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
}