package utils;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class Log {

    // Thread-safe logger (one instance per test thread)
    private static ThreadLocal<Logger> threadLocalLogger = new ThreadLocal<>();
    private static final Logger DEFAULT_LOGGER = LogManager.getLogger(Log.class);

    /**
     * Initialize logger for each test/scenario
     * @param testName scenario name / test method name
     */
    public static void startTestLog(String testName) {
        Logger logger = LogManager.getLogger(testName);
        threadLocalLogger.set(logger);
        String browser = valueOrDefault(ConfigReader.get("browser"), "unknown-browser");
        String device = valueOrDefault(ConfigReader.get("device"), "unknown-device");
        String os = valueOrDefault(System.getProperty("os.name"), "unknown-os");
        ThreadContext.put("testName", testName);
        ThreadContext.put("browser", browser);
        ThreadContext.put("device", device);
        ThreadContext.put("os", os);
        ThreadContext.put("executionFileKey", sanitize(browser) + "-" + sanitize(device) + "-" + sanitize(os) + "-test");
        ThreadContext.put(
                "logKey",
                sanitize(browser) + "-" + sanitize(device) + "-" + sanitize(testName)
                        + "-" + Thread.currentThread().getId() + "-" + System.nanoTime()
        );

        info("========== STARTING TEST: " + testName + " ==========");
    }

    /** Get logger for current thread */
    private static Logger getLogger() {
        Logger logger = threadLocalLogger.get();
        return logger != null ? logger : DEFAULT_LOGGER;
    }

    /* ========== Logging Wrapper Methods ========== */

    public static void info(String message) {
        getLogger().info(message);
    }

    public static void warn(String message) {
        getLogger().warn(message);
    }

    public static void error(String message) {
        getLogger().error(message);
    }

    public static void fatal(String message) {
        getLogger().fatal(message);
    }

    public static void debug(String message) {
        getLogger().debug(message);
    }

    /** Log exception with message */
    public static void error(String message, Throwable t) {
        getLogger().error(message, t);
    }

    /** Called at end of test */
    public static void endTestLog(String testName) {
        info("========== ENDING TEST: " + testName + " ==========\n");
    }

    /** Clean up after test thread ends */
    public static void unload() {
        ThreadContext.clearAll();
        threadLocalLogger.remove();
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
