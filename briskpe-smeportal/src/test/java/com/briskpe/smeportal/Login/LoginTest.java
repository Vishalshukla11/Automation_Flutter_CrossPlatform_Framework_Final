package com.briskpe.smeportal.Login;

import com.briskpe.smeportal.BaseTest.BaseTest;
import com.briskpe.smeportal.enums.Platform;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.briskpe.smeportal.pages.DashboardPage;
import com.briskpe.smeportal.pages.LoginPage;

@Slf4j
public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public  void TestMethod()
    {
        LoginPage loginPage = new LoginPage(getDriver(), platform);
        DashboardPage dashboardPage = new DashboardPage(getDriver(), platform);

        loginPage.LoginToApplication("7771860140","919191");
//        loginPage.typeMobileNumber("7771860140");
//        loginPage.clickContinueButton();
//        loginPage.isOtpScreenDisplayed();
//        loginPage.clickOtpTextField();
//        loginPage.enterOtp("919191");
//        loginPage.clickOtpSubmit();
    }

    @Test(priority = 2, description = "Verify user login functionality")
    public void verifyUserCanLoginSuccessfully() throws InterruptedException {

        LoginPage loginPage = new LoginPage(getDriver(), platform);
        DashboardPage dashboardPage = new DashboardPage(getDriver(), platform);

        if (platform == Platform.ANDROID) {
            loginPage.clickNextButton();
        }

        // Validate elements
        Assert.assertTrue(loginPage.isSigninTabDisplayed(), "Sign In tab should be visible");
       // Assert.assertTrue(loginPage.isMobileNumberTextFieldVisible(), "Mobile Number field should be visible");

        loginPage.typeMobileNumber("7771860140");

        Assert.assertTrue(loginPage.isContinueButtonEnabled(), "Continue button should be enabled");
        loginPage.clickContinueButton();

        // Verify OTP screen & enter OTP88
        Assert.assertTrue(loginPage.isOtpScreenDisplayed(), "OTP screen should be displayed");
        loginPage.clickOtpTextField();
        loginPage.enterOtp("919191");
        loginPage.clickOtpSubmit();

        Thread.sleep(10000);
        enableFlutterSemantics();

        // Verify Dashboard screen
        dashboardPage.clickSkipButton();
        Assert.assertTrue(dashboardPage.isHomePageSideMenuDisplayed(), "Dashboard screen should be displayed");
        Thread.sleep(10000);
    }

}
