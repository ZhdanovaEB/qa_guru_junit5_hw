package alego2525.com;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;


public class AlfaTests {

    @BeforeEach
    public void setup() {
        Selenide.open("https://alfabank.ru/");
    }


    @ParameterizedTest(name = "При выборе валюты {0} максимальная сумма отображаться {1}")
    @CsvSource(value = {
            "В рублях,         30 млн и более",
            "В долларах США,   400 тыс. и более",
    })
    @Tag("SMOKE")
    public void checkMaxAmountDependingOnCurrency(String currency, String maxAmount) {
        $(byTagAndText("p",currency)).parent().click();
        $x("//div[@class ='slider-input__steps_1l98q']/span[3]").scrollTo().shouldHave(text(maxAmount));
    }


    @ParameterizedTest(name = "При выборе срока {0} количество сумм 8")
    @ValueSource(strings = {"1 год", "2 года", "3 года"})
    @Tag("SMOKE")
    public void checkAmountSummEqualsEight(String years) {

        $(byTagAndText("span","Расcчитать кредит")).click();
        $(byTagAndText("p",years)).click();
        $$(".hPla_").shouldHave(sizeGreaterThanOrEqual(8));
    }


    static Stream<Arguments> checkDisplayedSummDependingOnCurrency() {
        return Stream.of(
                Arguments.of("В рублях", List.of("10 тыс.", "15 млн", "30 млн и более")),
                Arguments.of("В долларах США", List.of("500", "200 тыс.", "400 тыс. и более"))
        );
    }

    @ParameterizedTest(name = "Для валюты валюты {0} должны отображаться суммы {1}")
    @MethodSource()
    @Tag("SMOKE")
    public void checkDisplayedSummDependingOnCurrency(String currency, List<String> amount) {
        $(byTagAndText("p",currency)).parent().click();
        $$(".slider-input__steps_1l98q span").filter(visible).shouldHave(texts(amount));
    }

}
