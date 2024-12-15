package com.system.hotel.booking.sellinieum;

import com.system.hotel.booking.config.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Disabled
public class SeleniumTestSignUp {
    private WebDriver driver;
    private SeleniumConfig seleniumConfig;

    @BeforeEach
    public void setUp() {
        seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
        driver.get("http://localhost:3000/signup"); // Changez l'URL si nécessaire
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void signUpTest(){
        WebElement usernameField = driver.findElement(By.id("login__username"));
        WebElement emailField = driver.findElement(By.id("login__email"));
        WebElement passwordField = driver.findElement(By.id("login__password"));
        WebElement submitButton = driver.findElement(
                By.xpath("//input[@type='submit']"));
        // Remplir les champs avec des données fictives
        usernameField.sendKeys("TestUser");
        emailField.sendKeys("testuser@example.com");
        passwordField.sendKeys("Test@123");
        // Soumettre le formulaire
        submitButton.click();
        // Attendre que la redirection ou un élément spécifique de la page cible apparaisse
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));
        // Vérifier que l'utilisateur est redirigé vers la bonne page
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl.equals("http://localhost:3000/home") : "Redirection échouée.";
    }
}
