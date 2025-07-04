package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration; // Correct import for Duration in Selenium 4


public class GroupBookingPage {

    WebDriver driver;

    // Locators
    private By salutationDropdown = By.name("salutation");
    private By leadContactNameInput = By.name("leadContactName");
    private By emailInput = By.name("email");
    private By contactNumberInput = By.xpath("//input[@class='PhoneInputInput']");
    private By adultsInput = By.name("adults");
    private By childrenInput = By.name("children");
    private By callTimeInput = By.name("callTime");
    private By messageTextarea = By.name("message");
    private By submitButton = By.xpath("//button[@type='submit']");

    // Constructor
    public GroupBookingPage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods to interact with the form
    public void selectSalutation(String salutation) {
        WebElement dropdown = driver.findElement(salutationDropdown);
        dropdown.sendKeys(salutation);
    }

    public void enterLeadContactName(String name) {
        driver.findElement(leadContactNameInput).sendKeys(name);
    }

    public void enterEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterContactNumber(String number) {
        driver.findElement(contactNumberInput).sendKeys(number);
    }

    public void enterAdults(String adults) {
        driver.findElement(adultsInput).sendKeys(adults);
    }

    public void enterChildren(String children) {
        driver.findElement(childrenInput).sendKeys(children);
    }

    public void enterCallTime(String callTime) {
        driver.findElement(callTimeInput).sendKeys(callTime);
    }

    public void enterMessage(String message) {
        driver.findElement(messageTextarea).sendKeys(message);
        System.out.println("enter message");
    }

    public void clickSubmit() {
        driver.findElement(submitButton).click();
    }
    public boolean isSubmissionSuccess() {
        try {
            // Adjust this XPath or locator based on your actual success message element
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Thank you') or contains(text(), 'successfully submitted')]")
            ));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
