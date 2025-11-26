package com.briskpe.smeportal.Login;

import com.briskpe.smeportal.BaseTest.BaseTest;
import com.briskpe.smeportal.enums.Platform;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.briskpe.smeportal.pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void verifyUserCanLoginSuccessfully() throws InterruptedException {
        System.out.println("===== Starting Login Test =====");

        LoginPage loginPage = new LoginPage(getDriver(), platform);

        System.out.println("Waiting for app to load...");
        Thread.sleep(5000);

        // System.out.println("Enabling Flutter semantics");
        // enableFlutterSemantics();
        // System.out.print("Flutte sementics Enabled successfully");

        Thread.sleep(2000);

        System.out.println("Validating Sign In tab visibility");
        Assert.assertTrue(
                loginPage.isSigninTabDisplayed(),
                "Sign In tab should be visible on the login screen");
        System.out.println("✓ Sign In tab is visible");

        System.out.println("Validating Mobile Number field visibility");
        Assert.assertTrue(
                loginPage.isMobileNumberTextFieldVisible(),
                "Mobile Number field should be visible");
        System.out.println("✓ Mobile Number text field is visible");

        System.out.println("Entering Mobile Number: 9833010111");
        loginPage.enterMobileNumber("7771860136");
        System.out.println("✓ Mobile number entered successfully");

        System.out.println("Clicking Continue button");
        loginPage.clickContinueButton();

        Thread.sleep(5000);

        System.out.println("Waiting for OTP screen to be displayed");
        Assert.assertTrue(
                loginPage.isOtpScreenDisplayed(),
                "OTP screen should be displayed after submitting mobile number");
        System.out.println("✓ OTP screen displayed successfully");

        System.out.println("Entering OTP");
        loginPage.enterOtp("919191");

        System.out.println("Submitting OTP");
        loginPage.clickOtpSubmit();

        Thread.sleep(5000);

        System.out.println("===== Login Test Completed Successfully =====");
    }
}
