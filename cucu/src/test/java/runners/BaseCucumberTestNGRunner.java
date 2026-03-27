package runners;

import config.ExecutionContext;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseCucumberTestNGRunner extends AbstractTestNGCucumberTests {

    @BeforeMethod(alwaysRun = true)
    public void applySuiteParameters(ITestContext testContext) {
        setIfPresent("browser", testContext.getCurrentXmlTest().getParameter("browser"));
        setIfPresent("device", testContext.getCurrentXmlTest().getParameter("device"));
        setIfPresent("env", testContext.getCurrentXmlTest().getParameter("env"));
        setIfPresent("execution", testContext.getCurrentXmlTest().getParameter("execution"));
        setIfPresent("gridUrl", testContext.getCurrentXmlTest().getParameter("gridUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void clearSuiteParameters() {
        ExecutionContext.clear();
    }

    private void setIfPresent(String key, String value) {
        if (value != null && !value.isBlank()) {
            ExecutionContext.set(key, value);
        }
    }
}
