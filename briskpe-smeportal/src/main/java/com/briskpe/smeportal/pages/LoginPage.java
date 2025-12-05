package com.briskpe.smeportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.briskpe.smeportal.enums.Platform;

public class LoginPage extends BasePage {

    public static String NextButton = "btn_next";
    public static String LetsGetStartredButton = "btn_get_started";
    public static String SigninTab = "mobile_number_entry";
    public static String MObileNumberTextField = "//flt-semantics[@flt-semantics-identifier=\"txtfield_mobileNumber\"]//flt-semantics//input";
    // public static String EnterMobileNumberTextField = "mobileNumber";
    public static String ContinueButton = "btn_submit";
    public static String SignInWithEmail = "btn_change_type";
    public static String SignInWithGoogle = "btn_google_o_auth";
    public static String OtpScreen = "verify_otp";
    public static String OtpTextField = "//flt-semantics[@flt-semantics-identifier=\"txtfield_pin\"]//input";
    public static String OtpVerifyButton = "btn_submit";

    public LoginPage(WebDriver driver, Platform platform) {
        super(driver, platform);
    }

    public void clickNextButton() {

        if (platform.equals(Platform.ANDROID)) {
            for (int i = 0; i <= 3; i++) {
                waitForElement(NextButton, 10);
                click(NextButton);
            }

            // Now 'Next' is gone → Click 'Let's Get Started'
            if (isElementDisplayed(LetsGetStartredButton)) {
                click(LetsGetStartredButton);
                return;
            }

            throw new RuntimeException(
                    "Neither Next button nor LetsGetStarted button is visible after completing onboarding steps.");
        }
    }

    public boolean isSigninTabDisplayed() {
        // Use explicit wait to give Flutter time to render the element
        return isElementDisplayed(SigninTab);
    }

    public boolean isMobileNumberTextFieldVisible() {
        waitForElement(MObileNumberTextField, 10);
        return isElementDisplayed(MObileNumberTextField);

    }

    public void typeMobileNumber(String mobileNumber) {
        waitForElement(MObileNumberTextField, 10);
        type(MObileNumberTextField, mobileNumber);


    }

    public boolean isContinueButtonEnabled() {
        return waitForElement(ContinueButton, 10).isEnabled();
    }

    public void clickContinueButton() {
        waitForElement(ContinueButton, 10);
        click(ContinueButton);
    }


    public boolean isOtpScreenDisplayed() {
        return isElementDisplayed(OtpScreen);
    }

    public void clickOtpTextField() {
        click(OtpTextField);
    }

    public void enterOtp(String otp) {
        type(OtpTextField,otp);
    }

    public void clickOtpSubmit() {
        waitForElement(OtpVerifyButton, 10);
        click(OtpVerifyButton);
    }

    public void clickSignInWithEmail() {
        waitForElement(SignInWithEmail, 10);
        click(SignInWithEmail);
    }

    public void clickSignInWithGoogle() {
        waitForElement(SignInWithGoogle, 10);
        click(SignInWithGoogle);
    }

    public void LoginToApplication(String mobile, String otp) {
        clickNextButton();
        logInfo("clicked next button");
        typeMobileNumber(mobile);
        logInfo("Entered Mobile number");
        clickContinueButton();
        logInfo("Clicked Continue button");
        isOtpScreenDisplayed();
        logInfo("Otp Screen is displayed ");
        enterOtp(otp);
        logInfo("Otp is entered");
        clickOtpSubmit();
        logInfo("Otp is submitted by clicking submit button");

    }

}
