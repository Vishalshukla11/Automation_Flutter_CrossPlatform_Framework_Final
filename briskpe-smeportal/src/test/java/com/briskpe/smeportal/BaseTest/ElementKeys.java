package com.briskpe.smeportal.BaseTest;

import com.briskpe.smeportal.utils.ElementLocatorReader;

public class ElementKeys {

    public static final String LOGIN_BUTTON;
    public static final String OTP_FIELD;
    public static final String SUBMIT_BUTTON;
    public static final String LOGIN_TAB;

    static {
        LOGIN_TAB = ElementLocatorReader.getLocatorKey("mobile_number_entry");
        LOGIN_BUTTON = ElementLocatorReader.getLocatorKey("login_button");
        OTP_FIELD = ElementLocatorReader.getLocatorKey("otp_field");
        SUBMIT_BUTTON = ElementLocatorReader.getLocatorKey("submit_button");
    }
}
