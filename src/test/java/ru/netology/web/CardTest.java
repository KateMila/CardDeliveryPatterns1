package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.DataGenerator.Registration.generateDate;
import static ru.netology.web.DataGenerator.Registration.generateUser;

public class CardTest {
    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @Test
    public void shouldSendFormValid() {

        UserInfo user = generateUser();

        $("[data-test-id='city'] input").val(user.getCity());
        $("[data-test-id='date'] input[class='input__control']").
                sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input[class='input__control']").setValue(generateDate(5));
        $("[data-test-id='name'] input").val(user.getName());
        $("[data-test-id='phone'] input").val(user.getPhone());
        $x(".//label[@data-test-id='agreement']").click();
        $(byText("Запланировать")).click();

        $("[data-test-id='success-notification'] div[class='notification__content']").
                shouldBe(visible, Duration.ofSeconds(15)).should(text("Встреча успешно запланирована на " +
                        generateDate(5)));
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] input[class='input__control']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input[class='input__control']").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input[class='input__control']").setValue(generateDate(8));
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification']"). shouldBe(visible, Duration.ofSeconds(15));
        $(byText("Перепланировать")).click();
        $("[data-test-id='success-notification'] div[class='notification__content']").
                shouldBe(visible, Duration.ofSeconds(15)).should(text("Встреча успешно запланирована на " +
                        generateDate(8)));
    }
    @Test
    void shouldSendFormWithInvalidSurname() {
        UserInfo user = generateUser();
        $("[data-test-id=city] input").setValue(user.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue("Pavlova Лидия");
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void shouldSendFormWithInvalidCity() {
        $("[data-test-id=city] input").setValue("St. Petersburg");
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateUser().getName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }
    @Test
    void shouldSendFormWithInvalidDate() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys("18.02.2021");
        $("[data-test-id=name] input").setValue(generateUser().getName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithEmptyName() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithEmptyNumber() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateUser().getName());
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue(generateUser().getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(generateDate(8));
        $("[data-test-id=name] input").setValue(generateUser().getName());
        $("[data-test-id=phone] input").setValue(generateUser().getPhone());
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
