package bookstore;

import core.BaseTest;
import core.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class HomePageTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomePageTests.class);

    @Test(priority = 1, groups = {"smoke"}, description = "Test homepage elements visibility and default state")
    public void testVerifyHomepageDefaultState() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        logger.info("User login menggunakan credential success user");
        loginPage.login(config.getProperty("emailBookStoreUser"), config.getProperty("passwordBookStoreUser"));

        logger.info("Verify user can see the homepage");
        homePage.navigateToHomepage();
        homePage.verifyLandingOnHomepage();
        homePage.verifyDropdownUserIsDisplayed();
        homePage.verifySortIsDisplayed();
        
        logger.info("Assert that 5 books are displayed by default");
        int expectedBookCount = 5;
        int actualBookCount = homePage.getDisplayedBooksCount();
        Assert.assertEquals(actualBookCount, expectedBookCount, "Expected " + expectedBookCount + " books to be displayed, but found " + actualBookCount);
    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test Search functionality")
    public void testSearchFunctionality() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        
        // Ensure we are on homepage (assuming logged in from previous test or setup, though dependencies might need management in real suite)
        homePage.navigateToHomepage();

        String searchKeyword = "The DevOps Handbook";
        logger.info("Searching for book: " + searchKeyword);
        homePage.searchForBook(searchKeyword);

        logger.info("Asserting search results");
        List<String> displayedTitles = homePage.getDisplayedBookTitles();
        
        Assert.assertTrue(displayedTitles.size() > 0, "Search results should not be empty");
        
        for (String title : displayedTitles) {
            Assert.assertTrue(title.contains(searchKeyword), "Book title '" + title + "' does not contain the search keyword: " + searchKeyword);
        }
        
        // Clear search to reset state
        homePage.clearSearch();
    }

    @Test(priority = 3, groups = {"smoke"}, description = "Test Sort By Price ASC")
    public void testSortByPriceAscending() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomepage();

        logger.info("Selecting Sort By ASC");
        homePage.selectSortByASC();

        logger.info("Retrieving prices to assert ascending order");
        List<Integer> actualPrices = homePage.getDisplayedBookPrices();
        
        // Create a copy and sort it to get the expected sorted list
        List<Integer> expectedSortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedSortedPrices);

        Assert.assertEquals(actualPrices, expectedSortedPrices, "Books are not sorted in Ascending order by price. Actual: " + actualPrices + " Expected: " + expectedSortedPrices);
    }

    @Test(priority = 4, groups = {"smoke"}, description = "Test Sort By Price DESC")
    public void testSortByPriceDescending() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomepage();

        logger.info("Selecting Sort By DESC");
        homePage.selectSortByDESC();

        logger.info("Retrieving prices to assert descending order");
        List<Integer> actualPrices = homePage.getDisplayedBookPrices();
        
        // Create a copy, sort it, and reverse it to get the expected descending sorted list
        List<Integer> expectedSortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedSortedPrices, Collections.reverseOrder());

        Assert.assertEquals(actualPrices, expectedSortedPrices, "Books are not sorted in Descending order by price. Actual: " + actualPrices + " Expected: " + expectedSortedPrices);
    }
}

