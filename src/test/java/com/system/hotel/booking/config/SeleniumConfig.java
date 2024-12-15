package com.system.hotel.booking.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumConfig {
    private WebDriver driver;

    public SeleniumConfig() {
        try {
            // Configure WebDriverManager
            WebDriverManager.chromedriver().setup();

            // Configure Chrome options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // Start ChromeDriver with explicit service
            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingPort(9515)
                    .build();
            this.driver = new ChromeDriver(service, options);

            // Maximize window
            this.driver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de Selenium", e);
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
