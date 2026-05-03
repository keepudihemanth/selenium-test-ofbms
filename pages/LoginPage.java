package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * PAGE OBJECT: Passenger Login Page
 *
 * Confirmed from HTML inspection:
 *   name="user_id"    → Email field
 *   name="user_pass"  → Password field
 *   name="login_but"  → Submit button
 *
 * Confirmed URLs after login:
 *   Home page   → /index.php
 *   My Flights  → /my_flights.php
 *   My Tickets  → /ticket.php
 *   Feedback    → /feedback.php
 *
 * Test credentials:
 *   Email    → seleniumtest@gmail.com
 *   Password → Test@1234
 */
public class LoginPage {

    private WebDriver driver;

    // ── Confirmed test credentials ────────────────────────────────────────────
    public static final String LOGIN_URL   = "http://localhost/ofbms/login.php";
    public static final String VALID_EMAIL = "seleniumtest@gmail.com";
    public static final String VALID_PASS  = "Test@1234";

    // ── Confirmed locators from HTML inspection ───────────────────────────────
    private By emailField    = By.name("user_id");
    private By passwordField = By.name("user_pass");
    private By loginButton   = By.name("login_but");
    private By errorMessage  = By.className("alert-danger");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void open() {
        driver.get(LOGIN_URL);
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(emailField));
    }

    public void enterEmail(String email) {
        WebElement field = driver.findElement(emailField);
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = driver.findElement(passwordField);
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /** Combined: fill both fields and click login */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    /** Login with confirmed valid test credentials */
    public void loginAsTestUser() {
        open();
        login(VALID_EMAIL, VALID_PASS);
        // Wait until browser leaves login page
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.not(
                ExpectedConditions.urlContains("login.php")));
        waitForBodyText();
    }

    /** Waits until page body has real visible text */
    public void waitForBodyText() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(driver -> {
                try {
                    Object result = ((JavascriptExecutor) driver)
                        .executeScript("return document.body ? document.body.innerText.trim() : ''");
                    String text = result != null ? result.toString() : "";
                    return text.length() > 50;
                } catch (Exception e) {
                    return false;
                }
            });
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public String getCurrentUrl()  { return driver.getCurrentUrl(); }

    public String getPageText() {
        try {
            Object result = ((JavascriptExecutor) driver)
                .executeScript("return document.body.innerText");
            return result != null ? result.toString().toLowerCase() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getPageSource() {
        return driver.getPageSource().toLowerCase();
    }
}