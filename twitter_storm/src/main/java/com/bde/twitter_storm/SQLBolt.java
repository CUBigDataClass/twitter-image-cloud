package com.bde.twitter_storm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcPreparedStatement;
import java.sql.Timestamp;

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
        String entityName = input.getStringByField("entityName");
        String wikiUrl = input.getStringByField("wikiURL");
        String imageUrl = input.getStringByField("wikiImageURL");
        String twitterID = input.getStringByField("tweetID");
        Date tweetDate = (Date) input.getValueByField("tweetCreatedAt");
        int entityID = insertEntity(entityName, wikiUrl, imageUrl);
        insertTweet(twitterID, entityID, tweetDate);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public void insertTweet(String twitterID, int entityID, Date date) {
        try {
            SQLConnect.insertTweetStatement.setString(1, twitterID);
            SQLConnect.insertTweetStatement.setInt(2, entityID);
            SQLConnect.insertTweetStatement.setTimestamp(3, new Timestamp(date.getTime()));
            SQLConnect.insertTweetStatement.executeUpdate();
            System.out.println("Great success inserting tweet!");
        } catch(SQLException e) {
            System.out.println("ERROR SQLing");
        }
    }

    public int insertEntity(String entityName, String wikiUrl, String imageUrl) {

        try {
            SQLConnect.selectEntityStatement.setString(1, entityName);
            ResultSet rs = SQLConnect.selectEntityStatement.executeQuery();
            
            if(rs.next()) {
                if(rs.next()) {
                    System.out.println("****ERROR: TWO OR MORE ENTITY ROWS RETURNED ******");
                }
                return rs.getInt("ENTITY_ID");
            }
            else {
                System.out.println("no results for: " + entityName + ". Inserting new entity");
                SQLConnect.insertEntityStatement.setString(1, entityName);
                SQLConnect.insertEntityStatement.setString(2, wikiUrl);
                SQLConnect.insertEntityStatement.setString(3, imageUrl);
                SQLConnect.insertEntityStatement.executeUpdate();
                ResultSet insertResult = SQLConnect.insertEntityStatement.getGeneratedKeys();
                long key = -1L;
                if (insertResult.next()) {
                   key = insertResult.getLong(1);
                } else {
                    System.out.println("ERROR: no key returned from insert entity");
                }
                System.out.println("Insert Key: " + key);
                return (int) key;
            }
            
        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcPreparedStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } 


        return -1;
    }

}