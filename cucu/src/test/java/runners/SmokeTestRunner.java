package runners;

import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        tags = "@smoke",
        plugin = {"pretty", "summary", "listeners.ExtentCucumberPlugin"}
)
public class SmokeTestRunner extends BaseCucumberTestNGRunner {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
