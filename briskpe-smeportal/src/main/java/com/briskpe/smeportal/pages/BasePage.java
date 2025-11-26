package com.briskpe.smeportal.pages;

import com.briskpe.smeportal.enums.Platform;
import com.briskpe.smeportal.utils.ExtentManager;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected Platform platform;

    public BasePage(WebDriver driver, Platform platform) {
        this.driver = driver;
        this.platform = platform;
    }

    protected WebDriver getDriver() {
        return this.driver;
    }

    public WebElement getElementById(String key) {
        By locator;
        switch (platform) {
            case WEB:
            case MOBILE_WEB:
                // Support Flutter Web: flt-semantics-identifier, id, name, or text
                locator = By.xpath("//*[@flt-semantics-identifier='" + key + "' or @aria-label='" + key
                        + "' or contains(@id, '" + key
                        + "') or contains(@name, '" + key + "') or text()='" + key + "']");
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
        try {
            getElementById(key).click();
            logInfo("Clicked on element: " + key);
        } catch (Exception e) {
            logError("Failed to click on element: " + key + " - " + e.getMessage());
            throw e;
        }
    }

    public void type(String key, String text) {
        try {
            WebElement element = getElementById(key);
            element.clear();
            element.sendKeys(text);
            logInfo("Typed '" + text + "' into element: " + key);
        } catch (Exception e) {
            logError("Failed to type into element: " + key + " - " + e.getMessage());
            throw e;
        }
    }

    public void typeUsingJS(String key, String text) {
        try {
            WebElement element = getElementById(key);

            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Click using JS
            js.executeScript("arguments[0].click();", element);

            // Type using JS
            js.executeScript("arguments[0].value = arguments[1];", element, text);

            logInfo("Typed '" + text + "' into: " + key);

        } catch (Exception e) {
            logError("Cannot type into: " + key);
            throw e;
        }
    }

    public WebElement waitForElement(String key, int timeoutSeconds) {
        try {
            logInfo("Waiting for element: " + key + " (timeout: " + timeoutSeconds + "s)");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            By locator;
            switch (platform) {
                case WEB:
                case MOBILE_WEB:
                    locator = By.xpath("//*[@flt-semantics-identifier='" + key + "' or @aria-label='" + key
                            + "' or contains(@id, '" + key
                            + "') or contains(@name, '" + key + "') or text()='" + key + "']");
                    break;
                case ANDROID:
                case IOS:
                    locator = AppiumBy.accessibilityId(key);
                    break;
                default:
                    throw new IllegalStateException("Unexpected platform: " + platform);
            }
            WebElement element = wait.until(driver -> driver.findElement(locator));
            logInfo("Element found: " + key);
            return element;
        } catch (Exception e) {
            logError("Element not found: " + key + " - " + e.getMessage());
            return null;
        }
    }

    public boolean isElementDisplayed(String key) {
        try {
            return getElementById(key).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // public void enableFlutterSemantics() {
    // if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
    // try {
    // org.openqa.selenium.JavascriptExecutor js =
    // (org.openqa.selenium.JavascriptExecutor) driver;
    // js.executeScript("document.querySelector('#body >
    // flt-semantics-placeholder').click();");
    // // Short wait to ensure tree populates
    // Thread.sleep(1000);
    // } catch (Exception e) {
    // // Ignore if already enabled or element not found
    // }
    // }
    // }

    private void logInfo(String message) {
        try {
            if (ExtentManager.getTest() != null) {
                ExtentManager.getTest().info(message);
            }
        } catch (Exception e) {
            // Ignore if test context not available
        }
    }

    private void logError(String message) {
        try {
            if (ExtentManager.getTest() != null) {
                ExtentManager.getTest().fail(message);
            }
        } catch (Exception e) {
            // Ignore if test context not available
        }
    }

}
