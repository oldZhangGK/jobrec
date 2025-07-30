package com.example.jobrec.Recommendation;

import java.util.*;

import com.example.jobrec.db.MySQLConnection;
import com.example.jobrec.entity.Item;
import com.example.jobrec.external.SerpAPIClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Recommendation {
    public List<Item> recommendedItems(String userId, String location){
        List<Item> recommendedItems = new ArrayList<>();

        // Step 1: get all favorited itemids
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);

        // Step 2: get all keywords, sort by count

        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoritedItemIds){
            Set<String> keywords = connection.getKeywords(itemId);
            for (String keyword : keywords){
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0) + 1);
            }
        }
        connection.close();

        List<Map.Entry<String,Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2)
            -> Integer.compare(e2.getValue(), e1.getValue())
        );

        if (keywordList.size() > 3){
            keywordList = keywordList.subList(0, 3);
        }

        Set<String> visitedItemIds = new HashSet<>();
        SerpAPIClient client = new SerpAPIClient();
        ObjectMapper mapper = new ObjectMapper();

        for (Map.Entry<String,Integer> keyword : keywordList){
            List<Item> items = client.search(keyword.getKey(),location);

            for (Item item : items){
                if (!favoritedItemIds.contains(item.getId()) && !visitedItemIds.contains(item.getId())){
                    recommendedItems.add(item);
                    visitedItemIds.add(item.getId());
                }
            }
        }

        return recommendedItems;
    }

}
