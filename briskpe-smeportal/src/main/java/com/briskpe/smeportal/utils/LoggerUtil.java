package com.briskpe.smeportal.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for logging using Log4j2
 * Provides static methods for logging at different levels
 * Automatically integrates with Extent Reports when available
 */
public class LoggerUtil {

    private static final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();

    /**
     * Get logger for the calling class
     */
    private static Logger getLogger() {
        if (loggerThreadLocal.get() == null) {
            // Get the calling class name
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String className = stackTrace[3].getClassName();
            loggerThreadLocal.set(LogManager.getLogger(className));
        }
        return loggerThreadLocal.get();
    }

    /**
     * Log INFO level message
     */
    public static void info(String message) {
        getLogger().info(message);
        logToExtent(message, "INFO");
    }

    /**
     * Log DEBUG level message
     */
    public static void debug(String message) {
        getLogger().debug(message);
    }

    /**
     * Log WARN level message
     */
    public static void warn(String message) {
        getLogger().warn(message);
        logToExtent(message, "WARNING");
    }

    /**
     * Log ERROR level message
     */
    public static void error(String message) {
        getLogger().error(message);
        logToExtent(message, "FAIL");
    }

    /**
     * Log ERROR level message with exception
     */
    public static void error(String message, Throwable throwable) {
        getLogger().error(message, throwable);
        logToExtent(message + " - " + throwable.getMessage(), "FAIL");
    }

    /**
     * Log PASS message (for test steps)
     */
    public static void pass(String message) {
        getLogger().info("✓ " + message);
        logToExtent(message, "PASS");
    }

    /**
     * Log FAIL message (for test steps)
     */
    public static void fail(String message) {
        getLogger().error("✗ " + message);
        logToExtent(message, "FAIL");
    }

    /**
     * Log to Extent Reports if test context is available
     */
    private static void logToExtent(String message, String level) {
        try {
            if (ExtentManager.getTest() != null) {
                switch (level) {
                    case "INFO":
                        ExtentManager.getTest().info(message);
                        break;
                    case "PASS":
                        ExtentManager.getTest().pass("✓ " + message);
                        break;
                    case "FAIL":
                        ExtentManager.getTest().fail("✗ " + message);
                        break;
                    case "WARNING":
                        ExtentManager.getTest().warning(message);
                        break;
                    default:
                        ExtentManager.getTest().info(message);
                }
            }
        } catch (Exception e) {
            // Silently ignore if Extent Reports is not initialized
        }
    }

    /**
     * Clear thread local logger
     */
    public static void clearLogger() {
        loggerThreadLocal.remove();
    }
}
