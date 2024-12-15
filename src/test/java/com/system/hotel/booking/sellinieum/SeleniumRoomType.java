package com.system.hotel.booking.sellinieum;

import com.system.hotel.booking.config.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Disabled
public class SeleniumRoomType {
    private WebDriver driver;
    private SeleniumConfig seleniumConfig;

    @BeforeEach
    public void setUp() {
        seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
        driver.get("http://localhost:3000"); // Changez l'URL si nécessaire
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void book() {
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

        // Vérifier que l'utilisateur a bien été redirigé vers la page d'accueil
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/home", currentUrl);


    }

    @Disabled
    @Test
    public void searchHotelInParisNamedHotelParadise() throws InterruptedException {

        // Étape 1 : Remplir le formulaire de login
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));

        emailField.sendKeys("tarbi.achraf@gmail.com"); // Remplacer par un email valide
        passwordField.sendKeys("123456"); // Remplacer par un mot de passe valide
        submitButton.click();

        // Étape 2 : Attendre la redirection vers la page d'accueil
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/home"));

        // Vérifier que nous sommes bien sur la page d'accueil
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.equals("http://localhost:3000/home"), "Redirection vers /home échouée.");

        // Étape 3 : Remplir les champs de recherche
        // Remarquez que les deux champs ont le même ID "search", nous allons donc utiliser les attributs placeholder pour les différencier

        // Remplir le champ "Location" avec "Paris"
        WebElement locationField = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Location']")));
        locationField.sendKeys("Paris");

        // Remplir le champ "Hotel Name" avec "Hotel Paradise"
        WebElement hotelNameField = driver.findElement(By.xpath("//input[@placeholder='Hotel Name']"));
        hotelNameField.sendKeys("Hotel Paradise");

        // Étape 4 : Cliquer sur le bouton "SEARCH"
        WebElement searchButton = driver.findElement(By.xpath("//button[@class='btn-search']"));
        searchButton.click();

        Thread.sleep(3000);
        // Étape 5 : Attendre que les résultats de recherche soient affichés
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("hotel-row")));

        // Étape 6 : Vérifier que le nombre d'hôtels affichés dépasse 0
        List<WebElement> hotelCards = driver.findElements(By.cssSelector(".hotel-card-view"));
        assertTrue(hotelCards.size() > 0, "Aucun hôtel trouvé pour les critères de recherche.");

        // Étape 7 : Optionnel - Vérifier que chaque hôtel affiché correspond aux critères
        for (WebElement hotelCard : hotelCards) {
            // Vérifier que le nom de l'hôtel contient "Hotel Paradise"
            WebElement hotelNameElement = hotelCard.findElement(By.tagName("h3"));
            String hotelName = hotelNameElement.getText();
            assertTrue(hotelName.contains("Hotel Paradise".toUpperCase()), "L'hôtel trouvé ne correspond pas au nom recherché.");

        }

        // Étape 8 : Cliquer sur le lien "View Rooms" du premier hôtel trouvé
        WebElement firstHotelCard = hotelCards.get(0);
        WebElement viewRoomsLink = firstHotelCard.findElement(By.xpath(".//a[@class='book_now']"));
        viewRoomsLink.click();

        // Étape 9 : Attendre la redirection vers la page des types de chambres
        wait.until(ExpectedConditions.urlContains("/roomTypes"));

        // Vérifier que nous sommes bien sur la page des types de chambres
        String roomTypesUrl = driver.getCurrentUrl();
        assertTrue(roomTypesUrl.contains("/roomTypes"), "Redirection vers /roomTypes échouée.");

        Thread.sleep(2000);
        WebElement bookNowButton = driver.findElement(By.xpath("//a[contains(text(), 'BOOK NOW')]"));
        bookNowButton.click();

        // Vérifier la redirection vers la page "BookRoom"
        if (driver.getCurrentUrl().equals("http://localhost:3000/BookRoom")) {
            System.out.println("Redirection réussie vers la page BookRoom.");


            WebElement phoneField = driver.findElement(By.name("numOfGuest")); // Exemple : champ Téléphone
            phoneField.sendKeys("1");


            WebElement checkInDate = driver.findElement(By.name("date1"));
            checkInDate.clear();
            Thread.sleep(2000);
            checkInDate.sendKeys("14-Aug-2024");

            WebElement checkOutDate = driver.findElement(By.name("date2"));
            checkOutDate.clear();
            Thread.sleep(2000);
            checkOutDate.sendKeys("16-Aug-2024");

// Thread.sleep(2000);
            // Soumettre le formulaire
            WebElement submitButton2 = driver.findElement(By.xpath("(//button[contains(text(), 'Make Your Reservation')])[1]"));
            submitButton2.click();

            Thread.sleep(5000);

            WebElement cardNumberInput = driver.findElement(By.cssSelector("input[placeholder='Valid Card Number']"));
            cardNumberInput.sendKeys("4111111111111111");

             //Saisir la date d'expiration
            WebElement expiryDateInput = driver.findElement(By.cssSelector("input[placeholder='MM / YY']"));
            expiryDateInput.sendKeys("12/25");

            // Saisir le CVV
            WebElement cvvInput = driver.findElement(By.cssSelector("input[placeholder='CVV']"));
            cvvInput.sendKeys("123");

            // Saisir le nom du propriétaire de la carte
            WebElement cardOwnerInput = driver.findElement(By.cssSelector("input[placeholder='Card Owner Names']"));
            cardOwnerInput.sendKeys("Tarbi");

        }
    }
}