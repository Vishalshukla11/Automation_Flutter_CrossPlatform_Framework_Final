package com.briskpe.smeportal.BaseTest;

import com.briskpe.smeportal.core.DriverFactory;
import com.briskpe.smeportal.config.Config;
import com.briskpe.smeportal.enums.Platform;
import com.briskpe.smeportal.listeners.TestListener;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected Platform platform;

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement getElementById(String key) {
        By locator;
        switch (platform) {
            case WEB:
            case MOBILE_WEB:
                locator = By.xpath(
                        "//*[contains(@id, '" + key + "') or contains(@name, '" + key + "') or text()='" + key + "']");
                break;
            case ANDROID:
            case IOS:
                locator = AppiumBy.accessibilityId(key);
                break;
            default:
                throw new IllegalStateException("Unexpected platform: " + platform);
        }
        return driver.findElement(locator);
    }

    public void click(String key) {
        getElementById(key).click();
    }

    public void type(String key, String text) {
        WebElement element = getElementById(key);
        element.clear();
        element.sendKeys(text);
    }

    public void enableFlutterSemantics() {
        if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
            try {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("document.querySelector('#body > flt-semantics-placeholder').click();");
                // Short wait to ensure tree populates
                Thread.sleep(1000);
            } catch (Exception e) {
                // Ignore if already enabled or element not found
            }
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        try {
            System.out.println("\n========== TEST SETUP STARTED ==========");

            platform = Platform.fromString(Config.get("platform", "WEB"));
            System.out.println("Platform: " + platform);

            String browser = Config.get("browser", "chrome");
            System.out.println("Browser: " + browser);

            driver = DriverFactory.createDriver(platform, browser, Config.get("deviceName", ""));
            System.out.println("Driver created successfully");

            // Navigate to URL only for Web platforms
            if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
                String url = Config.get("url");
                System.out.println("Navigating to URL: " + url);
                driver.get(url);
            }

            // Wait a bit for Flutter to initialize
            Thread.sleep(2000);

            // Enable Flutter semantics for Web/Mobile Web platforms
            if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    // Wait for semantics placeholder to be available
                    for (int i = 0; i < 5; i++) {
                        Object result = js.executeScript(
                                "var elem = document.querySelector('#body > flt-semantics-placeholder');" +
                                        "if (elem) { elem.click(); return true; } return false;");
                        if (Boolean.TRUE.equals(result)) {
                            System.out.println("✓ Flutter semantics enabled");
                            Thread.sleep(1000); // Wait for semantics tree to populate
                            break;
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Could not enable semantics (may already be enabled): " + e.getMessage());
                }
            }

            System.out.println("Page loaded successfully");
            if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Page Title: " + driver.getTitle());
            }
            System.out.println("========== TEST SETUP COMPLETED ==========\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR DURING SETUP:");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();

            // Clean up if driver was created
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception quitException) {
                    System.err.println("Failed to quit driver: " + quitException.getMessage());
                }
            }

            throw new RuntimeException("Test setup failed", e);
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
