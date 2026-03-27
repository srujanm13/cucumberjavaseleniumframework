package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import config.ConfigReader;

import java.io.IOException;

public class ExtentManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    public synchronized static ExtentReports getExtent() {
        if (extent == null) {
            String browser = valueOrDefault(ConfigReader.get("browser"), "default-browser");
            String device = valueOrDefault(ConfigReader.get("device"), "default-device");
            String os = valueOrDefault(System.getProperty("os.name"), "unknown-os");
            ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent-report.html");
            try {
                reporter.loadXMLConfig("src/main/resources/extent-config.xml");
            } catch (IOException e) {
                Log.warn("Could not load extent-config.xml. Using default Extent settings.");
            }
            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Browser", browser);
            extent.setSystemInfo("Device", device);
            extent.setSystemInfo("OS", os);
            extent.setSystemInfo("Execution", valueOrDefault(ConfigReader.get("execution"), "local"));
            extent.setSystemInfo("Environment", valueOrDefault(ConfigReader.get("env"), "default"));
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest extentTest = getExtent().createTest(testName);
        extentTest.assignDevice(valueOrDefault(ConfigReader.get("device"), "desktop"));
        extentTest.assignCategory(valueOrDefault(ConfigReader.get("browser"), "chrome"));
        extentTest.assignCategory(valueOrDefault(ConfigReader.get("env"), "default"));
        TEST.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void unload() {
        TEST.remove();
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
