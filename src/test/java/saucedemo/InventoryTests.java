package saucedemo;

import core.BaseTest;
import core.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(InventoryTests.class);

    // Helper method untuk login di awal setiap test
    private void loginAndNavigateToInventory(String usernameKey) {
        logger.info("Setup awal: Melakukan login dengan user dari config '{}'", usernameKey);
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty(usernameKey), config.getProperty("password"));
        
        // HARD ASSERT: Memastikan proses login berhasil dan masuk ke halaman inventory
        // Jika ini gagal, test akan langsung berhenti (tidak melanjutkan ke soft assert)
        Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                "Hard Assert Failed: User gagal login dan masuk ke inventory");
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Test sorting Name (Z to A) works correctly for standard user")
    public void testSortingZtoAStandardUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingZtoAStandardUser");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'za' (Name Z to A)");
        inventoryPage.selectSortOptionByValue("za");

        List<String> actualNames = inventoryPage.getItemNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        logger.info("Soft Assertion: Memastikan urutan nama item sesuai dari Z ke A");
        softAssert.assertEquals(actualNames, expectedNames, "Sorting dari Z to A tidak bekerja sebagaimana mestinya.");
        
        softAssert.assertAll();
        logger.info("Test testSortingZtoAStandardUser berhasil dan urutan sudah benar!");
        logger.info("======================================================");
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test sorting Price (Low to High) works correctly for standard user")
    public void testSortingPriceLowToHighStandardUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingPriceLowToHighStandardUser");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'lohi' (Price Low to High)");
        inventoryPage.selectSortOptionByValue("lohi");

        List<Double> actualPrices = inventoryPage.getItemPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        logger.info("Soft Assertion: Memastikan urutan harga item sesuai dari rendah ke tinggi");
        softAssert.assertEquals(actualPrices, expectedPrices, "Sorting harga Low to High tidak bekerja sebagaimana mestinya.");
        
        softAssert.assertAll();
        logger.info("Test testSortingPriceLowToHighStandardUser berhasil dan urutan harga sudah benar!");
        logger.info("======================================================");
    }

    @Test(priority = 3, groups = {"smoke"}, description = "Test sorting Price (High to Low) works correctly for standard user")
    public void testSortingPriceHighToLowStandardUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingPriceHighToLowStandardUser");
        loginAndNavigateToInventory("standardUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'hilo' (Price High to Low)");
        inventoryPage.selectSortOptionByValue("hilo");

        List<Double> actualPrices = inventoryPage.getItemPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());

        logger.info("Soft Assertion: Memastikan urutan harga item sesuai dari tinggi ke rendah");
        softAssert.assertEquals(actualPrices, expectedPrices, "Sorting harga High to Low tidak bekerja sebagaimana mestinya.");
        
        softAssert.assertAll();
        logger.info("Test testSortingPriceHighToLowStandardUser berhasil dan urutan harga sudah benar!");
        logger.info("======================================================");
    }

    @Test(priority = 4, groups = {"smoke"}, description = "Test sorting Name (Z to A) for problem_user")
    public void testSortingZtoAProblemUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingZtoAProblemUser");
        loginAndNavigateToInventory("problemUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'za' (Name Z to A) dengan problem_user");
        inventoryPage.selectSortOptionByValue("za");

        List<String> actualNames = inventoryPage.getItemNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        logger.info("Soft Assertion: Ekspektasi kita sorting Z to A harusnya BERHASIL");
        softAssert.assertEquals(actualNames, expectedNames, "Bug Detected: Sorting Name Z to A gagal untuk problem_user!");
        
        softAssert.assertAll();
        logger.info("======================================================");
    }

    @Test(priority = 5, groups = {"smoke"}, description = "Test sorting Name (Z to A) for error_user")
    public void testSortingZtoAErrorUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingZtoAErrorUser");
        loginAndNavigateToInventory("errorUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'za' (Name Z to A) dengan error_user");
        inventoryPage.selectSortOptionByValue("za");

        logger.info("Mencoba mengambil nama item. Jika ada alert error dari web, ini akan divalidasi.");
        try {
            List<String> actualNames = inventoryPage.getItemNames();
            List<String> expectedNames = new ArrayList<>(actualNames);
            expectedNames.sort(Collections.reverseOrder());

            logger.info("Soft Assertion: Ekspektasi kita sorting Z to A harusnya BERHASIL");
            softAssert.assertEquals(actualNames, expectedNames, "Bug Detected: Sorting Name Z to A gagal untuk error_user!");
        } catch (org.openqa.selenium.UnhandledAlertException e) {
            String alertText = "Alert text tidak terdeteksi";
            try {
                alertText = e.getAlertText(); // Coba ambil dari Exception dulu
                if (alertText == null) {
                    alertText = DriverManager.getDriver().switchTo().alert().getText();
                }
                logger.error("Bug Tertangkap! Muncul Alert tidak terduga: {}", alertText);
                DriverManager.getDriver().switchTo().alert().accept();
            } catch (org.openqa.selenium.NoAlertPresentException noAlert) {
                logger.warn("Alert sudah hilang/ditutup otomatis oleh browser capabilites.");
            }
            
            // Kita gagalkan testnya secara rapi
            Assert.fail("Bug Detected: Muncul Alert error '" + alertText + "' saat mencoba sorting!");
        }
        
        softAssert.assertAll();
        logger.info("======================================================");
    }

    @Test(priority = 6, groups = {"smoke"}, description = "Test sorting Name (Z to A) for visual_user")
    public void testSortingZtoAVisualUser() {
        logger.info("======================================================");
        logger.info("Memulai testSortingZtoAVisualUser");
        loginAndNavigateToInventory("visualUser");

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        logger.info("Action: Memilih opsi sort 'za' (Name Z to A) dengan visual_user");
        inventoryPage.selectSortOptionByValue("za");

        List<String> actualNames = inventoryPage.getItemNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        logger.info("Soft Assertion: Ekspektasi kita sorting Z to A harusnya BERHASIL");
        softAssert.assertEquals(actualNames, expectedNames, "Bug Detected: Sorting Name Z to A gagal untuk visual_user!");
        
        softAssert.assertAll();
        logger.info("======================================================");
    }

}