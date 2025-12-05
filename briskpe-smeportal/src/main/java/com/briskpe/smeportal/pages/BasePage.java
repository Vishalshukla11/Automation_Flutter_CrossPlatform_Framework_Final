package com.briskpe.smeportal.pages;

import com.briskpe.smeportal.enums.Platform;
import com.briskpe.smeportal.utils.ExtentManager;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    // public WebElement getElementById(String key) {
    // switch (platform) {
    // case WEB:
    // case MOBILE_WEB:
    // // Support Flutter Web: flt-semantics-identifier, id, name, or text
    // By locator = By.xpath("//*[@flt-semantics-identifier='" + key + "' or
    // @aria-label='" + key
    // + "' or contains(@id, '" + key
    // + "') or contains(@name, '" + key + "') or text()='" + key + "']");
    // return driver.findElement(locator);

    // case ANDROID:
    // case IOS:
    // // Try accessibility ID first
    // try {
    // return driver.findElement(AppiumBy.accessibilityId(key));
    // } catch (Exception e1) {
    // // Fallback to resource ID (Android) or id (iOS)
    // System.out.println("Accessibility ID not found, trying id for: " + key);
    // try {
    // return driver.findElement(AppiumBy.id(key));
    // } catch (Exception e2) {
    // System.out.println("ID not found,trying xpath for: " + key);
    // try {
    // return driver.findElement(AppiumBy.xpath(key));
    // } catch (Exception e) {
    // System.out.println("Element not found by xpath for: " + key);
    // throw new RuntimeException("Element not found by any locator for: " + key);
    // }
    // }
    // }

    // default:
    // throw new IllegalStateException("Unexpected platform: " + platform);
    // }
    // }

    public WebElement getElementById(String key) {

        // 1️⃣ If user passed xpath directly → use it as xpath
        if (key.startsWith("//") || key.startsWith("(//")) {
            try {
                return driver.findElement(By.xpath(key));
            } catch (Exception e) {
                // Throw NoSuchElementException so explicit waits upstream keep polling
                throw new NoSuchElementException("XPath not found: " + key);
            }
        }

        switch (platform) {

            // ====================================================
            // WEB & MOBILE WEB (Flutter Web Optimized)
            // ====================================================
            case WEB:
            case MOBILE_WEB:
                // Added normalize-space for better text matching
                String webXpath = "//*[@flt-semantics-identifier='" + key +
                        "' or @aria-label='" + key +
                        "' or @name='" + key +
                        "' or @id='" + key +
                        "' or normalize-space(text())='" + key + "']";

                return driver.findElement(By.xpath(webXpath));

            // ====================================================
            // ANDROID / IOS (Native + Flutter)
            // ====================================================
            case ANDROID:
            case IOS:
                // ⚠️ CRITICAL: Save current wait time and set to 0 to prevent
                // 10-second delays on every failed 'try' block.
                // (Assuming you store your global wait time somewhere, e.g., 10 seconds)
                driver.manage().timeouts().implicitlyWait(Duration.ZERO);

                WebElement element = null;

                try {
                    // 1️⃣ Try accessibility id (Fastest)
                    if (element == null) {
                        try {
                            element = driver.findElement(AppiumBy.accessibilityId(key));
                        } catch (Exception ignored) {
                        }
                    }

                    // 2️⃣ Try id (resource-id)
                    if (element == null) {
                        try {
                            element = driver.findElement(AppiumBy.id(key));
                        } catch (Exception ignored) {
                        }
                    }

                    // 3️⃣ Try Flutter Semantics XPath
                    if (element == null) {
                        String flutterXpath = "//*[@content-desc='" + key +
                                "' or @accessibilityLabel='" + key +
                                "' or @aria-label='" + key +
                                "' or contains(@text, '" + key + "')]"; // Added text check for native
                        try {
                            element = driver.findElement(By.xpath(flutterXpath));
                        } catch (Exception ignored) {
                        }
                    }

                } finally {
                    // ⚠️ CRITICAL: Restore default implicit wait (e.g., 10 seconds)
                    // Adjust '10' to whatever your framework default is.
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                }

                if (element != null) {
                    return element;
                }

                // If everything fails, throw standard Selenium exception
                throw new NoSuchElementException("Element not found using any locator for key: " + key);
        }

        throw new IllegalStateException("Unexpected platform: " + platform);
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
            // element.clear();
            // element.click();
            element.sendKeys(text);
            logInfo("Typed '" + text + "' into element: " + key);
        } catch (Exception e) {
            logError("Failed to type into element: " + key + " - " + e.getMessage());
            throw e;
        }
    }

    // public void type(String key,String mobileNumber) {
    // try {
    // if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {

    // JavascriptExecutor js = (JavascriptExecutor) driver;

    // // Click the tappable parent flt-semantics to focus the Flutter input
    // js.executeScript(
    // "let flt = document.querySelector('flt-semantics[aria-label=\"" +
    // key
    // + "\"]');" +
    // "if(flt){ flt.click(); }");

    // Thread.sleep(200); // allow Flutter to focus the input

    // // Type the number using Actions (works on Flutter Web)
    // new Actions(driver)
    // .sendKeys(mobileNumber)
    // .perform();

    // System.out.println("Entered mobile number (Web): " + mobileNumber);

    // } else {
    // // Android / iOS
    // type(key, mobileNumber);
    // System.out.println("Entered mobile number (Mobile): " + mobileNumber);
    // }

    // } catch (Exception e) {
    // System.out.println("Failed to enter mobile number: " + e.getMessage());
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * Waits for an element to be present using WebDriverWait.
     * This method reuses getElementById logic, so all fallback strategies apply.
     */
    public WebElement waitForElement(String key, int timeoutSeconds) {
        try {
            logInfo("Waiting for element: " + key + " (timeout: " + timeoutSeconds + "s)");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

            // Use WebDriverWait with getElementById - this reuses all the fallback logic
            WebElement element = wait.until(driver -> {
                try {
                    return getElementById(key);
                } catch (Exception e) {
                    return null; // Return null to keep waiting
                }
            });

            if (element != null) {
                logInfo("Element found: " + key);
                return element;
            } else {
                logError("Element not found after waiting " + timeoutSeconds + "s: " + key);
                return null;
            }
        } catch (Exception e) {
            logError("Element not found: " + key + " - " + e.getMessage());
            return null;
        }
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

    public void logInfo(String message) {
        try {
            if (ExtentManager.getTest() != null) {
                ExtentManager.getTest().info(message);
            }
        } catch (Exception e) {
            // Ignore if test context not available
        }
    }

    public void logError(String message) {
        try {
            if (ExtentManager.getTest() != null) {
                ExtentManager.getTest().fail(message);
            }
        } catch (Exception e) {
            // Ignore if test context not available
        }
    }

    public boolean isElementDisplayed(String key) {
        WebElement element = waitForElement(key, 10);
        if (element != null) {
            return element.isDisplayed();
        } else {
            System.out.println("Element not found: " + key);
            return false;
        }
    }

}
