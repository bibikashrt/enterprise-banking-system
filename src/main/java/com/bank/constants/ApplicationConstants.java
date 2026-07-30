package com.bank.constants;

/**
 * Application wide constants used throughout
 * the Enterprise Banking System.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
        throw new IllegalStateException("Utility class");
    }

    /*
     * API
     */
    public static final String API_BASE_PATH = "/api";

    /*
     * Datasource
     */
    public static final String DATASOURCE_JNDI =
            "java:/jdbc/EnterpriseBankingDS";

    /*
     * Content Types
     */
    public static final String APPLICATION_JSON =
            "application/json";

    /*
     * Messages
     */
    public static final String SUCCESS = "Success";

    public static final String FAILED = "Failed";

    /*
     * Date Format
     */
    public static final String DATE_TIME_PATTERN =
            "yyyy-MM-dd HH:mm:ss";

}