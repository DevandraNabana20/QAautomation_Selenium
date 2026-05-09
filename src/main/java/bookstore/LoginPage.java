package bookstore;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    @FindBy(xpath = "//*[@id='email']")
    private WebElement inputEmail;

    @FindBy(xpath = "//*[@id='password']")
    private WebElement inputPassword;

    @FindBy(id = "submit")
    private WebElement signInButton;

    @FindBy(css = "#submit")
    private WebElement loginButton;

    @FindBy (xpath = "//*[@id='flash']")
    private WebElement popUpError;

    @FindBy (xpath = "//*[@id='welcome-message']")
    private WebElement welcomeMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }


    public void login(String username, String password) {
        scrollToElement(inputEmail);
        waitForElementToBeVisible(inputEmail);//tunggu hingga elemen inputEmail terlihat
        inputEmail.sendKeys(username); //masukan username ke dalam inputEmail
        inputPassword.sendKeys(password);//masukan password kedalam inputPassword
        signInButton.click();
    }

    public boolean verifyLoginSuccess(){
        scrollToElement(welcomeMessage);
        waitForElementToBeVisible(welcomeMessage);
        return welcomeMessage.isDisplayed();
    }

    public boolean verifyLoginFailed(){
        waitForElementToBeVisible(popUpError);
        return popUpError.isDisplayed();
    }
}
