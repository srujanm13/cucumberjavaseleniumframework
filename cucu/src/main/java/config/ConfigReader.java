package config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    public static void load() {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    public static String get(String key) {
        String value = ExecutionContext.get(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            value = properties.getProperty(key);
        }
        if (value == null) {
            return null;
        }
        int commentIndex = value.indexOf('#');
        if (commentIndex >= 0) {
            value = value.substring(0, commentIndex);
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        String env = get("env");
        if (env != null && !env.isBlank()) {
            String envBaseUrl = get(env + ".baseUrl");
            if (envBaseUrl != null && !envBaseUrl.isBlank()) {
                return envBaseUrl;
            }
        }
        return get("baseUrl");
    }
}
