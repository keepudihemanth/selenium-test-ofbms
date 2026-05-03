package com.test.tests;

import com.test.base.BaseTest;
import com.test.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

/**
 * LOGIN TESTS — 8 tests
 *
 * Confirmed test credentials:
 *   Email    → seleniumtest@gmail.com
 *   Password → Test@1234
 *
 * Confirmed URLs after login:
 *   Home      → /index.php
 *   My Flights → /my_flights.php
 *   My Tickets → /ticket.php
 *   Feedback  → /feedback.php
 */
public class LoginTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 1: Valid login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Valid credentials should redirect away from login page")
    public void testValidUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsTestUser();

        String url = loginPage.getCurrentUrl();

        // After login, must NOT still be on login.php
        Assert.assertFalse(
            url.contains("login.php"),
            "Login failed — still on login page. URL: " + url
        );
        System.out.println("Valid login URL: " + url);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 2: Nav links visible after login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "After login, nav should show: my flights, my tickets, feedback, logout")
    public void testNavLinksAfterLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsTestUser();

        String text = loginPage.getPageText();

        // These nav items should appear on the homepage after login
        Assert.assertTrue(
            text.contains("my flights") || text.contains("flights"),
            "BUG: 'my flights' nav link missing after login"
        );
        Assert.assertTrue(
            text.contains("ticket") || text.contains("my tickets"),
            "BUG: 'my tickets' nav link missing after login"
        );
        Assert.assertTrue(
            text.contains("logout") || text.contains("log out"),
            "BUG: 'logout' link missing after login"
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 3: My Flights page accessible after login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "My Flights page loads at /my_flights.php after login")
    public void testMyFlightsPageLoads() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsTestUser();

        driver.get("http://localhost/ofbms/my_flights.php");
        loginPage.waitForBodyText();

        String text = loginPage.getPageText();
        String url  = loginPage.getCurrentUrl();
        System.out.println("MY FLIGHTS URL : " + url);
        System.out.println("MY FLIGHTS TEXT: " + text.substring(0, Math.min(300, text.length())));

        Assert.assertFalse(
            text.contains("not found"),
            "404 on my_flights.php. URL: " + url
        );
        Assert.assertTrue(
            text.contains("flight")   ||
            text.contains("booking")  ||
            text.contains("ticket")   ||
            text.contains("no flight"),
            "My Flights page content missing. URL: " + url
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 4: My Tickets page accessible after login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "My Tickets page loads at /ticket.php after login")
    public void testMyTicketsPageLoads() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsTestUser();

        driver.get("http://localhost/ofbms/ticket.php");
        loginPage.waitForBodyText();

        String text = loginPage.getPageText();
        String url  = loginPage.getCurrentUrl();
        System.out.println("MY TICKETS URL : " + url);
        System.out.println("MY TICKETS TEXT: " + text.substring(0, Math.min(300, text.length())));

        Assert.assertFalse(
            text.contains("not found"),
            "404 on ticket.php. URL: " + url
        );
        Assert.assertTrue(
            text.contains("ticket")   ||
            text.contains("flight")   ||
            text.contains("booking")  ||
            text.contains("no ticket"),
            "My Tickets page content missing. URL: " + url
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 5: Feedback page accessible after login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Feedback page loads at /feedback.php after login")
    public void testFeedbackPageLoads() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsTestUser();

        driver.get("http://localhost/ofbms/feedback.php");
        loginPage.waitForBodyText();

        String text = loginPage.getPageText();
        String url  = loginPage.getCurrentUrl();
        System.out.println("FEEDBACK URL : " + url);
        System.out.println("FEEDBACK TEXT: " + text.substring(0, Math.min(300, text.length())));

        Assert.assertFalse(
            text.contains("not found"),
            "404 on feedback.php. URL: " + url
        );
        Assert.assertTrue(
            text.contains("feedback") ||
            text.contains("comment")  ||
            text.contains("message")  ||
            text.contains("submit"),
            "Feedback page content missing. URL: " + url
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 6: Wrong password rejected
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Wrong password should stay on login page or show error")
    public void testInvalidPasswordRejected() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(LoginPage.VALID_EMAIL, "WRONGPASSWORD_XYZ");

        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.jsReturnsValue(
                "return document.readyState === 'complete'"));

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        String url  = loginPage.getCurrentUrl();
        String text = loginPage.getPageText();
        System.out.println("WRONG PASSWORD URL: " + url);

        // Must NOT leave login page on wrong password
        Assert.assertFalse(
            url.contains("index.php") && !url.contains("login"),
            "BUG: Wrong password was accepted! URL: " + url
        );
        // Should still show login page or error
        Assert.assertTrue(
            url.contains("login")       ||
            text.contains("invalid")    ||
            text.contains("incorrect")  ||
            text.contains("wrong")      ||
            text.contains("error")      ||
            text.contains("user_id"),   // login form still visible
            "Wrong password: unexpected state. URL: " + url
        );
        System.out.println("Wrong password correctly rejected.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 7: Empty form should not crash
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Submitting empty login form should not crash the app")
    public void testEmptyFormSubmission() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Click login without entering anything
        loginPage.clickLogin();

        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.jsReturnsValue(
                "return document.readyState === 'complete'"));

        String url = loginPage.getCurrentUrl();

        // App must not throw a 500 error or navigate away unexpectedly
        Assert.assertFalse(
            url.contains("500") || url.contains("error"),
            "BUG: App threw server error on empty form. URL: " + url
        );
        System.out.println("Empty form handled safely. URL: " + url);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 8: SQL injection should be blocked
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "SQL injection attempt should NOT bypass login")
    public void testSqlInjectionBlocked() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Classic SQL injection string
        loginPage.login("' OR '1'='1", "' OR '1'='1");

        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.jsReturnsValue(
                "return document.readyState === 'complete'"));

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        String url = loginPage.getCurrentUrl();
        System.out.println("SQL INJECTION URL: " + url);

        // Must NOT be logged in after injection attempt
        Assert.assertTrue(
            url.contains("login") || !url.contains("index.php"),
            "CRITICAL SECURITY BUG: SQL injection bypassed login! URL: " + url
        );
        System.out.println("SQL injection correctly blocked.");
    }
}
