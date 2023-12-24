package ru.netology.page;


import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TransferPage {
    private SelenideElement header = $x("//h1[text()='Пополнение карты']");
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMsg = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        header.shouldBe(visible);
    }

    public DashboardPage getValidTransfer(String sum, DataHelper.CardInfo cardInfo) {
        replenishCard(sum, cardInfo);
        return new DashboardPage();
    }

    public void replenishCard(String sum, DataHelper.CardInfo cardInfo) {
        amountField.sendKeys(sum);
        fromField.sendKeys(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void invalidTransfer(String expectedText) {
        errorMsg.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
