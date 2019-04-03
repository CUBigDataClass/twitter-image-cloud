package com.bde.twitter_storm;

import org.apache.storm.Config;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        String consumerKey = args[0];
        String consumerSecret = args[1];

        String accessToken = args[2];
        String accessTokenSecret = args[3];
    
        Config config = new Config();
        config.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("twitter-spout",
                new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret));

        //bolt to strip tweet down to needed fields
        builder.setBolt("strip-filter-bolt", new TweetStripBolt()).shuffleGrouping("twitter-spout");
        //bolt to make NLP api call
        builder.setBolt("nlp-bolt", new NLPBolt()).shuffleGrouping("strip-filter-bolt");
        //bolt to filter valid entities returned

        //bolt to make wiki api call

        //bolt to story entity and tweet in SQL db

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TwitterHashtagStorm", config, builder.createTopology());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("ouch");
        }
        cluster.shutdown();
    }
}
