package com.example.jobrec.servlet;

import com.example.jobrec.db.RedisConnection;
import com.example.jobrec.external.SerpAPIClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.jobrec.db.MySQLConnection;
import com.example.jobrec.entity.Item;
import com.example.jobrec.entity.ResultResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //request session ID, do not remove "false", if there's no session, don't creat a new one
        //protect servlet
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session Invalid"));
            return;
        }

        String userId = request.getParameter("user_id");
        //input search keyword


        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        System.out.println("lat: " + lat);
        System.out.println("lon: " + lon);
        MySQLConnection connection = new MySQLConnection();
        //get favorited item IDs
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
        connection.close();
        response.setContentType("application/json");
        //check Redis
        RedisConnection redis = new RedisConnection();
        String cachedResult = redis.getSearchResult(lat, lon, null);

        List<Item> items = null;
        if (cachedResult != null) {
            items = Arrays.asList(mapper.readValue(cachedResult, Item[].class)); //Jackson can only convert items to array, but we need to convert array to list
        } else { //is cache is missed, then we still fetch data from GitHub
        SerpAPIClient client = new SerpAPIClient();
        items = client.search(lat, lon, "Software Developer");
            redis.setSearchResult(lat, lon, null, mapper.writeValueAsString(items));
        }
        redis.close();

        for (Item item : items) {
            //check if the favorite item existed in our favorite lists
            item.setFavorite(favoritedItemIds.contains(item.getId()));
        }

        //return results

        mapper.writeValue(response.getWriter(), items);

    }
}

//    Bubbling and Capturing