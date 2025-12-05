package com.briskpe.smeportal.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.briskpe.smeportal.BaseTest.BaseTest;
import com.briskpe.smeportal.utils.ExtentManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    private static ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.cleanOldReports(); // Clean up old reports before starting
        extent = ExtentManager.getInstance();
        System.out.println("\n========== TEST SUITE STARTED ==========");
        System.out.println("Suite Name: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        ExtentTest test = extent.createTest(testName)
                .assignCategory(className)
                .assignAuthor("Automation Team");

        ExtentManager.setTest(test);

        test.info("Test Started: " + testName);
        System.out.println("\n▶ Starting Test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.PASS, MarkupHelper.createLabel("Test PASSED", ExtentColor.GREEN));

        System.out.println("✅ Test PASSED: " + result.getMethod().getMethodName());

        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();

        test.log(Status.FAIL, MarkupHelper.createLabel("Test FAILED", ExtentColor.RED));
        test.fail(result.getThrowable());

        // Capture screenshot
        try {
            WebDriver driver = ((BaseTest) result.getInstance()).getDriver();
            if (driver != null) {
                String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                System.out.println("📸 Screenshot captured: " + screenshotPath);
            }
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }

        System.out.println("❌ Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("Error: " + result.getThrowable().getMessage());

        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.SKIP, MarkupHelper.createLabel("Test SKIPPED", ExtentColor.ORANGE));
        test.skip(result.getThrowable());

        System.out.println("⏭ Test SKIPPED: " + result.getMethod().getMethodName());

        ExtentManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();

        System.out.println("\n========== TEST SUITE COMPLETED ==========");
        System.out.println("Total Tests: " + context.getAllTestMethods().length);
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("Report: " + ExtentManager.getReportPath());
        System.out.println("==========================================\n");
    }

    private String captureScreenshot(WebDriver driver, String testName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        String screenshotDir = "test-output/screenshots/";

        // Create directory if it doesn't exist
        new File(screenshotDir).mkdirs();

        String screenshotPath = screenshotDir + screenshotName;

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        Files.copy(source.toPath(), Paths.get(screenshotPath));

        return screenshotPath;
    }
}
