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
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Disabled
public class SellinieumBokingList {
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
    public void viewBookingList() {
        // Entrer les informations d'identification correctes
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By
                .xpath("//input[@type='submit']"));
        emailField.sendKeys("tarbi.achraf@gmail.com");
        // Remplacez par l'email utilisateur
        passwordField.sendKeys("123456");
        // Remplacez par le mot de passe utilisateur
        submitButton.click();
        // Attendre que la redirection ou le composant soit visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));
        WebElement myBookingsButton = driver.findElement(By
                .xpath("//a[@href='/myBookings']")); // Remplacer par l'élément réel du bouton
        myBookingsButton.click();
        // Attendre que l'utilisateur soit redirigé vers la page des réservations
        wait.until(ExpectedConditions.urlContains("/myBookings"));
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement bookingTable = wait1.until(ExpectedConditions.visibilityOfElementLocated(By
                .id("dataTable3")));
        assertNotNull(bookingTable, "Le tableau des réservations doit être présent");
        // Vérifier qu'il y a des réservations affichées dans le tableau
        List<WebElement> rows = driver.findElements(By
                .xpath("//table[@id='dataTable3']/tbody/tr"));
        assertTrue(rows.size() > 0
                , "Il doit y avoir des réservations affichées");
        // Vérifier les informations de réservation dans la première ligne
        WebElement firstBookingCheckInDate = driver.findElement(By
                .xpath("//table[@id='dataTable3']/tbody/tr[1]/td[2]"));
        assertNotNull(firstBookingCheckInDate
                , "La première réservation doit avoir une date d'arrivée");
        System.out.println("Première réservation - Date d'arrivée : "
                + firstBookingCheckInDate.getText());
    }

    @Test
    public void numbrOfHotels(){

        // Entrer les informations d'identification correctes
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));

        emailField.sendKeys("tarbi.achraf@gmail.com"); // Remplacez par l'email utilisateur
        passwordField.sendKeys("123456"); // Remplacez par le mot de passe utilisateur
        submitButton.click();

        // Attendre que la redirection ou le composant soit visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));

        // Vérifier que le nombre d'hôtels dépasse 0
        List<WebElement> hotelCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".hotel-card-view")));
        assertTrue(hotelCards.size() > 0, "Aucun hôtel n'est affiché");

    }

    @Test
    public void viewRoom() throws InterruptedException {
        // Entrer les informations d'identification correctes
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));

        emailField.sendKeys("tarbi.achraf@gmail.com"); // Remplacez par l'email utilisateur
        passwordField.sendKeys("123456"); // Remplacez par le mot de passe utilisateur
        submitButton.click();

        // Attendre que la redirection ou le composant soit visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));
        Thread.sleep(2000);
        WebElement viewRoomLink = driver.findElement(By.xpath("//a[@class='book_now']"));
        viewRoomLink.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/roomTypes"));
        Thread.sleep(2000);

    }

}
