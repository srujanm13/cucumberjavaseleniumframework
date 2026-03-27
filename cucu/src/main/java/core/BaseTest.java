package core;

import config.ConfigReader;

public class BaseTest {

    public void setup() {
        ConfigReader.load();
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(ConfigReader.getBaseUrl());
    }

    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
