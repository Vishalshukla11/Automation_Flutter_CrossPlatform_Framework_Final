package com.briskpe.smeportal.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots";

    /**
     * Capture screenshot and return file path
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            // Capture screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);

            // Copy to destination
            FileUtils.copyFile(srcFile, destFile);

            LoggerUtil.info("Screenshot captured: " + filePath);
            return destFile.getAbsolutePath();

        } catch (IOException e) {
            LoggerUtil.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Capture screenshot and attach to Extent Report
     */
    public static void captureAndAttachScreenshot(WebDriver driver, String testName) {
        try {
            String screenshotPath = captureScreenshot(driver, testName);
            if (screenshotPath != null && ExtentManager.getTest() != null) {
                ExtentManager.getTest().addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to attach screenshot to report", e);
        }
    }
}
