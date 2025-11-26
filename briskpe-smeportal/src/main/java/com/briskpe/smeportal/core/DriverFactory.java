package com.briskpe.smeportal.core;

import com.briskpe.smeportal.config.Config;
import com.briskpe.smeportal.enums.Platform;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    public static WebDriver createDriver(Platform platform, String browser, String deviceName) {
        WebDriver driver = null;

        try {
            switch (platform) {
                case WEB:
                    driver = setupWebDriver(browser);
                    break;

                case ANDROID:
                    driver = setupAndroidDriver(deviceName);
                    break;

                case IOS:
                    driver = setupIOSDriver(deviceName);
                    break;

                case MOBILE_WEB:
                    driver = setupMobileWebDriver();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
            setDriver(driver); // ✅ Save driver to ThreadLocal before returning
            return driver;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    /** -------- WEB -------- **/
    public static WebDriver setupWebDriver(String browser) {
        if (browser == null || browser.isEmpty())
            browser = "chrome";

        switch (browser.toLowerCase()) {
            case "chrome":
                // Force WebDriverManager to download the latest ChromeDriver
                WebDriverManager.chromedriver()
                        .clearDriverCache()
                        .clearResolutionCache()
                        .setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                // Essential stability options to prevent crashes
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--remote-allow-origins=*");

                // Disable extensions and popups
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-popup-blocking");

                // Performance and stability
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--start-maximized");

                // Set page load strategy
                chromeOptions.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);

                if (Config.get("headless", "false").equalsIgnoreCase("true")) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }

                System.out.println("🚀 Initializing ChromeDriver...");
                WebDriver chromeDriver = new ChromeDriver(chromeOptions);

                if (!Config.get("headless", "false").equalsIgnoreCase("true")) {
                    chromeDriver.manage().window().maximize();
                }

                System.out.println("✅ ChromeDriver initialized successfully");
                return chromeDriver;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Config.get("headless", "false").equalsIgnoreCase("true")) {
                    firefoxOptions.addArguments("--headless");
                }
                WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
                firefoxDriver.manage().window().maximize();
                return firefoxDriver;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /** -------- ANDROID -------- **/
    private static WebDriver setupAndroidDriver(String deviceName) throws MalformedURLException {
        System.out.println("🤖 Setting up Android Driver...");

        UiAutomator2Options options = new UiAutomator2Options();

        // Read all capabilities from properties
        String androidDeviceName = Config.get("android.deviceName", deviceName);
        String udid = Config.get("android.udid");
        String platformVersion = Config.get("android.platformVersion", "11");
        String automationName = Config.get("android.automationName", "UiAutomator2");
        String noReset = Config.get("android.noReset", "true");
        String timeout = Config.get("android.newCommandTimeout", "300");

        // Set capabilities
        options.setDeviceName(androidDeviceName)
                .setPlatformName("Android")
                .setPlatformVersion(platformVersion)
                .setAutomationName(automationName)
                .setNoReset(Boolean.parseBoolean(noReset))
                .setNewCommandTimeout(Duration.ofSeconds(Long.parseLong(timeout)))
                .setUdid(udid);

        // App configuration
        String appPath = Config.get("appPath", "");
        if (!appPath.isEmpty()) {
            options.setApp(appPath);
            System.out.println("  ✓ Using app path: " + appPath);
        } else {
            String appPackage = Config.get("android.appPackage");
            String appActivity = Config.get("android.appActivity");
            options.setAppPackage(appPackage)
                    .setAppActivity(appActivity);
            System.out.println("  ✓ App Package: " + appPackage);
            System.out.println("  ✓ App Activity: " + appActivity);
        }

        // Log all capabilities for debugging
        System.out.println("  ✓ Device Name: " + androidDeviceName);
        System.out.println("  ✓ UDID: " + udid);
        System.out.println("  ✓ Platform Version: " + platformVersion);
        System.out.println("  ✓ Automation Name: " + automationName);
        System.out.println("  ✓ No Reset: " + noReset);
        System.out.println("  ✓ Command Timeout: " + timeout + "s");

        System.out.println("✅ Android Driver configured successfully");
        return new AndroidDriver(getAppiumUrl(), options);
    }

    /** -------- iOS -------- **/
    private static WebDriver setupIOSDriver(String deviceName) throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions();
        options.setDeviceName(deviceName)
                .setPlatformName("iOS")
                .setPlatformVersion(Config.get("platformVersion"))
                .setAutomationName("XCUITest")
                .setNewCommandTimeout(Duration.ofSeconds(300));

        String appPath = Config.get("appPath", "");
        if (!appPath.isEmpty()) {
            options.setApp(appPath);
        } else {
            options.setBundleId(Config.get("ios.bundleId"));
        }

        return new IOSDriver(getAppiumUrl(), options);
    }

    /** -------- MOBILE WEB -------- **/
    private static WebDriver setupMobileWebDriver() throws MalformedURLException {
        String deviceType = Config.get("mweb.device", "android").toLowerCase();

        if (deviceType.equals("android")) {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setDeviceName(Config.get("android.deviceName", "Android Emulator"));
            options.setPlatformName("Android");
            options.setPlatformVersion(Config.get("android.platformVersion", "11"));
            options.setAutomationName("UiAutomator2");

            // For Mobile Web, we use Chrome browser, not a native app
            options.setCapability("browserName", "Chrome");
            options.setNoReset(true);
            options.setNewCommandTimeout(Duration.ofSeconds(300));

            System.out.println("📱 Setting up Mobile Web on Android (Chrome browser)");
            System.out.println("  ✓ Device: " + Config.get("android.deviceName", "Android Emulator"));
            System.out.println("  ✓ Browser: Chrome");

            try {
                return new AndroidDriver(getAppiumUrl(), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Appium server URL is invalid", e);
            }
        } else if (deviceType.equals("ios")) {
            XCUITestOptions options = new XCUITestOptions();
            options.setDeviceName(Config.get("ios.deviceName"));
            options.setPlatformName("iOS");
            options.setPlatformVersion(Config.get("ios.platformVersion", "14.0"));
            options.setAutomationName("XCUITest");
            options.setCapability("browserName", "Safari");
            options.setNewCommandTimeout(Duration.ofSeconds(300));

            System.out.println("📱 Setting up Mobile Web on iOS (Safari browser)");

            try {
                return new IOSDriver(getAppiumUrl(), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Appium server URL is invalid", e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported mobile device type: " + deviceType);
        }
    }

    /** -------- HELPER -------- **/
    private static URL getAppiumUrl() throws MalformedURLException {
        return new URL(Config.get("appium.url", "http://127.0.0.1:4723"));
    }
}
