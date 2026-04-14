package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class AdminPage {

    private WebDriver driver;

    public static final String ADMIN_URL       = "http://localhost/ofbms/admin/";
    public static final String ADMIN_LOGIN_URL = "http://localhost/ofbms/admin/login.php";
    public static final String ADMIN_USER      = "admin";
    public static final String ADMIN_PASS      = "admin123"; // ← update from LOGIN DETAILS.txt

    private By usernameField = By.name("user_id");
    private By passwordField = By.name("user_pass");
    private By loginButton   = By.name("login_but");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openAdminLogin() {
        driver.get(ADMIN_LOGIN_URL);
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void login(String username, String password) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public void loginAsAdmin() {
        openAdminLogin();
        login(ADMIN_USER, ADMIN_PASS);
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.urlContains("login=success"));
        waitForBodyText();
    }

    public void navigateTo(String page) {
        driver.get(ADMIN_URL + page);
        waitForBodyText();
    }

    /** Waits until page body has real visible text (not empty/redirecting) */
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

    /** Returns visible page text as lowercase — more reliable than getPageSource() */
    public String getPageText() {
        try {
            Object result = ((JavascriptExecutor) driver)
                .executeScript("return document.body.innerText");
            return result != null ? result.toString().toLowerCase() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getCurrentUrl() { return driver.getCurrentUrl(); }
    public String getPageSource() { return driver.getPageSource().toLowerCase(); }
}