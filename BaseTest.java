// src/test/java/base/BaseTest.java
package com.test.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    // 'protected' means all child test classes can access this driver
    protected WebDriver driver;

    protected static final String BASE_URL = "http://localhost/ofbms";

    @BeforeMethod
    public void setUp() {
        // Auto-download and configure ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment to run without GUI

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Implicit wait: Selenium waits up to 5 seconds before throwing errors
        driver.manage().timeouts()
              .implicitlyWait(java.time.Duration.ofSeconds(5));
    }

    @AfterMethod
    public void tearDown() {
        // Always close browser after each test — even if test fails
        if (driver != null) {
            driver.quit();
        }
    }
}