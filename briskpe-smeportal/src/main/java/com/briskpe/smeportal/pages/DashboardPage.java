package com.briskpe.smeportal.pages;

import com.briskpe.smeportal.enums.Platform;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver, Platform platform) {
        super(driver, platform);
    }

    public static String SkipButton = "Skip";
    public static String HomePageSideMenu = "navitem-home";
    public static String AwatingInvoicesSideMenu = "navitem-awaiting_invoices";
    public static String ReceivedPaymentsSideMenu = "navitem-received_payments";
    public static String RequestedPaymentsSideMenu = "navitem-requested_payments";
    public static String PaymentLinksSideMenu = "navitem-payment_links";
    public static String PayersSideMenu = "navitem-payers";
    public static String ReportsSideMenu = "navitem-reports";
    public static String MarketplacesSideMenu = "navitem-marketplaces";

    public void clickSkipButton() {
        logInfo("waiting for skip button");
        waitForElement(SkipButton, 10);
        logInfo("clicking skip button");
        click(SkipButton);
        logInfo("skip button clicked");
    }

    public void clickHomePageSideMenu() {
        waitForElement(HomePageSideMenu, 10);
        click(HomePageSideMenu);
    }

    public void clickAwatingInvoicesSideMenu() {
        waitForElement(AwatingInvoicesSideMenu, 10);
        click(AwatingInvoicesSideMenu);
    }

    public void clickReceivedPaymentsSideMenu() {
        waitForElement(ReceivedPaymentsSideMenu, 10);
        click(ReceivedPaymentsSideMenu);
    }

    public void clickRequestedPaymentsSideMenu() {
        waitForElement(RequestedPaymentsSideMenu, 10);
        click(RequestedPaymentsSideMenu);
    }

    public void clickPaymentLinksSideMenu() {
        waitForElement(PaymentLinksSideMenu, 10);
        click(PaymentLinksSideMenu);
    }

    public void clickPayersSideMenu() {
        waitForElement(PayersSideMenu, 10);
        click(PayersSideMenu);
    }

    public void clickReportsSideMenu() {
        waitForElement(ReportsSideMenu, 10);
        click(ReportsSideMenu);
    }

    public void clickMarketplacesSideMenu() {
        waitForElement(MarketplacesSideMenu, 10);
        click(MarketplacesSideMenu);
    }

    public boolean isHomePageSideMenuDisplayed() {
        waitForElement(HomePageSideMenu, 10);
        return isElementDisplayed(HomePageSideMenu);
    }
}
