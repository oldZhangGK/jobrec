package com.example.jobrec.db;

public class MySQLDBUtil {
    private static final String JOBREC_DB_KEY = System.getenv("JOBREC_DB_KEY");
    private static final String JOBREC_DB_INSTANCE_ADDRESS = System.getenv("JOBREC_DB_INSTANCE_ADDRESS");
    private static final String INSTANCE = JOBREC_DB_INSTANCE_ADDRESS;
    private static final String PORT_NUM = "3306";
    public static final String DB_NAME = "jobrecdb";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = JOBREC_DB_KEY;
    public static final String URL = "jdbc:mysql://" + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME + "?user=" +  USERNAME + "&password=" + PASSWORD + "&autoReconnect=true&serverTimezone=UTC";
}
