package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;


public class TransferServiceTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferFromFirstCardToSecondCard() {
        var LoginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardNumber();
        var secondCardInfo = DataHelper.getSecondCardNumber();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var sum = DataHelper.getValidSum(firstCardBalance);

        var expectedFirstCardBalance = firstCardBalance - sum;
        var expectedSecondCardBalance = secondCardBalance + sum;

        var transferPage = dashboardPage.getTransferPage(secondCardInfo);
        dashboardPage = transferPage.getValidTransfer(String.valueOf(sum), firstCardInfo);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);

        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    void shouldTransferFromSecondCardToFirstCard() {
        var LoginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardNumber();
        var secondCardInfo = DataHelper.getSecondCardNumber();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var sum = DataHelper.getValidSum(secondCardBalance);

        var expectedFirstCardBalance = firstCardBalance + sum;
        var expectedSecondCardBalance = secondCardBalance - sum;

        var transferPage = dashboardPage.getTransferPage(firstCardInfo);
        dashboardPage = transferPage.getValidTransfer(String.valueOf(sum), secondCardInfo);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);

        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    void shouldTransferInvalidSum() {
        var LoginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardNumber();
        var secondCardInfo = DataHelper.getSecondCardNumber();

        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);

        var sum = DataHelper.getInvalidSum(secondCardBalance);

        var transferPage = dashboardPage.getTransferPage(firstCardInfo);
        transferPage.replenishCard(String.valueOf(sum), secondCardInfo);
        transferPage.invalidTransfer("Ошибка! Недостаточно средств на карте!");
    }
    @Test
    void shouldTransferBetweenSingleCard() {
        var LoginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardNumber();
        var secondCardInfo = DataHelper.getSecondCardNumber();

        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);

        var sum = DataHelper.getValidSum(firstCardBalance);

        var transferPage = dashboardPage.getTransferPage(firstCardInfo);
        transferPage.replenishCard(String.valueOf(sum), firstCardInfo);
        transferPage.invalidTransfer("Ошибка! Невозможно совершить перевод на карту списания!");
    }
}
