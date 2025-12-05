package com.briskpe.smeportal.Login;

import com.briskpe.smeportal.BaseTest.BaseTest;
import com.briskpe.smeportal.config.Config;
import com.briskpe.smeportal.enums.Platform;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.briskpe.smeportal.pages.DashboardPage;
import com.briskpe.smeportal.pages.LoginPage;

@Slf4j
public class LoginTest extends BaseTest {

    String mobileNumber = Config.get("mobileNo");
    String otp = Config.get("OTP");
    String email = Config.get("email");

    LoginPage loginPage ;
    DashboardPage dashboardPage ;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(getDriver(), platform);
        dashboardPage = new DashboardPage(getDriver(), platform);
    }


    @Test(priority = 1, description = "Verify user login functionality using MobileNumber")
    public void verifyUserCanLoginSuccessfullyUsingMobileNumber() {

        //click on Continue Button If Platform is Android and ios
        loginPage.clickNextButton();
        // Validate elements
        Assert.assertTrue(loginPage.isSigninTabDisplayed(), "Sign In tab should be visible");
        loginPage.typeMobileNumber(mobileNumber);

        Assert.assertTrue(loginPage.isContinueButtonEnabled(), "Continue button should be enabled");
        loginPage.clickContinueButton();

        // Verify OTP screen & enter OTP88
        Assert.assertTrue(loginPage.isOtpScreenDisplayed(), "OTP screen should be displayed");
        loginPage.clickOtpTextField();
        loginPage.enterOtp(otp);
        loginPage.clickOtpSubmit();
        enableFlutterSemantics();
        // Verify Dashboard screen
        dashboardPage.clickSkipButton();
        Assert.assertTrue(dashboardPage.isHomePageSideMenuDisplayed(), "Dashboard screen should be displayed");
    }

    @Test(priority = 2, description = "Verify user login functionality using Email")
    public void verifyUserCanLoginUsingEmail()
    {
        loginPage.clickSignInWithEmail();
    }

}
