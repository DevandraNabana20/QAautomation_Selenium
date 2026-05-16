package saucedemo;

import core.BaseTest;
import core.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import core.TestUtils;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTests.class);

    @Test(priority = 1, groups = {"smoke"}, description = "Test successful login with standard user")
    public void testLogin() {
        logger.info("Memulai test login dengan credential standard user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential standard user");
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");

        logger.info("Verify user sukses redirected ke halaman inventory");
        Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                "User should be redirected to the inventory page after successful login");

        logger.info("verify tidak ada error message yang ditampilkan setelah login sukses");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        logger.info("TestLogin sudah dijalankan dengan sukses");
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test successful login with problem user")
    public void testLoginProblemUser() {
        logger.info("Memulai test login dengan credential problem user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential problem user");
        loginPage.login(config.getProperty("problemUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");
        
        logger.info("verify tidak ada error message yang ditampilkan setelah login sukses");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        logger.info("TestLoginProblemUser sudah dijalankan dengan sukses");
    }

    @Test(priority = 3, groups = {"smoke"}, description = "Test successful login with performance glitch user")
    public void testLoginPerformanceGlitchUser() {
        logger.info("Memulai test login dengan credential performance glitch user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential performance glitch user");
        loginPage.login(config.getProperty("performanceGlitchUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");
                
        logger.info("verify tidak ada error message yang ditampilkan setelah login sukses");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        logger.info("TestLoginPerformanceGlitchUser sudah dijalankan dengan sukses");
    }

    @Test(priority = 4, groups = {"smoke"}, description = "Test successful login with error user")
    public void testLoginErrorUser() {
        logger.info("Memulai test login dengan credential error user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential error user");
        loginPage.login(config.getProperty("errorUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");
                
        logger.info("verify tidak ada error message yang ditampilkan setelah login sukses");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        logger.info("TestLoginErrorUser sudah dijalankan dengan sukses");
    }

    @Test(priority = 5, groups = {"smoke"}, description = "Test successful login with visual user")
    public void testLoginVisualUser() {
        logger.info("Memulai test login dengan credential visual user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential visual user");
        loginPage.login(config.getProperty("visualUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");
                
        logger.info("verify tidak ada error message yang ditampilkan setelah login sukses");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        logger.info("TestLoginVisualUser sudah dijalankan dengan sukses");
    }

    @Test(priority = 6, groups = {"smoke"}, description = "Test failed login scenario with locked out user")
    public void testFailedLogin() {
        logger.info("Memulai test failed login dengan credential locked out user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential locked out user");
        loginPage.login(config.getProperty("failedUser"), config.getProperty("password"));

        SoftAssert softAssert = new SoftAssert();

        logger.info("Verify pesan error muncul");
        softAssert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "User should see an error message when login fails");

        logger.info("Verify user tidak bisa login ke halaman Products");
        softAssert.assertFalse(loginPage.isUserLoggedInSuccessfully(),
                "User should not be able to access the Products page with invalid credentials");

        logger.info("Verify pesan error mengandung 'locked out'");
        softAssert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "User should be informed that the account has been locked");

        softAssert.assertAll();
        logger.info("TestFailedLogin sudah dijalankan dengan sukses");
    }

    // ----------------------------------------------------------------------------------------------------------------
    /*
    @DataProvider(name = "loginCredentials", parallel = true)
    public Object[][] loginCredentials() {
        return TestUtils.getTestData("src/test/resources/data/login-data-test.xlsx", "login-tests");
    }

    @Test(priority = 7, dataProvider = "loginCredentials", description = "Data-driven login test")
    public void testDataDriven(String username, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        SoftAssert softAssert = new SoftAssert();

        // Mencegah error jika ada cell kosong di Excel
        if (username == null) username = "";
        if (password == null) password = "";

        // eksekusi login
        loginPage.login(username, password);

        // validasi ketika success
        if (expectedResult.equalsIgnoreCase("success")) {

            Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                    "User should be redirected to the inventory page after successful login");

            softAssert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                    "User with username '" + username + "' should be able to login successfully");

            softAssert.assertFalse(loginPage.isErrorMessageDisplayed(),
                    "There should be no error alert box upon successful login.");
        }
        // validasi ketika failed
        else {

            Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                    "User with username '" + username + "' should see an error message");

            softAssert.assertTrue(loginPage.getErrorMessage().contains("Epic sadface"),
                    "Error message is not standard (does not contain the word Epic sadface)");

            softAssert.assertFalse(loginPage.isUserLoggedInSuccessfully(),
                    "User should not be able to access the Products page with invalid credentials");
        }

        // Wajib untuk soft assertions
        softAssert.assertAll();
    }
    */
}
