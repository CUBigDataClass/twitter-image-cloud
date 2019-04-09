package com.bde.twitter_storm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcPreparedStatement;
import com.mysql.*;
import java.sql.Statement;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class SQLBolt extends BaseRichBolt {

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public void execute(Tuple input) {
        String entityName = (String) input.getValueByField("entityName");
        insertEntity(entityName);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public int insertEntity(String entityName) {

        Map<String, String> env = System.getenv();
        String user = env.get("BDE_SQL_USERNAME");
        String password = env.get("BDE_SQL_PASSWORD");

        String url = "jdbc:mysql://google/big-data-energy:us-central1:big-data-energy-mysql:3306/DEV?user="+ user + "&password=" + password + "&useSSL=false";

        //String url = "jdbc:mysql://big-data-energy:us-central1:big-data-energy-mysql:3306/DEV?user="+ user + "&password=" + password + "&useSSL=false";
        
        
        


        //check if entity is 
        String selectEntity = "SELECT * FROM ENTITIES WHERE ENTITY_NAME = " + entityName + ";";



        //entity table
        String sql = "INSERT INTO ENTITIES(ENTITY_ID, ENTITY_NAME, WIKI_URL) VALUES()";
        
        //tweet table: 
        //String sql = "INSERT INTO TWEETS(UNIQUE_ID, TWITTER_ID, ENTITY_ID, CREATED_DATETIME) VALUES()";
        Connection con = SQLConnect.con;
        try {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(selectEntity);
            
            if(rs.next()) {
                System.out.println(rs.getString("ENTITY_NAME"));
                if(rs.next()) {
                    System.out.println("****FUCK: TWO OR MORE ENTITY ROWS RETURNED ******");
                }
            }
            else {
                System.out.println("no results, need to insert new entity");
            }

            

            
            
        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcPreparedStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } 


        return -1;
    }

}