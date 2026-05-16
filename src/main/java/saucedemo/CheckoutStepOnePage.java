package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutStepOnePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CheckoutStepOnePage.class);

    @FindBy(xpath = "//span[@class='title']")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Memastikan halaman Checkout Step One sudah ter-load dengan mengecek firstNameInput");
        try {
            waitForElementToBeVisible(firstNameInput);
            return firstNameInput.isDisplayed();
        } catch (Exception e) {
            logger.error("Gagal memuat halaman Checkout Step One: " + e.getMessage());
            return false;
        }
    }

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        logger.info("Mengisi informasi checkout: Nama Depan='{}', Nama Belakang='{}', Kode Pos='{}'", firstName, lastName, postalCode);
        
        waitForElementToBeVisible(firstNameInput);
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
    }

    public void clickContinue() {
        logger.info("Klik tombol Continue");
        waitForElementToBeVisible(continueButton);
        waitForElementToBeClickable(continueButton);
        continueButton.click();
    }
}
