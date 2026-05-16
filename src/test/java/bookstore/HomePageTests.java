package bookstore;


import core.BaseTest;
import core.DriverManager;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HomePageTests extends BaseTest {


    private static final Logger logger = LogManager.getLogger(HomePageTests.class);


    @Test(priority = 1, groups = {"smoke"}, description = "Test homepage elements visibility")
    public void testVerifyHomepage() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());


        logger.info("User login menggunakan credential success user");
        loginPage.login(config.getProperty("emailBookStoreUser"), config.getProperty("passwordBookStoreUser"));


        logger.info("Verify user can see the homepage");
        homePage.navigateToHomepage();
        homePage.verifyLandingOnHomepage();
        homePage.verifyDropdownUserIsDisplayed();
        homePage.verifySortIsDisplayed();
        homePage.verifySearchFunction();
    }
}

