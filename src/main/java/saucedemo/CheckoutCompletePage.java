package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutCompletePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CheckoutCompletePage.class);

    @FindBy(xpath = "//span[@class='title']")
    private WebElement pageTitle;

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Memastikan halaman Checkout Complete sudah ter-load dengan mengecek completeHeader");
        try {
            waitForElementToBeVisible(completeHeader);
            return completeHeader.isDisplayed();
        } catch (Exception e) {
            logger.error("Gagal memuat halaman Checkout Complete: " + e.getMessage());
            return false;
        }
    }

    public String getCompleteHeaderText() {
        logger.info("Mengambil teks header dari halaman complete");
        waitForElementToBeVisible(completeHeader);
        return completeHeader.getText();
    }
}
