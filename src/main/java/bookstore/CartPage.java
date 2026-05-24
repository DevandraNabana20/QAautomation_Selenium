package bookstore;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = "#cartQty")
    private WebElement cartQtyInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement updateBtn;

    @FindBy(xpath = "//a[normalize-space()='Delete']")
    private WebElement deleteBtn;

    @FindBy(xpath = "//a[@class='btn btn-expand w-100 mt-3']")
    private WebElement checkoutBtn;

    @FindBy(xpath = "//strong[contains(text(), 'Total')]/span")
    private WebElement totalPriceText;

    @FindBy(xpath = "//h2[contains(text(), 'No items in carts')]")
    private WebElement emptyCartMsg;

    // Optional: List of all item names in cart if there are multiple
    @FindBy(xpath = "//table/tbody/tr/td[2]")
    private List<WebElement> cartItemNames;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isBookInCart(String bookName) {
        // Dinamis xpath untuk mencari text buku di dalam table cart
        String dynamicXpath = "//td[normalize-space()='" + bookName + "']";
        try {
            WebElement book = driver.findElement(By.xpath(dynamicXpath));
            return book.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void updateQuantity(String quantity) {
        scrollToElement(cartQtyInput);
        waitForElementToBeVisible(cartQtyInput);
        cartQtyInput.clear();
        cartQtyInput.sendKeys(quantity);
        
        scrollToElement(updateBtn);
        updateBtn.click();
        // Wait for cart to update total
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickDelete() {
        scrollToElement(deleteBtn);
        waitForElementToBeVisible(deleteBtn);
        deleteBtn.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void proceedToCheckout() {
        scrollToElement(checkoutBtn);
        waitForElementToBeVisible(checkoutBtn);
        checkoutBtn.click();
    }

    public String getTotalPrice() {
        scrollToElement(totalPriceText);
        waitForElementToBeVisible(totalPriceText);
        return totalPriceText.getText().trim();
    }

    public boolean isEmptyCartMessageDisplayed() {
        try {
            scrollToElement(emptyCartMsg);
            waitForElementToBeVisible(emptyCartMsg);
            return emptyCartMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
