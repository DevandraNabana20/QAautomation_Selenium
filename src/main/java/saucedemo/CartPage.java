package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    @FindBy(xpath = "//span[@class='title']")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Memastikan halaman Cart sudah ter-load dengan mengecek checkoutButton");
        try {
            waitForElementToBeVisible(checkoutButton);
            return checkoutButton.isDisplayed();
        } catch (Exception e) {
            logger.error("Gagal memuat halaman Cart: " + e.getMessage());
            return false;
        }
    }

    public boolean isItemInCart(String itemName) {
        logger.info("Mengecek apakah item '{}' ada di dalam keranjang", itemName);
        wait.until(ExpectedConditions.visibilityOfAllElements(cartItemNames));

        for (WebElement item : cartItemNames) {
            if (item.getText().equalsIgnoreCase(itemName)) {
                logger.info("Item '{}' ditemukan di dalam keranjang", itemName);
                return true;
            }
        }
        logger.error("Item '{}' tidak ditemukan di dalam keranjang", itemName);
        return false;
    }

    public void clickCheckout() {
        logger.info("Klik tombol Checkout");
        waitForElementToBeVisible(checkoutButton);
        waitForElementToBeClickable(checkoutButton);
        checkoutButton.click();
    }
}
