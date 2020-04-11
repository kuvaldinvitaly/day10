package ru.lanit.atschool.steps;


import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.lanit.atschool.webdriver.WebDriverManager;



public class Hooks {
    private final Logger logger = LogManager.getLogger(getClass());

   @Before
    public void beforeScenario(Scenario scenario){
        logger.info("Run scenario " + scenario.getName());

    }


    @After
    public void afterScenario(Scenario scenario){
        logger.info("Complited scenario " + scenario.getName());
        WebDriverManager.quit();
    }

}
