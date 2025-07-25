package com.example.jobrec.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "ExampleBookServlet", urlPatterns = "/example_book")
public class ExampleBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));
        String title = jsonRequest.getString("title");
        String author = jsonRequest.getString("author");
        String date = jsonRequest.getString("date");
        float price = jsonRequest.getFloat("price");
        String currency = jsonRequest.getString("currency");
        int page  = jsonRequest.getInt("page");
        String series =  jsonRequest.getString("series");
        String language = jsonRequest.getString("language");
        String isbn = jsonRequest.getString("isbn");

        System.out.println("title: " + title);
        System.out.println("author: " + author);
        System.out.println("date: " + date);
        System.out.println("price: " + price);
        System.out.println("currency: " + currency);
        System.out.println("page: " + page);
        System.out.println("series: " + series);
        System.out.println("language: " + language);
        System.out.println("isbn: " + isbn);

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "ok");
        response.getWriter().print(jsonResponse);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String keyword = request.getParameter("keyword");
//        String category = request.getParameter("category");
//        System.out.println("Keyword: " + keyword);
//        System.out.println("Category: " + category);
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        json.put("title", "Elon Musk");
        json.put("author", "J.K. Rowling");
        json.put("date", "2025-07-25");
        json.put("currency", "USD");
        json.put("price", 39.99);
        json.put("page", 1112);
        json.put("series", "None");
        json.put("language", "en_US");
        json.put("isbn" , "1235464635");
        response.getWriter().print(json);
    }
}