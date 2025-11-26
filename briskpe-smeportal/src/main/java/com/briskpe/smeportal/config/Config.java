package com.briskpe.smeportal.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {

    private static final Properties globalProps = new Properties();
    private static final Properties clientProps = new Properties();

    // Config filenames
    private static String globalConfigFile = "global.properties";
    private static String clientConfigFile = null;

    private static final Object lock = new Object();

    static {
        // Load global properties
        loadGlobalConfig();

        // Dynamically detect client from global properties and load client config
        String clientName = globalProps.getProperty("client");
        if (clientName != null && !clientName.trim().isEmpty()) {
            loadClientConfig(clientName);
        } else {
            System.err.println("⚠️ No client specified in global properties. Skipping client config load.");
        }
    }

    /** Load or reload global configuration */
    public static void loadGlobalConfig() {
        synchronized (lock) {
            globalProps.clear();
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream(globalConfigFile)) {
                if (in != null) {
                    globalProps.load(in);
                } else {
                    System.err.println("⚠️ Global config file not found: " + globalConfigFile);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot load global config file: " + globalConfigFile, e);
            }
        }
    }

    /** Load client-specific configuration */
    private static void loadClientConfig(String clientName) {
        if (clientName == null || clientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }

        clientConfigFile = clientName.toLowerCase() + ".properties";

        synchronized (lock) {
            clientProps.clear();
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream(clientConfigFile)) {
                if (in != null) {
                    clientProps.load(in);
                    System.out.println("✅ Loaded client config: " + clientConfigFile);
                } else {
                    System.err.println("⚠️ Client config file not found: " + clientConfigFile);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot load client config file: " + clientConfigFile, e);
            }
        }
    }

    /** Get property value: system property > client > global */
    public static String get(String key) {
        return System.getProperty(key, clientProps.getProperty(key, globalProps.getProperty(key)));
    }

    /** Get property value with default fallback */
    public static String get(String key, String defaultValue) {
        return System.getProperty(key, clientProps.getProperty(key, globalProps.getProperty(key, defaultValue)));
    }

    /** Set property in client config only (optional save) */
    public static void setClientProperty(String key, String value) {
        synchronized (lock) {
            if (clientProps.isEmpty()) {
                throw new IllegalStateException("Client config not loaded yet.");
            }
            clientProps.setProperty(key, value);
            try (OutputStream out = new FileOutputStream(
                    "C:\\Briskpe\\briskpe-smeportal 1\\briskpe-smeportal\\src\\main\\resources" + clientConfigFile)) {
                clientProps.store(out, "Updated property " + key);
                System.out.println("✅ Client property saved: " + key + "=" + value);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write to " + clientConfigFile, e);
            }
        }
    }

    /** Reload global configuration and automatically reload client */
    public static void reloadGlobal() {
        loadGlobalConfig();

        // Reload client dynamically from global properties
        String clientName = globalProps.getProperty("client");
        if (clientName != null && !clientName.trim().isEmpty()) {
            loadClientConfig(clientName);
        }
    }

    /** Reload client configuration */
    public static void reloadClient() {
        if (clientConfigFile == null) {
            throw new IllegalStateException("Client config not loaded yet.");
        }
        String clientName = clientConfigFile.replace(".properties", "");
        loadClientConfig(clientName);
    }
}
