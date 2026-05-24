package bookstore;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(xpath = "//h1[@class='mt-3']")
    private WebElement checkoutHeader;

    @FindBy(css = "#name")
    private WebElement nameInput;

    @FindBy(css = "#address")
    private WebElement addressInput;

    @FindBy(css = "#card-name")
    private WebElement cardNameInput;

    @FindBy(css = "#card-number")
    private WebElement cardNumberInput;

    @FindBy(css = "#card-expiry-month")
    private WebElement expiryMonthInput;

    @FindBy(css = "#card-expiry-year")
    private WebElement expiryYearInput;

    @FindBy(css = "#card-cvc")
    private WebElement cvcInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement purchaseBtn;

    @FindBy(xpath = "//b[contains(text(),'Your purchase was successful')]")
    private WebElement successMessage;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutPageDisplayed() {
        try {
            waitForElementToBeVisible(checkoutHeader);
            return checkoutHeader.getText().contains("Checkout");
        } catch (Exception e) {
            return false;
        }
    }

    public void fillBillingDetails(String name, String address) {
        scrollToElement(nameInput);
        waitForElementToBeVisible(nameInput);
        nameInput.sendKeys(name);
        
        scrollToElement(addressInput);
        addressInput.sendKeys(address);
    }

    public void fillCardDetails(String cardName, String cardNumber, String expMonth, String expYear, String cvc) {
        scrollToElement(cardNameInput);
        waitForElementToBeVisible(cardNameInput);
        cardNameInput.sendKeys(cardName);
        
        scrollToElement(cardNumberInput);
        cardNumberInput.sendKeys(cardNumber);
        
        scrollToElement(expiryMonthInput);
        expiryMonthInput.sendKeys(expMonth);
        
        scrollToElement(expiryYearInput);
        expiryYearInput.sendKeys(expYear);
        
        scrollToElement(cvcInput);
        cvcInput.sendKeys(cvc);
    }

    public void clickPurchase() {
        scrollToElement(purchaseBtn);
        waitForElementToBeVisible(purchaseBtn);
        purchaseBtn.click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            waitForElementToBeVisible(successMessage);
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
