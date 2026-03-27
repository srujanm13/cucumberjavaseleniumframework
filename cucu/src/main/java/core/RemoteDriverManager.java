package core;

import config.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class RemoteDriverManager {

    public WebDriver createDriver() {
        try {
            WebDriver driver = new RemoteWebDriver(
                    new URL(ConfigReader.get("gridUrl")),
                    buildOptions(ConfigReader.get("browser"))
            );
            applyDeviceConfiguration(driver, ConfigReader.get("device"));
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Selenium Grid", e);
        }
    }

    private AbstractDriverOptions<?> buildOptions(String browser) {
        boolean headless = isHeadlessEnabled();

        if (browser == null || browser.isBlank() || browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            if (headless) {
                options.addArguments("--headless=new");
            }
            return options;
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            return options;
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            if (headless) {
                options.addArguments("--headless=new");
            }
            return options;
        }

        throw new RuntimeException("Unsupported browser: " + browser);
    }

    private void applyDeviceConfiguration(WebDriver driver, String device) {
        String normalizedDevice = device == null || device.isBlank() ? "desktop" : device.toLowerCase();

        switch (normalizedDevice) {
            case "mobile":
                driver.manage().window().setSize(new Dimension(390, 844));
                break;
            case "tab":
            case "tablet":
                driver.manage().window().setSize(new Dimension(820, 1180));
                break;
            case "desktop":
                driver.manage().window().maximize();
                break;
            default:
                throw new RuntimeException("Unsupported device: " + device);
        }
    }

    private boolean isHeadlessEnabled() {
        String headless = ConfigReader.get("headless");
        return headless != null && Boolean.parseBoolean(headless.trim());
    }
}
