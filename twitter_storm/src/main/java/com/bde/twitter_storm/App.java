package com.bde.twitter_storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class App {
    public static void main(String[] args) {
        String consumerKey = args[0];
        String consumerSecret = args[1];

        String accessToken = args[2];
        String accessTokenSecret = args[3];
    
        Config config = new Config();
        config.setDebug(false);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("twitter-spout", new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret));

        //bolt to strip tweet down to needed fields
        builder.setBolt("strip-filter-bolt", new TweetStripBolt()).shuffleGrouping("twitter-spout");
        //bolt to make NLP api call - this is the current rest point of data
        builder.setBolt("nlp-bolt", new NLPBolt()).shuffleGrouping("strip-filter-bolt");
        //bolt to filter valid entities returned

        //bolt to make wiki api call

        //bolt to story entity and tweet in SQL db

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Twitter-to-SQL", config, builder.createTopology());

        
        try {
            //how long to run topology - set to 30 secs
            Thread.sleep(30000);
        } catch (Exception e) {
            System.out.println("ouch");
        }
        cluster.shutdown();
    }
}
