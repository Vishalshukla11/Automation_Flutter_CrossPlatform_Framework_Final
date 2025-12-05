package com.briskpe.smeportal.Login;

import com.briskpe.smeportal.BaseTest.BaseTest;

import org.testng.annotations.Test;
import com.briskpe.smeportal.pages.LoginPage;

public class DashboardTest extends BaseTest {

    @Test(priority = 1, description = "verify dashboard page")
    public void verifyDashboardPage() throws InterruptedException {
        LoginPage loginPage = new LoginPage(getDriver(), platform);
        loginPage.LoginToApplication("7771860137", "919191");
    }

}
