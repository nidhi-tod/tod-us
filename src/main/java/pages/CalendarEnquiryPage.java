package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class CalendarEnquiryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By monthDropdown = By.id("To_toggleDropdown");
    private By saveButton = By.xpath("//button[contains(text(), 'Save') and contains(@class, 'btn-main')]");
    private By searchButton = By.xpath("//div[contains(@class,'btn-main') and text()='SEARCH']");

    public CalendarEnquiryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Selects an option from a child dropdown by index (1-based for dropdownIndex, 0-based for optionIndex).
     */
    public void selectChildDropdownOption(int dropdownIndex, int optionIndex, By optionsLocator, String label) {
        try {
            // Dynamic XPath for dropdown (1-based index)
            By dropdownLocator = By.xpath("(//div[contains(@class, 'deal-drop') or contains(@class, 'destination-drop')])[" + dropdownIndex + "]");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
            System.out.println("✅ Opened " + label + " dropdown");

            wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));
            List<WebElement> options = driver.findElements(optionsLocator);

            if (options.size() > optionIndex) {
                WebElement option = options.get(optionIndex);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                System.out.println("✅ Selected option index " + optionIndex + " from " + label + " dropdown");
            } else {
                System.out.println("❌ Not enough options in " + label + " dropdown");
            }

        } catch (Exception e) {
            System.out.println("❌ Error in " + label + " dropdown: " + e.getMessage());
        }
    }

    /**
     * Selects a given month and year from the calendar dynamically.
     */
    public void selectMonthAndYear(String month, String year) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(monthDropdown));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
            System.out.println("✅ Clicked on month dropdown");

            By dynamicMonth = By.xpath("//h3[text()='" + year + "']/following-sibling::div//label[text()='" + month + "']");
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(dynamicMonth));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            System.out.println("✅ Selected '" + month + " " + year + "'");

            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            System.out.println("✅ Clicked on 'Save' button");

        } catch (TimeoutException te) {
            System.out.println("❌ Calendar selection timed out: " + te.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Calendar selection failed: " + e.getMessage());
        }
    }

    /**
     * Clicks the 'SEARCH' button.
     */
    public void clickSearchButton() {
        try {
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
            Thread.sleep(10000);  // Wait for results to load
            System.out.println("✅ Clicked on Search button");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Search button: " + e.getMessage());
        }
    }
}
