package xhtd_ab.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigDB {
    public ConfigDB() {
    }

    private static final String filePath = "idempiereEnv.properties";
    public static String dbT24Url = "";
    public static String dbT24UserName = "";
    public static String dbT24Password = "";
    public static String dbIdempiereUrl = "";
    public static String dbIdempiereUserName = "";
    public static String dbIdempierePassword = "";

    public static void loadConfig() {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            dbT24Url = properties.getProperty("DB_T24_URL");
            dbT24UserName = properties.getProperty("DB_T24_USERNAME");
            dbT24Password = properties.getProperty("DB_T24_PASSWORD");
            dbIdempiereUrl = properties.getProperty("DB_IDEMPIERE_URL");
            dbIdempiereUserName = properties.getProperty("DB_IDEMPIERE_USERNAME");
            dbIdempierePassword = properties.getProperty("DB_IDEMPIERE_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
