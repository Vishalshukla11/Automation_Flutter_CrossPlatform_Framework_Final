package com.briskpe.smeportal.pages;

import org.openqa.selenium.WebDriver;

import com.briskpe.smeportal.enums.Platform;

public class SignUp extends BasePage {

    public static String MobileNumberTextField = "txtfield_mobileNumber";
    public static String EmailTextField = "txtfield_email";
    public static String SignUpWithGoogle = "btn_google_o_auth";
    public static String GetOtpButton = "btn_submit";
    public static String TermsAndConditions = "Terms and Conditions";
    public static String PrivacyPolicy = "Privacy Policy";

    public SignUp(WebDriver driver, Platform platform) {
        super(driver, platform);
    }

    public void typeMobileNumber(String mobileNumber) {
        waitForElement(MobileNumberTextField, 10);
        type(MobileNumberTextField, mobileNumber);
    }

    public void typeEmail(String email) {
        waitForElement(EmailTextField, 10);
        type(EmailTextField, email);
    }

    public void clickGetOtp() {
        waitForElement(GetOtpButton, 10);
        click(GetOtpButton);
    }

    public void clickSignUpWithGoogle() {
        waitForElement(SignUpWithGoogle, 10);
        click(SignUpWithGoogle);
    }

    public void clickTermsAndConditions() {
        waitForElement(TermsAndConditions, 10);
        click(TermsAndConditions);
    }

    public void clickPrivacyPolicy() {
        waitForElement(PrivacyPolicy, 10);
        click(PrivacyPolicy);
    }

}
