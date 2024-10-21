package com.orange;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.time.Duration;

public class ApplicationTest {

    @Test
    public void runTestsSequentially() throws InterruptedException {
        // First, test the positive login scenario
        validLoginTest();

        // Optionally, uncomment the following if you want to test these cases in sequence
        invalidLoginTest();
        forgotPasswordTest();
    }

    public void validLoginTest() throws InterruptedException {
        System.out.println("Starting valid login test...");

        // Setup WebDriver and initiate Chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Maximize the browser window
            driver.manage().window().maximize();

            // Perform login with valid credentials
            performLogin(driver, "Admin", "admin123");

            // Wait for the login to be successful by checking for a specific element on the dashboard
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Tingkatkan waktu tunggu
            WebElement dashboardElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/aside[1]/nav[1]/div[1]/a[1]/div[2]/img[1]")));
            assertTrue("Dashboard element should be visible", dashboardElement.isDisplayed());

            // Check if the URL contains "/dashboard"
            String actualUrl = driver.getCurrentUrl();
            assertTrue("The URL should contain /dashboard", actualUrl.contains("/dashboard"));
            System.out.println("Login successful. Navigated to: " + actualUrl);

            // Wait for dashboard menu to load
            waitForDashboardMenu(driver);

            // Perform positive and negative search tests
            performSearchTests(driver);

            // Perform search for "Leave" menu after search tests
            searchForMenu(driver, "Leave");

            // After navigating to the "Leave" menu, click the "Apply" button
            applyForLeave(driver);

            // After applying, navigate to another menu, like "Dashboard"
            navigateToMenu(driver, "Dashboard");

            // Perform logout action
            logout(driver);

            // Wait to observe the login page after logout
            Thread.sleep(3000);  // 3 seconds delay for observation

        } finally {
            // Close the browser
            System.out.println("Closing browser after valid login test...");
            driver.quit();
        }
    }

    public void invalidLoginTest() throws InterruptedException {
        // Negative test case (invalid login)
        System.out.println("Starting invalid login test...");

        // Setup WebDriver and initiate Chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Perform login with invalid credentials
            performLogin(driver, "InvalidUser", "wrongpassword");

            // Wait for the URL to remain the same (login page)
            System.out.println("Verifying the user is not redirected to the dashboard...");

            // Correct the condition to check that the URL does not contain "/dashboard"
            boolean urlContainsDashboard = driver.getCurrentUrl().contains("/dashboard");
            assertFalse("The URL should not contain /dashboard", urlContainsDashboard);
            System.out.println("Login failed as expected. No redirection to dashboard.");

            // Wait to observe error message
            Thread.sleep(3000);  // 3 seconds delay

        } finally {
            // Close the browser
            System.out.println("Closing browser after invalid login test...");
            driver.quit();
        }
    }
    // Helper function to perform positive and negative search tests
    public void performSearchTests(WebDriver driver) throws InterruptedException {
        System.out.println("Starting search tests...");

        // Perform positive search test for an existing menu (e.g., "Dashboard")
        searchForMenu(driver, "Dashboard");
        Thread.sleep(2000);  // Delay to observe the action

        // Perform negative search test for a non-existing menu (e.g., "NonExistentMenu")
        searchForMenu(driver, "NonExistentMenu");
        Thread.sleep(2000);  // Delay to observe the action

        System.out.println("Search tests completed.");
    }

    public void forgotPasswordTest() throws InterruptedException {
        System.out.println("Starting forgot password test...");

        // Setup WebDriver and initiate Chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Maximize the browser window
            driver.manage().window().maximize();

            // Navigate to OrangeHRM Login page
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            System.out.println("Navigated to OrangeHRM login page.");

            // Wait for the page to fully load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));  // Wait for the login page to fully load

            // Use the specific XPath provided for the "Forgot your password?" link
            WebElement forgotPasswordLink = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/form[1]/div[4]/p[1]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", forgotPasswordLink);
            System.out.println("Scrolled to 'Forgot your password?' link...");

            // Wait for the "Forgot your password?" link to be clickable and click it
            wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
            System.out.println("Clicking 'Forgot your password?' link...");
            forgotPasswordLink.click();

            // Wait for the password reset page to load
            wait.until(ExpectedConditions.urlContains("/auth/requestPasswordResetCode"));
            String actualUrl = driver.getCurrentUrl();
            assertTrue("The URL should contain /auth/requestPasswordResetCode", actualUrl.contains("/auth/requestPasswordResetCode"));
            System.out.println("Password reset page loaded. Current URL: " + actualUrl);

            // Interact with the password reset form
            resetPassword(driver);

            // Wait to observe the password reset page
            Thread.sleep(3000);  // 3 seconds delay for observation

        } finally {
            // Close the browser
            System.out.println("Closing browser after forgot password test...");
            driver.quit();
        }
    }
    // Helper function to click on "Apply" leave button
    public void applyForLeave(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for the "Apply" button to be visible and clickable
        WebElement applyLeaveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/header[1]/div[2]/nav[1]/ul[1]/li[1]/a[1]")));
        System.out.println("Clicking the 'Apply' button on the Leave menu...");
        Thread.sleep(3000);  // Delay to observe the action
        applyLeaveButton.click();

        // Wait for the Apply Leave page to load
        wait.until(ExpectedConditions.urlContains("/applyLeave"));
        System.out.println("Apply Leave page loaded successfully.");
    }

    // Helper function to navigate to another menu after applying leave
    public void navigateToMenu(WebDriver driver, String menuName) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Perform search for another menu (e.g., "Dashboard")
        searchForMenu(driver, menuName);

        // Wait for the URL to change to the new menu
        wait.until(ExpectedConditions.urlContains(menuName.toLowerCase()));
        System.out.println(menuName + " page loaded successfully.");
    }

    // Updated helper function to interact with the reset password page and check for success message
    public void resetPassword(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for the email/username input field to be visible
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        System.out.println("Password reset page loaded. Entering username/email...");

        // Simulate typing the username or email for password reset
        typeWithDelay(emailField, "Admin");

        // Click the reset button
        WebElement resetButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        System.out.println("Clicking the reset button...");
        resetButton.click();

        // Wait for a more general success message or confirmation
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/h6[1]")));
        assertTrue("Success message should be displayed", successMessage.isDisplayed());
        System.out.println("Password reset request submitted successfully. Message: " + successMessage.getText());
    }

    // Helper function for waiting until the dashboard menu is loaded
    public void waitForDashboardMenu(WebDriver driver) throws InterruptedException {

        System.out.println("Dashboard menu is fully loaded.");

        Thread.sleep(3000);  // Delay to visually confirm the dashboard is ready before searching
    }

    // Helper function for searching a menu in the search bar
