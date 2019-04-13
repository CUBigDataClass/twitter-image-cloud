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
        System.out.println("SQL USER: " + user);
        System.out.println("SQL PASS: " + password);

        String url = "jdbc:mysql://google/DEV?cloudSqlInstance=big-data-energy:us-central1:big-data-energy-mysql&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user="
                + user + "&password=" + password;

        // String url =
        // "jdbc:mysql://google/big-data-energy:us-central1:big-data-energy-mysql:3306/DEV?user="
        // + user + "&password=" + password + "&useSSL=false";

        try {

            con = DriverManager.getConnection(url, user, password);

            System.out.println("Connected!");

            //log the schema
            ResultSet resultSet = con.getMetaData().getCatalogs();

            String[] types = { "TABLE" };
            resultSet = con.getMetaData().getTables("DEV", null, "%", types);
            String tableName = "";
            while (resultSet.next()) {
                tableName = resultSet.getString(3);
                System.out.println("Table Name = " + tableName);
            }
            resultSet.close();
            // --- LISTING DATABASE COLUMN NAMES ---
            DatabaseMetaData meta = con.getMetaData();
            resultSet = meta.getColumns("DEV", null, "ENTITIES", "%");
            while (resultSet.next()) {
                System.out.println("Column Name of table " + "ENTITIES" + " = " + resultSet.getString(4));
            }


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
}