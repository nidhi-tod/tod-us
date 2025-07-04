package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class DealsListingPage {
    WebDriver driver;

    // Updated XPath to be more robust using partial class and text
    By dealCountText = By.xpath("//p[contains(text(),'Found')]");
    By firstViewDealButton = By.xpath("(//button[contains(text(),'View deal')])[1]");
    // Locator for price on the listing page (second page)
    By listingPrice = By.xpath("(//p[contains(text(),'$') and span[contains(text(),'per person')]])[1]");
    // Locator for breadcrumbs
    By breadcrumbsLocator = By.cssSelector("span.text-gray-300.flex");

    public DealsListingPage(WebDriver driver) {
        this.driver = driver;
    }
    /**
     * Returns the number of deals found on the listing page.
     * Extracts numeric value from text like "641 Deals Found"
     */
    public int getDealCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dealCountText));

        String countText = driver.findElement(dealCountText).getText();
        System.out.println("Deal count text: " + countText); // Debug print
        String numericPart = countText.replaceAll("[^0-9]", "");

        if (numericPart.isEmpty()) {
            throw new IllegalStateException("Could not extract number from deal count text: " + countText);
        }

        return Integer.parseInt(numericPart);
    }
    
    public void clickFirstDeal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(firstViewDealButton)).click();
        System.out.println("✅ Clicked on the first 'View deal' button.");
    }
    public String getListingPrice() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(listingPrice)).getText();
        String price = priceText.replaceAll("[^0-9]", ""); // extract numeric part
        System.out.println("💰 Listing page price: " + price);
        return price;
    }
    
    public String getBreadcrumbText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement breadcrumbContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(breadcrumbsLocator));
        
        List<WebElement> breadcrumbItems = breadcrumbContainer.findElements(By.tagName("p"));
        StringBuilder breadcrumb = new StringBuilder();

        for (int i = 0; i < breadcrumbItems.size(); i++) {
            breadcrumb.append(breadcrumbItems.get(i).getText().trim());
            if (i < breadcrumbItems.size() - 1) {
                breadcrumb.append(" > ");
            }
        }

        String breadcrumbText = breadcrumb.toString();
        System.out.println("📍 Breadcrumbs: " + breadcrumbText);
        return breadcrumbText;
    }
}