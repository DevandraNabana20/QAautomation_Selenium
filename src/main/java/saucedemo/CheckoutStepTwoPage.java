package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutStepTwoPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CheckoutStepTwoPage.class);

    @FindBy(xpath = "//span[@class='title']")
    private WebElement pageTitle;

    @FindBy(xpath = "//button[@id='finish']")
    private WebElement finishButton;

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Memastikan halaman Checkout Step Two sudah ter-load dengan mengecek finishButton");
        try {
            waitForElementToBeVisible(finishButton);
            return finishButton.isDisplayed();
        } catch (Exception e) {
            logger.error("Gagal memuat halaman Checkout Step Two: " + e.getMessage());
            return false;
        }
    }

    public void clickFinish() {
        logger.info("Klik tombol Finish");
        waitForElementToBeVisible(finishButton);
        waitForElementToBeClickable(finishButton);
        finishButton.click();
    }
}
