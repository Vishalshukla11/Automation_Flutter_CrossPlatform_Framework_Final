package com.briskpe.smeportal.pages;

import com.briskpe.smeportal.enums.Platform;

import io.appium.java_client.AppiumBy.FlutterBy;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class LoginPage extends BasePage {

    public static String SigninTab = "mobile_number_entry";
    public static String MObileNumberTextField = "txtfield_mobileNumber";
    public static String ContinueButton = "btn_submit";
    public static String SignInWithEmail = "btn_change_type";
    public static String SignInWithGoogle = "btn_google_o_auth";
    public static String OtpScreen = "verify_otp";
    public static String OtpTextField = "txtfield_pin";
    public static String OtpVerifyButton = "btn_submit";

    // public static String MobileNumberTextField =
    // "//flt-semantics[@aria-label=\"txtfield_mobileNumber\"]//.//input";

    public LoginPage(WebDriver driver, Platform platform) {
        super(driver, platform);
    }

    public boolean isSigninTabDisplayed() {
        // Use explicit wait to give Flutter time to render the element
        WebElement element = waitForElement(SigninTab, 10);
        return element != null && element.isDisplayed();
    }

    public boolean isMobileNumberTextFieldVisible() {
        // Use explicit wait to give Flutter time to render the element
        WebElement element = waitForElement(MObileNumberTextField, 10);
        return element != null && element.isDisplayed();
    }

    // public void enterMobileNumber(String number) {

    // try {
    // if (platform.equals(Platform.WEB)) {
    // typeUsingJS(MObileNumberTextField, number);
    // } else {
    // type(MObileNumberTextField, number);
    // }
    // } catch (Exception e) {
    // // logError("Failed to enter mobile number: " + e.getMessage());
    // System.out.println("Failed to enter mobile number: " + e.getMessage());
    // throw e;
    // }
    // }

    public void enterMobileNumber(String mobileNumber) {
        try {
            if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {
                // ----------------- Flutter Web / Mobile Web -----------------
                JavascriptExecutor js = (JavascriptExecutor) driver;

                // Click the tappable parent flt-semantics to focus the Flutter input
                js.executeScript(
                        "let flt = document.querySelector('flt-semantics[aria-label=\"" + MObileNumberTextField
                                + "\"]');" +
                                "if(flt){ flt.click(); }");

                Thread.sleep(200); // Allow Flutter to focus the input

                // Type the number using Actions
                Actions actions = new Actions(driver);
                actions.sendKeys(mobileNumber).perform();

                System.out.println("Entered mobile number (Web): " + mobileNumber);

            } else {
                // // ----------------- Android / iOS -----------------
                // FlutterElement inputField =
                // flutterDriver.findElement(FlutterBy.accessible(mobileNumberKey));
                // inputField.click(); // Focus field
                // flutterDriver.enterText(mobileNumber); // Enter text

                System.out.println("Entered mobile number (Mobile): " + mobileNumber);
            }

        } catch (Exception e) {
            System.out.println("Failed to enter mobile number: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickSignInWithEmail() {
        click(SignInWithEmail);
    }

    public void clickSignInWithGoogle() {
        click(SignInWithGoogle);
    }

    public void clickContinueButton() {
        click(ContinueButton);
    }

    public boolean isOtpScreenDisplayed() {
        // Use explicit wait to give Flutter time to render the element
        WebElement element = waitForElement(OtpScreen, 10);
        return element != null && element.isDisplayed();
    }

    public void clickGetOtp() {
        click(ContinueButton);
    }

    // public void enterOtp(String otp) {
    // click(OtpTextField);
    // type(OtpTextField, otp);
    // }
    public void enterOtp(String otp) {
        try {
            if (platform == Platform.WEB || platform == Platform.MOBILE_WEB) {

                JavascriptExecutor js = (JavascriptExecutor) driver;

                // 1️⃣ Click flt-semantics wrapper (this is IMPORTANT)
                js.executeScript(
                        "let el = document.querySelector('flt-semantics[aria-label=\"" + OtpTextField + "\"]');" +
                                "if(el){ el.click(); }");

                Thread.sleep(300); // allow Flutter to focus

                // 2️⃣ Type using keyboard events (works for Flutter Web)
                Actions actions = new Actions(driver);
                actions.sendKeys(otp).perform();

                System.out.println("OTP entered: " + otp);

            } else {
                // // ANDROID / iOS
                // WebElement otpField = driver.findElement(getLocator(OtpTextField));
                // otpField.click();
                // Thread.sleep(200);
                // otpField.sendKeys(otp);
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to enter OTP: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickOtpSubmit() {
        click(OtpVerifyButton);
    }

    // public void performLogin(String mobile, String otp) {
    // enterMobileNumber(mobile);
    // clickGetOtp();
    // enterOtp(otp);
    // clickSubmit();
    // }
}
