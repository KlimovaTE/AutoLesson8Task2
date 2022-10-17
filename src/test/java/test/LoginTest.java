package test;

import data.DataHelper;
import data.DataHelperBD;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void setClose() {
        DataHelperBD.clearingTable("auth_codes");
        DataHelperBD.clearingTable("card_transactions");
        DataHelperBD.clearingTable("cards");
        DataHelperBD.clearingTable("users");
    }

    @Test
    void shouldLogIn() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getFirstAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.validVerify(DataHelperBD.getValidCode("vasya")).visible();
    }

    @Test
    void shouldBlockAfterThreeInvalidLogin() {
        var loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getInvalidPassFirstAuthInfo());
        loginPage.invalidLogin(DataHelper.getInvalidPassFirstAuthInfo());
        loginPage.invalidLogin(DataHelper.getInvalidPassFirstAuthInfo());
    }
}
