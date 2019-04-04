package com.bde.twitter_storm;

import java.util.Map;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

public class NLPBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    @Override
    public void execute(Tuple input) {
        String text = (String) input.getValueByField("text");
        try {
            //get entities and print tweets
            analyzeEntitiesText(text);
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields());
    }

    public static void analyzeEntitiesText(String text) throws Exception {
        // [START language_entities_text]
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
          Document doc = Document.newBuilder()
              .setContent(text)
              .setType(Type.PLAIN_TEXT)
              .build();
          AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();
    
          AnalyzeEntitiesResponse response = language.analyzeEntities(request);
    
          // Print the response
          for (Entity entity : response.getEntitiesList()) {
            //loop through entity list
            for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                //if entity has a wiki url, print the tweet, the entity, and the URL
                if(entry.getKey().equals("wikipedia_url")) {
                    System.out.println("Tweet: " + text);
                    System.out.println("Entity: " + entity.getName());
                    System.out.println("WikiURL: " + entry.getValue());
                }
            }
          }
        }
        // [END language_entities_text]
      }
    
}