/**
 * Класс для работы с вэбдрайвером
 * Автор Васильев И.Н. atcc@mail.ru
 * 02.12.2018
 */
package ru.lanit.atschool.webdriver;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import ru.lanit.atschool.helpers.ConfigReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WebDriverManager {

    public static WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(WebDriverManager.class);
    private WebDriverManager() {

    }

    public static WebDriver getDriver(){
        if (driver == null) {
            try {
                System.setProperty("webdriver.chrome.driver", ConfigReader.getStringSystemProperty("chrome.driver.path"));
                ChromeOptions option = new ChromeOptions();
                option.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(option);
                driver.manage().timeouts().implicitlyWait(Long.parseLong(ConfigReader.getStringSystemProperty("implicit.wait")), TimeUnit.SECONDS);
            } catch(UnreachableBrowserException e) {
                logger.error("Невозможно инциализировать драйвер!", e);
            }catch (IOException e) {
                logger.error("Не найден путь до драйвера", e);
            }
        }
        return driver;
    }

    public static void quit() {
        if (driver == null){
            logger.info("Драйвер уже закрыт");
        }
        try {
            driver.quit();
            driver = null;
        } catch (UnreachableBrowserException e) {
            logger.error("Невозможно закрыть браузер!");
        }
    }


}
