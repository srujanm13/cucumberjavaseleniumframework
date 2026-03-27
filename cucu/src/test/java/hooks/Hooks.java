package hooks;

import com.aventstack.extentreports.MediaEntityBuilder;
import config.ConfigReader;
import core.BaseTest;
import core.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ExtentManager;
import utils.Log;

public class Hooks extends BaseTest {

    @Before
    public void beforeScenario(Scenario scenario) {
        ConfigReader.load();
        ExtentManager.createTest(scenario.getName());
        Log.startTestLog(scenario.getName());
        setup();
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BASE64);
            ExtentManager.getTest().fail(
                    scenario.getName(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()
            );
        } else {
            ExtentManager.getTest().pass("Scenario passed");
        }

        ExtentManager.getExtent().flush();
        ExtentManager.unload();
        Log.endTestLog(scenario.getName());
        Log.unload();  // Clear thread logger
        tearDown();
    }
}
