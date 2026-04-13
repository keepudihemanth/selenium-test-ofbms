// src/test/java/pages/LoginPage.java
package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    // ── Locators ──────────────────────────────────────────────
    // These tell Selenium HOW to find each element on the page
    private By emailField    = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton   = By.xpath("//button[@type='submit']");
    private By errorMessage  = By.className("alert-danger");

    // ── Constructor ───────────────────────────────────────────
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── Actions ───────────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/login.php");
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

    // Combined helper — does the full login in one call
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}