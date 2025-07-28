package com.example.jobrec.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.example.jobrec.entity.Item;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SerpAPIClient {
    private static final String SerpAPI_KEY = System.getenv("Serp_API_KEY");
    private static final String URL_TEMPLATE = "https://serpapi.com/search.json?engine=google_jobs&q=%shl=en&api_key=%s&location=%s";
    private static final String API_KEY = SerpAPI_KEY;
    private static final String DEFAULT_KEYWORD = "developer";
    private static final String DEFAULT_LOCATION = "toronto";

    public List<Item> search(String keyword, String location){
        if (keyword ==null){
            keyword = DEFAULT_KEYWORD;
        }
        if(location ==null){
            location = DEFAULT_LOCATION;
        }

        //replace " " with %20
        try{
            keyword = URLEncoder.encode(keyword, "UTF-8");
            location = URLEncoder.encode(location, "UTF-8");
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        String url = String.format(URL_TEMPLATE, keyword, API_KEY,location);
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //create a custom handler
        ResponseHandler<List<Item>> responseHandler = response ->{
            if (response.getStatusLine().getStatusCode()!=200){
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();

            if (entity==null){
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
        };

        try{
            return httpclient.execute(new HttpGet(url), responseHandler);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
