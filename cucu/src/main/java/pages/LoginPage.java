package pages;

import org.openqa.selenium.By;
import utils.SeleniumUtils;

public class LoginPage {

    private By user = By.id("username");
    private By pass = By.id("password");
    private By loginBtn = By.id("signInBtn");
    private By terms = By.id("terms");
    private By loginErrorMessage = By.cssSelector(".alert-danger");

    public void login(String username, String password) {

        SeleniumUtils.clearAndFillText(user, username);
        SeleniumUtils.clearAndFillText(pass, password);
        if (!SeleniumUtils.waitForElementPresent(terms).isSelected()) {
            SeleniumUtils.click(terms);
        }
        SeleniumUtils.click(loginBtn);

    }

    public String getLoginErrorMessage() {
        return SeleniumUtils.getText(loginErrorMessage);
    }

    public boolean getLoginSuccess() {
        return SeleniumUtils.verifyElementPresent(loginBtn);
    }
}
