package core;

import config.ConfigReader;
import org.openqa.selenium.WebDriver;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void initDriver() {
        String execution = ConfigReader.get("execution");

        WebDriver driverInstance = execution.equalsIgnoreCase("remote")
                ? new RemoteDriverManager().createDriver()
                : new LocalDriverManager().createDriver();

        setDriver(driverInstance);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}