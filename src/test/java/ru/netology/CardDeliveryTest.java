package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void setUp(){
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {

        var daysToAddForFirstMeeting = 5;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 8;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        SelenideElement form = $(".form");
        form.$("[data-test-id='city'] input").setValue(DataGenerator.generateCity("ru"));
        form.$("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        form.$("[data-test-id='date'] input").setValue(firstMeetingDate);
        form.$("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        form.$("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone("ru"));
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(".button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);
        form.$("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        form.$("[data-test-id='date'] input").setValue(secondMeetingDate);
        form.$(".button").click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}