package saucedemo;

import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CheckoutTests.class);

    // Helper method untuk login di awal setiap test
    private void loginAndNavigateToInventory(String usernameKey) {
        logger.info("Setup awal: Melakukan login dengan user dari config '{}'", usernameKey);
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty(usernameKey), config.getProperty("password"));
        
        Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                "Hard Assert Failed: User gagal login dan masuk ke inventory");
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Test End to End proses Checkout dengan item di keranjang")
    public void testCheckoutSuccess() {
        logger.info("======================================================");
        logger.info("Memulai testCheckoutSuccess");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        String itemName = "Sauce Labs Backpack";

        // 1. Tambah item ke keranjang
        logger.info("Action: Menambahkan item '{}' ke dalam keranjang", itemName);
        inventoryPage.clickAddToCartByItemName(itemName);

        // 2. Navigasi ke Cart
        logger.info("Action: Navigasi ke halaman Cart");
        inventoryPage.clickCartLink();

        CartPage cartPage = new CartPage(DriverManager.getDriver());
        Assert.assertTrue(cartPage.isPageLoaded(), "Gagal memuat halaman Cart.");

        // 3. Klik Checkout
        cartPage.clickCheckout();

        // 4. Isi Form Checkout Step One
        CheckoutStepOnePage stepOnePage = new CheckoutStepOnePage(DriverManager.getDriver());
        Assert.assertTrue(stepOnePage.isPageLoaded(), "Gagal memuat halaman Checkout Step One.");
        
        stepOnePage.fillCheckoutInformation("Devandra", "Nabana", "12345");
        stepOnePage.clickContinue();

        // 5. Validasi di Checkout Step Two dan Finish
        CheckoutStepTwoPage stepTwoPage = new CheckoutStepTwoPage(DriverManager.getDriver());
        Assert.assertTrue(stepTwoPage.isPageLoaded(), "Gagal memuat halaman Checkout Step Two.");
        
        stepTwoPage.clickFinish();

        // 6. Validasi Pesanan Berhasil
        CheckoutCompletePage completePage = new CheckoutCompletePage(DriverManager.getDriver());
        Assert.assertTrue(completePage.isPageLoaded(), "Gagal memuat halaman Checkout Complete.");
        
        String headerText = completePage.getCompleteHeaderText();
        Assert.assertEquals(headerText, "Thank you for your order!", "Teks penyelesaian order tidak sesuai.");

        logger.info("Test testCheckoutSuccess selesai dan berhasil!");
        logger.info("======================================================");
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Negative Test: Melakukan Checkout tanpa ada item di keranjang")
    public void testCheckoutEmptyCart() {
        logger.info("======================================================");
        logger.info("Memulai testCheckoutEmptyCart (Negative Test)");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());

        // 1. Navigasi ke Cart (Tanpa menambah item)
        logger.info("Action: Navigasi ke halaman Cart tanpa menambahkan item");
        inventoryPage.clickCartLink();

        CartPage cartPage = new CartPage(DriverManager.getDriver());
        Assert.assertTrue(cartPage.isPageLoaded(), "Gagal memuat halaman Cart.");

        // 2. Klik Checkout
        cartPage.clickCheckout();

        // 3. Validasi: Seharusnya tidak bisa lanjut ke Checkout Step One jika keranjang kosong.
        // Karena ini adalah negative test case untuk fitur yang sebenarnya "buggy" (bisa checkout tanpa item),
        // Kita akan melakukan assert bahwa kita HARUSNYA tetap di halaman Cart atau muncul error.
        // Di sini kita cek apakah halaman beralih ke Step One. Jika beralih, test akan gagal untuk menyoroti bug.
        
        CheckoutStepOnePage stepOnePage = new CheckoutStepOnePage(DriverManager.getDriver());
        
        // Assert false: Kita berekspektasi bahwa isPageLoaded untuk step one mengembalikan FALSE (artinya tidak bisa masuk step one).
        // Tapi pada kenyataannya, di SauceDemo bug ini ada, sehingga akan return TRUE, dan Assert.assertFalse akan membuat test FAILED (yang mana benar untuk menyoroti bug).
        Assert.assertFalse(stepOnePage.isPageLoaded(), "BUG: User bisa melakukan checkout padahal keranjang kosong!");

        logger.info("Test testCheckoutEmptyCart selesai!");
        logger.info("======================================================");
    }
}
