package com.example.niteshverma.demoweather.database;

public interface DatabaseConstant {

    String TABLE_LOCATION_BOOKMARK = "bookmark";


    String QUERY_CREATE_TABLE_LOCATION_BOOKMARK = "CREATE TABLE \"" + TABLE_LOCATION_BOOKMARK+ "\" (" +
            "\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
            "\"lat\" VARCHAR, " +
            "\"lon\" VARCHAR, " +
            "\"name\" VARCHAR)";

    String col_id = "_id";
    String col_lat = "lat";
    String col_lon="lon";
    String col_name="name";

}