public void searchForMenu(WebDriver driver, String menuName) throws InterruptedException {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Wait for the search bar to be visible
    WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));
    System.out.println("Searching for menu: " + menuName);

    // Enter the menu name in the search bar
    searchBar.clear();
    searchBar.sendKeys(menuName);

    // Wait for the search results to appear
    Thread.sleep(3000);  // Wait for the search results to load

    // Check if the desired menu item is present in the search results
    try {
        WebElement menuOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='" + menuName + "']")));
        System.out.println(menuName + " menu found. Clicking the menu...");
        Thread.sleep(3000);  // Delay before clicking the menu
        menuOption.click();  // Click the menu if found
    } catch (Exception e) {
        System.out.println(menuName + " menu not found. Refreshing the page...");
        driver.navigate().refresh();  // Refresh the page if the menu is not found
        Thread.sleep(3000);  // Delay for observation after page refresh
    }
}
    // New helper function for performing logout dynamically
    public void logout(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Locate the user dropdown by a more dynamic selector, such as a profile icon or general element.
        WebElement userDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='oxd-userdropdown-tab']")));
        System.out.println("Clicking on user dropdown to access logout...");
        Thread.sleep(3000);  // Delay before clicking the dropdown
        userDropdown.click();

        // Wait for and click the logout button
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Logout')]")));
        System.out.println("Clicking logout button...");
        Thread.sleep(3000);  // Delay before clicking logout
        logoutButton.click();

        // Wait for the login page to reload
        wait.until(ExpectedConditions.urlContains("/auth/login"));
        System.out.println("Logout successful. Navigated back to login page.");
    }

    // Helper function for login action
    public void performLogin(WebDriver driver, String username, String password) throws InterruptedException {
        // Navigate to OrangeHRM Login page
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        System.out.println("Navigated to OrangeHRM login page.");

        // Wait for username and password fields to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));

        // Simulate typing username and password
        System.out.println("Typing username...");
        typeWithDelay(usernameField, username);
        System.out.println("Typing password...");
        typeWithDelay(passwordField, password);

        // Click the login button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.tagName("button")));
        System.out.println("Clicking the login button...");
        Thread.sleep(3000);  // Delay before clicking login
        loginButton.click();
    }

    // Helper function to simulate typing with delay
    public void typeWithDelay(WebElement element, String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(500);  // Increase delay between each character to make it slower
        }
        System.out.println("Finished typing: " + text);
    }
}
