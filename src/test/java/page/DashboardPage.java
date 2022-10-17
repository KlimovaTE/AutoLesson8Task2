package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;

public class DashboardPage {
    private SelenideElement heading = Selenide.$("[data-test-id=dashboard]");

    public void visible() {
        heading.shouldBe(visible);
    }
}
