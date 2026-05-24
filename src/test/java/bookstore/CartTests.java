package bookstore;

import core.BaseTest;
import core.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CartTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTests.class);

    // Helper method to setup cart state since browser resets every test
    private void setupCartState(HomePage homePage, LoginPage loginPage) {
        logger.info("User login");
        loginPage.login(config.getProperty("emailBookStoreUser"), config.getProperty("passwordBookStoreUser"));

        homePage.navigateToHomepage();
        logger.info("Adding book index 0 to cart to setup test state");
        homePage.clickAddToCartByIndex(0);
        homePage.navigateToCart();
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Test Add Item to Cart and verify Badge")
    public void testAddToCartFlow() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        CartPage cartPage = new CartPage(DriverManager.getDriver());

        logger.info("User login");
        loginPage.login(config.getProperty("emailBookStoreUser"), config.getProperty("passwordBookStoreUser"));

        homePage.navigateToHomepage();
        
        // Ambil judul buku index 0 untuk diverifikasi nanti di cart
        String expectedBookTitle = homePage.getBookTitleByIndex(0);
        logger.info("Adding book to cart: " + expectedBookTitle);
        
        // Klik Add to Cart
        homePage.clickAddToCartByIndex(0);

        logger.info("Assert cart badge updated to 1");
        String badgeCount = homePage.getCartBadgeCount();
        Assert.assertEquals(badgeCount, "1", "Cart badge should display 1");

        logger.info("Navigate to Cart Page");
        homePage.navigateToCart();

        logger.info("Verify book is actually inside the Cart");
        boolean isBookPresent = cartPage.isBookInCart(expectedBookTitle);
        Assert.assertTrue(isBookPresent, "The book '" + expectedBookTitle + "' was not found in the cart");
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test Update Cart Item Quantity")
    public void testUpdateCartQuantity() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        CartPage cartPage = new CartPage(DriverManager.getDriver());
        
        // Setup state: Login & Add item
        setupCartState(homePage, loginPage);

        logger.info("Get initial total price");
        String initialPrice = cartPage.getTotalPrice();
        
        logger.info("Update item quantity to 2");
        cartPage.updateQuantity("2");

        logger.info("Verify total price has changed");
        String updatedPrice = cartPage.getTotalPrice();
        Assert.assertNotEquals(initialPrice, updatedPrice, "Total price should be updated after changing quantity");
    }

    @Test(priority = 3, groups = {"smoke"}, description = "Test Delete Cart Item")
    public void testDeleteCartItem() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        CartPage cartPage = new CartPage(DriverManager.getDriver());
        
        // Setup state: Login & Add item
        setupCartState(homePage, loginPage);

        logger.info("Click Delete item from cart");
        cartPage.clickDelete();

        logger.info("Verify empty cart message is displayed");
        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message should be displayed after deleting the item");
    }
}