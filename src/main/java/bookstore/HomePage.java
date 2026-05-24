package bookstore;


import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {


    @FindBy(xpath = "//h1[@class='mt-3']")
    private WebElement titleHomePage;


    @FindBy(css = ".filter_sort-select--label")
    private WebElement filterSortPrice;


    @FindBy(xpath = "//a[normalize-space()='Sort By ASC']")
    private WebElement sortByASC;


    @FindBy(xpath = "//a[normalize-space()='Sort By DESC']")
    private WebElement sortByDESC;


    @FindBy(xpath = "//input[@id='search-input']")
    private WebElement searchInput;


    @FindBy(xpath = "//button[@id='search-btn']")
    private WebElement searchButton;


    @FindBy(xpath = "(//a[@id='navbarDropdown'])[last()]")
    private WebElement dropdownUser;


    @FindBy(xpath = "//a[@id='profile']")
    private WebElement profileCta;


    @FindBy(xpath = "//a[@id='logout']")
    private WebElement logoutCta;


    @FindBy(xpath = "//a[contains(text(),'E-commerce Bookstore')]")
    private WebElement homepageEntryPoint;

    // --- New Elements based on User Input ---
    @FindBy(css = ".card-title.ms-1")
    private List<WebElement> bookTitles;

    @FindBy(css = ".card-text.ms-1.mt-1")
    private List<WebElement> bookPrices;

    @FindBy(css = ".btn.btn-expand.w-100.mt-1.mb-2.ms-1.me-1")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".card-img-top")
    private List<WebElement> bookImages;

    // --- Cart Elements ---
    @FindBy(xpath = "//img[@alt='Cart']")
    private WebElement cartIcon;

    @FindBy(xpath = "//span[contains(@class, 'badge bg-danger')]")
    private WebElement cartBadge;


    public HomePage(WebDriver driver) {
        super(driver);
    }


    public void verifySortIsDisplayed() {
        waitForElementToBeVisible(filterSortPrice);
        filterSortPrice.click();
        waitForElementToBeVisible(sortByASC);
        waitForElementToBeVisible(sortByDESC);
    }

    public void selectSortByASC() {
        scrollToElement(filterSortPrice);
        waitForElementToBeVisible(filterSortPrice);
        filterSortPrice.click();
        waitForElementToBeVisible(sortByASC);
        scrollToElement(sortByASC);
        sortByASC.click();
        // Wait a bit for the sorting to apply (you might need a better synchronization strategy depending on the app)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectSortByDESC() {
        scrollToElement(filterSortPrice);
        waitForElementToBeVisible(filterSortPrice);
        filterSortPrice.click();
        waitForElementToBeVisible(sortByDESC);
        scrollToElement(sortByDESC);
        sortByDESC.click();
        // Wait a bit for the sorting to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void verifyDropdownUserIsDisplayed() {
        scrollToElement(dropdownUser);
        waitForElementToBeVisible(dropdownUser);
        dropdownUser.click();
        waitForElementToBeVisible(profileCta);
        waitForElementToBeVisible(logoutCta);
    }


    public void verifySearchFunction(){
        scrollToElement(searchInput);
        waitForElementToBeVisible(searchInput);
        searchInput.sendKeys("The DevOps Handbook");
        scrollToElement(searchButton);
        searchButton.click();
    }

    public void searchForBook(String bookName) {
        scrollToElement(searchInput);
        waitForElementToBeVisible(searchInput);
        searchInput.clear();
        searchInput.sendKeys(bookName);
        scrollToElement(searchButton);
        searchButton.click();
        // Wait for search results
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clearSearch() {
        scrollToElement(searchInput);
        waitForElementToBeVisible(searchInput);
        searchInput.clear();
        scrollToElement(searchButton);
        searchButton.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void verifyLandingOnHomepage(){
        scrollToElement(titleHomePage);
        waitForElementToBeVisible(titleHomePage);
    }


    public void navigateToHomepage(){
        scrollToElement(homepageEntryPoint);
        waitForElementToBeVisible(homepageEntryPoint);
        homepageEntryPoint.click();
    }

    // --- Helper methods for Assertions & Cart ---

    public int getDisplayedBooksCount() {
        return bookTitles.size();
    }

    public List<String> getDisplayedBookTitles() {
        List<String> titles = new ArrayList<>();
        for (WebElement title : bookTitles) {
            titles.add(title.getText());
        }
        return titles;
    }

    public List<Integer> getDisplayedBookPrices() {
        List<Integer> prices = new ArrayList<>();
        for (WebElement priceElement : bookPrices) {
            // Assuming price format is like "Rp 150.000" or "$15"
            // We need to clean the string to parse it as an integer for comparison
            String priceText = priceElement.getText().replaceAll("[^0-9]", ""); 
            if (!priceText.isEmpty()) {
                prices.add(Integer.parseInt(priceText));
            }
        }
        return prices;
    }

    public String getBookTitleByIndex(int index) {
        if (index >= 0 && index < bookTitles.size()) {
            WebElement titleElement = bookTitles.get(index);
            scrollToElement(titleElement);
            return titleElement.getText();
        }
        return null;
    }

    public void clickAddToCartByIndex(int index) {
        if (index >= 0 && index < addToCartButtons.size()) {
            WebElement btn = addToCartButtons.get(index);
            scrollToElement(btn); // Scroll ke elemen dulu
            waitForElementToBeVisible(btn);
            btn.click();
            try {
                Thread.sleep(1500); // Tunggu agak lama biar badge beneran ke-update
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCartBadgeCount() {
        try {
            scrollToElement(cartBadge);
            return cartBadge.getText().trim();
        } catch (Exception e) {
            return "0"; // If badge is not present, cart is likely empty
        }
    }

    public void navigateToCart() {
        scrollToElement(cartIcon); // Scroll ke atas / ke icon cart
        waitForElementToBeVisible(cartIcon);
        cartIcon.click();
    }
}


