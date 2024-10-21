# OrangeHRM Automation Tests

## Overview
This project contains automated tests for the OrangeHRM demo site using Selenium WebDriver with Java and JUnit. The tests include scenarios for login (positive and negative), forgot password, and navigating through the Leave and Dashboard menus.

## Key Features
- **Positive Login Test**: Validates the ability to log in with correct credentials.
- **Negative Login Test**: Validates the behavior when incorrect credentials are used.
- **Forgot Password Test**: Tests the password reset functionality.
- **Menu Search and Navigation**: Automates searching and navigating through dashboard menus and performing actions like applying for leave.

## Prerequisites
Before running the tests, ensure you have the following installed:

- **Java Development Kit (JDK)**
- **Maven**: Install Maven to manage the project dependencies.
- **Chrome Browser**: Ensure that you have Google Chrome installed.
- **ChromeDriver**: The project uses WebDriverManager to automatically download the correct version of ChromeDriver.

## Setup
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-repository-url.git
   ```

2. **Navigate to the project directory:**
   ```bash
   cd your-repository-directory
   ```

3. **Install dependencies:** Run the following Maven command to download all the required dependencies.
   ```bash
   mvn clean install
   ```

4. **Run the tests:**
   ```bash
   mvn test
   ```

## Project Structure
The project is structured as follows:

```
├── pom.xml               # Maven project configuration
└── src
    └── test
        └── java
            └── com
                └── orange
                    └── ApplicationTest.java   # Main test file with different test scenarios
```

## Test Scenarios
The following are the test scenarios included in the project:

- **Positive Login Test**: Logs in with valid credentials and navigates to the dashboard.
- **Negative Login Test**: Attempts to log in with invalid credentials and verifies the error message.
- **Forgot Password Test**: Navigates to the password reset page and performs the reset.
- **Menu Search**: Searches for dashboard and leave menu items, both valid and invalid.
- **Leave Application**: Searches for the "Leave" menu and applies for leave using the form.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
