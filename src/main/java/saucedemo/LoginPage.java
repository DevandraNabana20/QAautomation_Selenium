package saucedemo;

import core.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorAlert;

    @FindBy(className = "title")
    private WebElement pageTitle;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        logger.info("Attempting to login with username: {}", username);
        waitForElementToBeVisible(usernameInput);
        usernameInput.sendKeys(username);
        logger.debug("Entered username: {}", username);
        passwordInput.sendKeys(password);
        logger.debug("Entered password");
        loginButton.click();
        logger.info("Clicked login button");
    }

    public boolean isUserLoggedInSuccessfully() {
        try {
            waitForElementToBeVisible(pageTitle);
            boolean isDisplayed = pageTitle.isDisplayed() && pageTitle.getText().equals("Products");
            logger.info("Checked if user logged in successfully: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error checking login success: {}", e.getMessage());
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            waitForElementToBeVisible(errorAlert);
            boolean isDisplayed = errorAlert.isDisplayed();
            logger.info("Checked if error message is displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.warn("Error message not found or not visible");
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            waitForElementToBeVisible(errorAlert);
            String text = errorAlert.getText();
            logger.info("Retrieved error message: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to retrieve error message: {}", e.getMessage());
            return "";
        }
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL is: {}", url);
        return url;
    }
}
