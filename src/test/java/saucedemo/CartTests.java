package saucedemo;

import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CartTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTests.class);

    // Helper method untuk login di awal setiap test
    private void loginAndNavigateToInventory(String usernameKey) {
        logger.info("Setup awal: Melakukan login dengan user dari config '{}'", usernameKey);
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty(usernameKey), config.getProperty("password"));
        
        // HARD ASSERT: Memastikan proses login berhasil dan masuk ke halaman inventory
        Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                "Hard Assert Failed: User gagal login dan masuk ke inventory");
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Test menambahkan satu item ke keranjang dan memvalidasinya")
    public void testAddSingleItemToCart() {
        logger.info("======================================================");
        logger.info("Memulai testAddSingleItemToCart");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        String itemName = "Sauce Labs Bike Light";

        logger.info("Action: Menambahkan item '{}' ke dalam keranjang", itemName);
        inventoryPage.clickAddToCartByItemName(itemName);

        // Soft Assert: Memastikan badge di icon cart berubah menjadi 1
        String badgeCount = inventoryPage.getCartBadgeCount();
        logger.info("Validasi (Soft Assert): Cart badge harus '1'. Nilai aktual: {}", badgeCount);
        softAssert.assertEquals(badgeCount, "1", "Cart badge count tidak sesuai setelah add to cart.");

        logger.info("Action: Navigasi ke halaman Cart");
        inventoryPage.clickCartLink();

        CartPage cartPage = new CartPage(DriverManager.getDriver());
        
        // Hard Assert: Memastikan kita benar-benar berada di halaman Cart
        Assert.assertTrue(cartPage.isPageLoaded(), "Hard Assert Failed: Gagal memuat halaman Cart.");

        // Soft Assert: Memastikan item yang ditambahkan ada di dalam Cart
        boolean isItemInCart = cartPage.isItemInCart(itemName);
        logger.info("Validasi (Soft Assert): Item '{}' harus ada di keranjang. Nilai aktual: {}", itemName, isItemInCart);
        softAssert.assertTrue(isItemInCart, "Item " + itemName + " tidak ditemukan di halaman Cart!");

        softAssert.assertAll();
        logger.info("Test testAddSingleItemToCart selesai dan berhasil!");
        logger.info("======================================================");
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test menambahkan beberapa item dan memvalidasinya")
    public void testAddMultipleItemsToCart() {
        logger.info("======================================================");
        logger.info("Memulai testAddMultipleItemsToCart");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Fleece Jacket";

        logger.info("Action: Menambahkan item '{}' dan '{}' ke dalam keranjang", item1, item2);
        inventoryPage.clickAddToCartByItemName(item1);
        inventoryPage.clickAddToCartByItemName(item2);

        // Soft Assert: Memastikan badge di icon cart berubah menjadi 2
        String badgeCount = inventoryPage.getCartBadgeCount();
        logger.info("Validasi (Soft Assert): Cart badge harus '2'. Nilai aktual: {}", badgeCount);
        softAssert.assertEquals(badgeCount, "2", "Cart badge count tidak sesuai setelah menambah 2 item.");

        logger.info("Action: Navigasi ke halaman Cart");
        inventoryPage.clickCartLink();

        CartPage cartPage = new CartPage(DriverManager.getDriver());
        
        // Hard Assert: Memastikan kita benar-benar berada di halaman Cart
        Assert.assertTrue(cartPage.isPageLoaded(), "Hard Assert Failed: Gagal memuat halaman Cart.");

        // Soft Assert: Memastikan kedua item yang ditambahkan ada di dalam Cart
        boolean isItem1InCart = cartPage.isItemInCart(item1);
        boolean isItem2InCart = cartPage.isItemInCart(item2);
        
        logger.info("Validasi (Soft Assert): Item '{}' dan '{}' harus ada di keranjang.", item1, item2);
        softAssert.assertTrue(isItem1InCart, "Item " + item1 + " tidak ditemukan di halaman Cart!");
        softAssert.assertTrue(isItem2InCart, "Item " + item2 + " tidak ditemukan di halaman Cart!");

        softAssert.assertAll();
        logger.info("Test testAddMultipleItemsToCart selesai dan berhasil!");
        logger.info("======================================================");
    }

    @Test(priority = 3, groups = {"smoke"}, description = "Test menghapus item dari inventory page setelah menambahkannya")
    public void testRemoveItemFromInventoryPage() {
        logger.info("======================================================");
        logger.info("Memulai testRemoveItemFromInventoryPage");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        String itemName = "Sauce Labs Bolt T-Shirt";

        logger.info("Action: Menambahkan item '{}' ke dalam keranjang", itemName);
        inventoryPage.clickAddToCartByItemName(itemName);
        
        // Validasi sementara sebelum remove
        String badgeCountAfterAdd = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(badgeCountAfterAdd, "1", "Hard Assert Failed: Item gagal ditambahkan sebelum mencoba remove.");

        logger.info("Action: Klik tombol Remove pada item yang sama");
        inventoryPage.clickRemoveByItemName(itemName);

        // Soft Assert: Memastikan badge kembali menjadi 0 atau hilang (getCartBadgeCount handle ini dan return "0")
        String badgeCountAfterRemove = inventoryPage.getCartBadgeCount();
        logger.info("Validasi (Soft Assert): Cart badge harus '0' (kosong) setelah di-remove. Nilai aktual: {}", badgeCountAfterRemove);
        softAssert.assertEquals(badgeCountAfterRemove, "0", "Cart badge count tidak sesuai setelah remove item.");

        softAssert.assertAll();
        logger.info("Test testRemoveItemFromInventoryPage selesai dan berhasil!");
        logger.info("======================================================");
    }
}
