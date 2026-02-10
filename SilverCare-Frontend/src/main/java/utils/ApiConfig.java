package utils;

public class ApiConfig {
    // Default to localhost for development, can be overridden by environment variable
    private static final String DEFAULT_BASE_URL = "http://localhost:8081/api";
    
    public static String getBaseUrl() {
        String envUrl = System.getenv("BACKEND_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            return envUrl;
        }
        return DEFAULT_BASE_URL;
    }
}
