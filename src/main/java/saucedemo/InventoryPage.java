package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(InventoryPage.class);

    @FindBy(css = "select.product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement cartLink;

    @FindBy(xpath = "//span[@class='shopping_cart_badge']")
    private WebElement cartBadge;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void selectSortOptionByValue(String value) {
        logger.info("Memilih opsi sorting dengan value: {}", value);
        waitForElementToBeVisible(sortDropdown);
        Select select = new Select(sortDropdown);
        select.selectByValue(value);
        logger.info("Opsi sorting '{}' berhasil dipilih", value);
    }

    public List<String> getItemNames() {
        logger.info("Menunggu elemen itemNames selesai di-render");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfAllElements(itemNames));

        logger.info("Mengambil semua nama item yang tampil di halaman");
        List<String> names = itemNames.stream().map(WebElement::getText).collect(Collectors.toList());
        logger.debug("Item names retrieved: {}", names);
        return names;
    }

    public List<Double> getItemPrices() {
        logger.info("Menunggu elemen itemPrices selesai di-render");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfAllElements(itemPrices));

        logger.info("Mengambil semua harga item yang tampil di halaman");
        List<Double> prices = itemPrices.stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
        logger.debug("Item prices retrieved: {}", prices);
        return prices;
    }

    // --- Tambahan untuk Fitur Cart ---

    public void clickAddToCartByItemName(String itemName) {
        logger.info("Mencoba klik button Add to cart untuk item: {}", itemName);
        // Mengubah "Sauce Labs Bike Light" menjadi "sauce-labs-bike-light"
        String formattedName = itemName.toLowerCase().replace(" ", "-");
        By addToCartLocator = By.id("add-to-cart-" + formattedName);
        
        // Handling dynamic element menggunakan driver.findElement
        WebElement addToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartLocator));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        logger.info("Berhasil klik Add to cart untuk item: {}", itemName);
    }

    public void clickRemoveByItemName(String itemName) {
        logger.info("Mencoba klik button Remove untuk item: {}", itemName);
        String formattedName = itemName.toLowerCase().replace(" ", "-");
        By removeLocator = By.id("remove-" + formattedName);
        
        WebElement removeButton = wait.until(ExpectedConditions.presenceOfElementLocated(removeLocator));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        logger.info("Berhasil klik Remove untuk item: {}", itemName);
    }

    public String getCartBadgeCount() {
        logger.info("Mengambil jumlah item pada icon cart badge");
        try {
            waitForElementToBeVisible(cartBadge);
            return cartBadge.getText();
        } catch (Exception e) {
            logger.info("Cart badge tidak ditemukan (kemungkinan kosong).");
            return "0";
        }
    }

    public void clickCartLink() {
        logger.info("Klik icon keranjang belanja (cart link)");
        waitForElementToBeVisible(cartLink);
        waitForElementToBeClickable(cartLink);
        cartLink.click();
    }
}
