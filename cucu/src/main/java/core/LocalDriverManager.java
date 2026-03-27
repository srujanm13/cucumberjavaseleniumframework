package core;

import config.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class LocalDriverManager {

    public WebDriver createDriver() {
        String browser = ConfigReader.get("browser");
        boolean headless = isHeadlessEnabled();
        WebDriver driver;

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            if (headless) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            driver = new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            if (headless) {
                options.addArguments("--headless=new");
            }
            driver = new EdgeDriver(options);
        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }

        applyDeviceConfiguration(driver, ConfigReader.get("device"));
        return driver;
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
