package stepdefinitions;

import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.json.simple.JSONObject;
import pages.LoginPage;
import utils.JSONUtils;
import org.testng.Assert;

public class LoginSteps {

    private static final String LOGIN_TEST_DATA_FILE = "login.json";
    LoginPage login = new LoginPage();
    private JSONObject currentLoginData;

    @Given("User logs in with valid credentials")
    public void loginUser() {
        login.login(ConfigReader.get("username"), ConfigReader.get("password"));

    }

    @Given("User logs in with invalid credentials from test data {string}")
    public void userLogsInWithInvalidCredentialsFromTestData(String testDataKey) {
        currentLoginData = JSONUtils.getJSONObject(LOGIN_TEST_DATA_FILE, testDataKey);
        login.login(
                JSONUtils.getString(currentLoginData, "username"),
                JSONUtils.getString(currentLoginData, "password")
        );

    }

    @Then("User should see the invalid login error from test data")
    public void userShouldSeeTheInvalidLoginErrorFromTestData() {
        String expectedError = JSONUtils.getString(currentLoginData, "errorMessage");

        Assert.assertTrue(
                login.getLoginErrorMessage().contains(expectedError),
                "Invalid login error message mismatch"
        );
    }

    @Then("verify login screen is displayed")
    public void verify_login_screen_is_displayed() {

        Assert.assertTrue(false );
    }
}
