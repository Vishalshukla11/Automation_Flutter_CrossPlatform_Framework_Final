package com.briskpe.smeportal.pages;

import com.briskpe.smeportal.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.briskpe.smeportal.enums.Platform;

@Slf4j
public class LoginPage extends BasePage {

    public static String NextButton = "btn_next";
    public static String LetsGetStartredButton = "btn_get_started";
    public static String SigninTab = "mobile_number_entry";
    public static String MObileNumberTextField = "//flt-semantics[@flt-semantics-identifier=\"txtfield_mobileNumber\"]//flt-semantics//input";
    public static String ContinueButton = "btn_submit";
    public static String SignInWithEmail = "btn_change_type";
    public static String SignInWithGoogle = "btn_google_o_auth";
    public static String OtpScreen = "verify_otp";
    public static String OtpTextField = "//flt-semantics[@flt-semantics-identifier=\"txtfield_pin\"]//input";
    public static String OtpVerifyButton = "btn_submit";

    public LoginPage(WebDriver driver, Platform platform) {
        super(driver, platform);
        this.platform=platform;
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
        return isElementDisplayed(MObileNumberTextField);

    }

    public void typeMobileNumber(String mobileNumber) {
        type(MObileNumberTextField, mobileNumber);
    }

    public void enterEmail(String email)
    {
    }

    public boolean isContinueButtonEnabled() {
        return waitForElement(ContinueButton, 10).isEnabled();
    }

    public  void clickContinueButton() {
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
        click(OtpVerifyButton);
    }

    public void clickSignInWithEmail() {
        click(SignInWithEmail);
    }

    public void clickSignInWithGoogle() {
        click(SignInWithGoogle);
    }

    public void LoginToApplication(String mobile, String otp) {
        //click on Continue Button If Platform is Android and ios
        clickNextButton();
        // Validate elements
        isSigninTabDisplayed();
        typeMobileNumber(mobile);
        isContinueButtonEnabled();
        clickContinueButton();

        // Verify OTP screen & enter OTP88
        isOtpScreenDisplayed();
        clickOtpTextField();
        enterOtp(otp);
        clickOtpSubmit();


    }

}
