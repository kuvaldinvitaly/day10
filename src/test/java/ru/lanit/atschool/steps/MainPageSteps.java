package ru.lanit.atschool.steps;


import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.lanit.atschool.helpers.ConfigReader;
import ru.lanit.atschool.pages.MainPage;
import ru.lanit.atschool.webdriver.WebDriverManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MainPageSteps {
    private WebDriver driver = WebDriverManager.getDriver();
    private MainPage mainPage = new MainPage();
    private final Logger logger = LogManager.getLogger(getClass());


    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] saveScreenshot(String title){
        logger.info("Create screenshot, step : {" + title + "}");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Пусть("открыт браузер и введен адрес$")
    public void openedBrowserAndEnteredUrl() throws IOException {
        mainPage.openPage(ConfigReader.getStringSystemProperty("url"));
        String title = driver.getTitle();
        Assert.assertEquals(title, "Lanit education", "Test not complited");
        logger.info("Step openedBrowserAndEnteredUrl complited");
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("openedBrowserAndEnteredUrl")));
    }

    @И("переходим в раздел Категории")
    public void goToCategories() {
        mainPage.btnCategiries.click();
        String title = driver.getTitle();
        Assert.assertEquals(title, "Категории | Lanit education", "Test goToCategories not complited");
        logger.info("Step goToCategories complited");
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("goToCategories")));
    }

    @И("переходим в раздел Пользователи")
    public void goToUsers(){
        mainPage.btnUsers.click();
        String title = driver.getTitle();
        Assert.assertEquals(title, "Top posters | Пользователи | Lanit education", "Test goToUsers not complited");
        logger.info("Step goToUsers complited");
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("goToUsers")));
    }

    @И("выполняем поиск пользователя из предусловия")
    public void searchForUserFromPrecondition() throws IOException {
        mainPage.buttonSearchToSite().click();
        mainPage.searchField.sendKeys(ConfigReader.getStringSystemProperty("name.user"));
        mainPage.btnShowFullSearchResults.click();
        String title = driver.getTitle();
        Assert.assertEquals(title, "Поиск по сайту | Lanit education", "Test searchForUserFromPrecondition not complited");
        logger.info("Step searchForUserFromPrecondition complited");
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("searchForUserFromPrecondition")));
    }

    @Тогда("пробуем выполнить регистрацию с пустыми полями")
    public void registratedZeroField() throws IOException {
        mainPage.btnRegistration.click();
        mainPage.btnRegistration.isDisplayed();
        driver.manage().timeouts().implicitlyWait(Long.parseLong(ConfigReader.getStringSystemProperty("implicit.wait")), TimeUnit.SECONDS);
       mainPage.clickToNameUser.click();
        driver.manage().timeouts().implicitlyWait(Long.parseLong(ConfigReader.getStringSystemProperty("implicit.wait")), TimeUnit.SECONDS);
        WebDriverWait webDriverWait1 = new WebDriverWait(driver, 10);
        webDriverWait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[class=\"help-block errors\"]")));
        checkFieldsZero("id_username");
        checkFieldsZero("id_email");
        checkFieldsZero("id_password");
        logger.info("Step registratedZeroField complited");
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("registratedZeroField")));
    }

    private void checkFieldsZero(String id){
        WebElement field = driver.findElement(By.id(id));
        WebElement fieldParrent = field.findElement(By.xpath(".."));
        String fielsError = fieldParrent.findElement(By.cssSelector("p")).getText();
        Assert.assertEquals(fielsError, "Это поле обязательно.", "Test registratedZeroField not complited");
    }

    @Тогда("пробуем выполнить регистрацию существующем пользователем")
    public void registratedExistingUser() throws IOException {
        fillFields("id_username","kuvaldinvitaly");
        Random random = new Random();
        int n = random.nextInt(100) + 1;
        String email = "kuvaldinvitaly" + n + ConfigReader.getStringSystemProperty("email");
        fillFields("id_email",email);
        fillFields("id_password", n + "veryhardpassword");
        mainPage.clickToNameUser.click();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[class=\"help-block errors\"]")));
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("registratedExistingUser")));
        checkFieldUserName("id_username");
        mainPage.btnCancellation.click();
        logger.info("Step registratedExistingUser complited");
    }

    private void fillFields(String id, String text){
        WebElement field = driver.findElement(By.id(id));
        field.sendKeys(text);
    }

    private void checkFieldUserName(String id)  {
        WebElement field = driver.findElement(By.id(id));
        WebElement fieldParrent = field.findElement(By.xpath(".."));
        String fielsError = fieldParrent.findElement(By.cssSelector("p")).getText();
        Assert.assertEquals(fielsError, "Данное имя пользователя недоступно.", "Test registratedExistingUser not complited");
    }

    @И("пробуем зарегестрировать нового пользователя")
    public void redistratedNewUser() throws IOException {
        mainPage.btnRegistration.click();
        Random random = new Random();
        int n = random.nextInt(10000) - 1;
        fillFields("id_username","TestUser" + n);
        String email = "newTestUser" + n + ConfigReader.getStringSystemProperty("email");
        fillFields("id_email",email);
        fillFields("id_password", n + "veryhardpassword");
        mainPage.clickToNameUser.click();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[class=\"lead\"]")));
        String reloadPage = driver.findElement(By.xpath("//*[@class=\"lead\"]")).getText();
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(saveScreenshot("redistratedNewUser")));
        Assert.assertTrue(reloadPage.contains("Вы вошли как"), "Test redistratedNewUser not complited");
        mainPage.btnReloadField.click();
        logger.info("Step redistratedNewUser complited");
    }
}
