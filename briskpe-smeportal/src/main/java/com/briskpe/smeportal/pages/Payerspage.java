package com.briskpe.smeportal.pages;

import org.openqa.selenium.WebDriver;

import com.briskpe.smeportal.enums.Platform;

public class Payerspage extends BasePage {

    public Payerspage(WebDriver driver, Platform platform) {
        super(driver, platform);
    }

    public static String Payers_Screen = "screen_payer_list";
    public static String Add_New_PayerButton = "btn_add_payer";
    public static String Payer_Type_Button = "Individual";
    public static String Payer_Name_TextField = "txtfield_payerName";
    public static String Payer_Email_TextField = "txtfield_email";
    public static String Payer_MobileNumber_TextField = "txtfield_mobileNumber";
    public static String payer_Country_DropDown = "Country*";
    public static String payer_purposeCode_DropDown = "Select country";
    public static String payer_Address_TextField = "txtfield_payerAddress";
    public static String payers_companyName_TextField = "txtfield_companyName";
    public static String payers_website_TextField = "txtfield_website";

}
