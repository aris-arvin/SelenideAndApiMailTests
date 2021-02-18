import com.codeborne.selenide.Condition;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static io.restassured.RestAssured.given;

public class MailTests {
    private static String mailUsername = System.getProperty("mail.username", "Имя ящика");
    private static String mailPassword = System.getProperty("mail.password", "");

    @BeforeEach
    public void setUp() {
        timeout = 10000;
        baseUrl = "http://mail.ru";
        browser = "chrome";
        startMaximized = true;
        open("/");
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Mail auth test")
    public void mainMailTest() throws InterruptedException {
        $(By.name("login")).setValue("testlasttest");
        $(byXpath(".//button[@data-testid='enter-password']")).click();
        $(By.name("password")).val("fhfh3864");
        $(byXpath(".//button[@data-testid='login-to-mail']")).click();

        Assert.assertEquals("Проверяем заголовок письма"
                , "Вход с нового устройства в аккаунт"
                , $(byXpath(".//div[@class='dataset__items']/a[1]//span[@class='llc__subject']")).getText());
    }

    @Test
    @DisplayName("Mail title test")
    public void mainTitleTest() throws InterruptedException {
        $(By.name("login")).setValue("testlasttest");
        $(byXpath(".//button[@data-testid='enter-password']")).click();
        $(By.name("password")).val("fhfh3864");
        $(byXpath(".//button[@data-testid='login-to-mail']")).click();

        Assert.assertEquals("Проверяем заголовок письма"
                , "Вход с нового устройства в аккаунт"
                , $(byXpath(".//div[@class='dataset__items']/a[1]//span[@class='llc__subject']")).getText());

        $(byXpath(".//a[@title='выход']")).click();
        $(By.name("login")).should(Condition.visible);
    }

    @Test
    @DisplayName("Mail inside message test")
    public void mainMailTitleTestInside() throws InterruptedException {
        $(By.name("login")).setValue("testlasttest");
        $(byXpath(".//button[@data-testid='enter-password']")).click();
        $(By.name("password")).val("fhfh3864");
        $(byXpath(".//button[@data-testid='login-to-mail']")).click();

        Assert.assertEquals("Проверяем заголовок письма"
                , "Вход с нового устройства в аккаунт"
                , $(byXpath(".//div[@class='dataset__items']/a[1]//span[@class='llc__subject']")).getText());

        $(byXpath(".//div[@class='dataset__items']/a[1]")).shouldHave(text("Вход с нового устройства в аккаунт")).click();

        Assert.assertEquals("Проверяем заголовок внутри письма"
                , "Вход с нового устройства в аккаунт"
                , $(byXpath(".//h2[@class='thread__subject']")).getText());
        Assert.assertEquals("Проверяем отправителя внутри письма"
                , "Mail.ru"
                , $(byXpath(".//span[@class='letter-contact']")).getText());
        Assert.assertEquals("Проверяем e-mail отправителя"
                , "security@id.mail.ru"
                , $(byXpath(".//span[@class='letter-contact']")).getAttribute("title"));
    }

    @Test
    @DisplayName("Mail authentification")
    @Disabled
    public void showsNumberOfUnreadMessages() {
        //RestAssured restAssured = new RestAssured();
        //given().get().body().prettyPrint().
        given().auth()
                .basic(mailUsername, mailPassword)
                .when()
                .get("https://oauth.mail.ru/login")
                .then()
                .assertThat()
                .statusCode(200);
    }
}

