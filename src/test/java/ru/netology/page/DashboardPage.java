package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement header = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private String balanceStart = "баланс:";
    private String balanceFinish = " р.";

    public DashboardPage() {
        header.shouldBe(visible);
    }

    public int extractBalance(String text) {
        /*String cardInfo = cards.get(Integer.parseInt(text)).text();*/
        val start = text.indexOf(balanceStart);
        val finish = text.lastIndexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String text = $("[data-test-id =" + "'" + cardInfo.getTestIdInCss() + "']").getText();
        return extractBalance(text);
    }

    public TransferPage getTransferPage(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestIdInCss())).$("button").click();
        return new TransferPage();
    }
}