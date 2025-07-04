package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration; // Correct import for Duration in Selenium 4

public class ContactUsPage {

    WebDriver driver;

    // Locators for the form fields
    private By firstNameInput = By.id("first-name-input");
    private By lastNameInput = By.name("lastName");
    private By emailInput = By.name("email");
    private By contactNoInput = By.xpath("//input[@class='PhoneInputInput']");
    private By bookingReferenceInput = By.name("bookingReference");
    private By messageInput = By.name("message");
    private By submitButton = By.xpath("//button[@type='submit']");
    
    // Locator for success message
    private By successMessage = By.xpath("//p[contains(text(), 'Thank you')]");

    // Constructor to initialize WebDriver
    public ContactUsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods to interact with the elements on the page
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameInput).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void enterEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterContactNo(String contactNo) {
        driver.findElement(contactNoInput).sendKeys(contactNo);
    }

    public void enterBookingReference(String bookingReference) {
        driver.findElement(bookingReferenceInput).sendKeys(bookingReference);
    }

    public void selectQueryType(String queryType) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdownActivator = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[class*='css-b9n1eg-control']")
        ));
        dropdownActivator.click();
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[id*='react-select'][id$='-input']")
        ));
        input.sendKeys("Post-Departure");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, '-option') and text()='Post-Departure']")
        ));
        option.click();
    }

    public void enterMessage(String message) {
        driver.findElement(messageInput).sendKeys(message);
        System.out.println("enter message");
    }

    public void submitForm() {
        driver.findElement(submitButton).click();
        System.out.println("click submit");
    }

    public boolean isSubmissionSuccess() {
        try {
            System.out.println("🔍 Step 1: Waiting for success message element...");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement successMessageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(successMessage)
            );

            System.out.println("✅ Step 2: Success message element is visible.");
            return successMessageElement.isDisplayed();

        } catch (NoSuchElementException e) {
            System.out.println("❌ NoSuchElement: Could not find the success message element.");
            return false;

        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            return false;
        }
    }
}
