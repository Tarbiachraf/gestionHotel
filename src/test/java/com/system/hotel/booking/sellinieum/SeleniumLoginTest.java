package com.system.hotel.booking.sellinieum;

import com.system.hotel.booking.config.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class SeleniumLoginTest {

    private WebDriver driver;
    private SeleniumConfig seleniumConfig;

    @BeforeEach
    public void setUp() {
        seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
        driver.get("http://localhost:3000/login"); // Changez l'URL si nécessaire
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void loginSuccess() {
        // Entrer les informations d'identification correctes
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.
                xpath("//input[@type='submit']"));
        emailField.sendKeys("tarbi.achraf@gmail.com");
        // Remplacez par l'email utilisateur
        passwordField.sendKeys("123456");
        // Remplacez par le mot de passe utilisateur
        submitButton.click();
        // Attendre que la redirection ou le composant soit visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));
        // Vérifier que l'utilisateur a bien été redirigé vers la page d'accueil
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/home", currentUrl);
    }

    @Test
    public void loginFailure() {
        // Entrer des informations d'identification incorrectes
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.
                xpath("//input[@type='submit']"));
        emailField.sendKeys("fakeuser@example.com");
        passwordField.sendKeys("wrongpassword");
        submitButton.click();
        // Attendre l'apparition de l'alerte
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        // Capturer l'alerte et valider son texte
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        // Vérifier que le message de l'alerte est correct
        assertEquals("Please check email and password", alertText);
        // Accepter l'alerte
        alert.accept();
    }
}