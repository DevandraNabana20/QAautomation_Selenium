package bookstore;

import core.BaseTest;
import core.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckoutTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CheckoutTests.class);

    // Helper method to setup cart state and navigate to checkout
    private void setupCartAndProceedToCheckout(HomePage homePage, LoginPage loginPage, CartPage cartPage) {
        logger.info("User login");
        loginPage.login(config.getProperty("emailBookStoreUser"), config.getProperty("passwordBookStoreUser"));

        homePage.navigateToHomepage();
        logger.info("Adding book index 0 to cart");
        homePage.clickAddToCartByIndex(0);
        
        logger.info("Navigate to Cart");
        homePage.navigateToCart();
        
        logger.info("Proceeding to Checkout from Cart");
        cartPage.proceedToCheckout();
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Test End to End Checkout Success")
    public void testCheckoutSuccess() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        CartPage cartPage = new CartPage(DriverManager.getDriver());
        CheckoutPage checkoutPage = new CheckoutPage(DriverManager.getDriver());

        // 1. Setup Data: Login -> Homepage -> Add Item -> Cart -> Proceed to Checkout
        setupCartAndProceedToCheckout(homePage, loginPage, cartPage);

        // 2. Verify Landed on Checkout Page
        logger.info("Verify we are on the Checkout Page");
        Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed(), "Checkout page header is not displayed");

        // 3. Fill Billing Details with User Name
        logger.info("Filling Billing Details");
        checkoutPage.fillBillingDetails("Devandra", "123 Automation Street, Jakarta");

        // 4. Fill Card Details (Using future date 12/2030 and valid mock card)
        logger.info("Filling Card Details");
        checkoutPage.fillCardDetails("Devandra Nabana", "4242424242424242", "12", "2030", "123");

        // 5. Submit Order
        logger.info("Clicking Purchase button");
        checkoutPage.clickPurchase();

        // 6. Assert Success Message
        logger.info("Verify Success Message on Profile/History Page");
        Assert.assertTrue(checkoutPage.isSuccessMessageDisplayed(), "Order success message was not displayed after purchase");
    }
}
