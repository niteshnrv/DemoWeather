package com.example.niteshverma.demoweather.network;

import com.example.niteshverma.demoweather.model.Bookmark;
import com.example.niteshverma.demoweather.model.Forecast;
import com.example.niteshverma.demoweather.model.Forecast5Day;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkHelper {

    private static final String TYPE_TODAY_FORCAST = "weather";
    private static final String TYPE_FIVE_DAY_FORCAST = "forecast";

    private String baseUrl = "http://api.openweathermap.org/data/2.5/";
    private String API_KEY = "c6e381d8c7ff98f0fee43775817cf6ad";

    public Forecast loadTodayForcast(Bookmark bookmark){

        String response = getDataOverNetwork(TYPE_TODAY_FORCAST, bookmark);
        Gson gson = new Gson();
        Forecast todayForecast = gson.fromJson(response, Forecast.class);
        return todayForecast;
    }

    public Forecast5Day load5DayForcast(Bookmark bookmark){

        String response = getDataOverNetwork(TYPE_FIVE_DAY_FORCAST, bookmark);
        Gson gson = new Gson();
        Forecast5Day forecast5Day = gson.fromJson(response, Forecast5Day.class);
        return forecast5Day;
    }

    private String getDataOverNetwork(String type, Bookmark bookmark){
        String response = null;
        try {
            URL url = new URL(baseUrl + type + getQuery(bookmark));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == 200){
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                response = readStream(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    private String getQuery(Bookmark bookmark){
        StringBuilder sb = new StringBuilder("?");
        sb.append("lat="+ bookmark.getLat());
        sb.append("&lon="+ bookmark.getLon());
        sb.append("&appid=" + API_KEY);
        sb.append("&units=metric");
        return sb.toString();
    }
}
