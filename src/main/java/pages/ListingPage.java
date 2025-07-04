package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;


public class ListingPage {
    WebDriver driver;
    WebDriverWait wait;

    // Updated XPath to be more robust using partial class and text
    By dealCountText = By.xpath("//p[contains(text(),'Found')]");
    By firstViewDealButton = By.xpath("(//button[contains(text(),'View deal')])[1]");
    // Locator for price on the listing page (second page)
    By listingPrice = By.xpath("(//p[contains(text(),'$') and span[contains(text(),'per person')]])[1]");
    // Locator for breadcrumbs
    By breadcrumbsLocator = By.cssSelector("span.text-gray-300.flex");
    private By totalDealsCount = By.xpath("//p[contains(text(),'Deals Found')]");
    By fiveToTenNightsCheckbox = By.id("facility5 - 10 Nights");
    
    private By checkbox_500_1000 = By.id("facility$500 - $1000");
    
    private By sortingDropdown = By.xpath("//div[contains(@class, 'css-b62m3t-container')][1]");
    private String sortingListbox = "//div[@role='listbox']//div[@role='option' and normalize-space(text())='%s']";
    private By firstDealPriceBold = By.xpath("(//p[contains(@class, 'text-[24px]') and contains(., 'per person')])");


    public ListingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
        //System.out.println("📍 Breadcrumbs: " + breadcrumbText);
        return breadcrumbText;
    }
    
    public int getTotalDealsCount() {
        WebElement dealCount = wait.until(ExpectedConditions.visibilityOfElementLocated(totalDealsCount));
        System.out.println("📋 Deal Count: " + dealCount.getText());
        String dealsText = dealCount.getText(); // e.g., "496 Deals Found"
        return Integer.parseInt(dealsText.split(" ")[0]);
    }
    public void slowScrollDown() {
    	
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 10; i++) {
            js.executeScript("window.scrollBy(0, 300)");
            try {
                Thread.sleep(300); // slight delay to simulate slow scroll
            } catch (InterruptedException ignored) {}
        }
    }  
    public List<WebElement> getViewDealButtons() {
        return driver.findElements(By.xpath("//button[contains(text(), 'View deal')]"));
    }
    public WebElement getShowNext20Button() {
        try {
            return driver.findElement(By.xpath("//button[contains(text(), 'SHOW NEXT 20')]"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
 // Scroll to any element
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // Safe click with JS fallback
    private void clickElementSafely(WebElement element) {
        try {
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void selectFiveStar() {
    	
        WebElement fiveStar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rating5")));
        scrollToElement(fiveStar);
        wait.until(ExpectedConditions.elementToBeClickable(fiveStar));
        if (!fiveStar.isSelected()) {
            clickElementSafely(fiveStar);
        }
    }

    public void selectFourStar() {
        WebElement fiveStar = driver.findElement(By.id("rating5"));
        if (fiveStar.isSelected()) {
            scrollToElement(fiveStar);
            clickElementSafely(fiveStar);
        }

        WebElement fourStar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rating4")));
        scrollToElement(fourStar);
        wait.until(ExpectedConditions.elementToBeClickable(fourStar));
        if (!fourStar.isSelected()) {
            clickElementSafely(fourStar);
        }
    }
    public void selectThreeStar() throws InterruptedException {
    	
    	WebElement fourStar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rating4")));
        if (fourStar.isSelected()) {
        	scrollToElement(fourStar);
        	Thread.sleep(5000);
            clickElementSafely(fourStar);
            
        }
        Thread.sleep(5000);
        WebElement threeStar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rating3")));
        Thread.sleep(5000);
        scrollToElement(threeStar);
        wait.until(ExpectedConditions.elementToBeClickable(threeStar));
        if (!threeStar.isSelected()) {
        	clickElementSafely(threeStar);
        }
    }
    
	/*
	 * public int getTotalDealsCount() { WebElement dealCount =
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(
	 * By.xpath("//p[contains(text(),'Deals Found')]")));
	 * System.out.println("Deal Count: " + dealCount.getText()); String dealsText =
	 * dealCount.getText(); // e.g., "653 Deals Found" return
	 * Integer.parseInt(dealsText.split(" ")[0]); }
	 */
    public int getCurrentlyShowingDealsCount() {
        WebElement showingDealsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Showing') and contains(text(),'out of')]")));
        String dealsText = showingDealsElement.getText(); // e.g., "Showing 112 out of 653 Deals"
        System.out.println("Currently Showing Deals Text: " + dealsText);
        return Integer.parseInt(dealsText.split(" ")[1]);
    }
    
    public void selectPriceRange500to1000() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkbox_500_1000));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void unselectPriceRange500to1000() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkbox_500_1000));
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }
    
    public void waitForResultsToLoad() {
        try {
            Thread.sleep(3000); // Replace with intelligent wait if needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void selectFiveToTenNights() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(fiveToTenNightsCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void unselectFiveToTenNights() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(fiveToTenNightsCheckbox));
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }
    
    public void applyAllSortingOptionsAndPrintFirstDealPrice() {
        List<String> sortingOptions = Arrays.asList(
                "Best Deals", // Default (already applied)
                "Highest Rated",
                "Price High to Low",
                "Price Low to High"
        );

        System.out.println("\n========================");
        System.out.println("🔃 Starting Sorting Verification");
        System.out.println("========================");

        for (int i = 0; i < sortingOptions.size(); i++) {
            String option = sortingOptions.get(i);
            System.out.println("\n----------------------------------------");
            System.out.println("🔽 Sorting by: " + option);
            System.out.println("----------------------------------------");

            try {
                if (i != 0) { // Skip dropdown for default
                    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortingDropdown));
                    scrollToElement(dropdown);
                    clickElementSafely(dropdown);

                    By optionLocator = By.xpath(String.format(sortingListbox, option));
                    WebElement listOption = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
                    clickElementSafely(listOption);

                    waitForResultsToLoad(); // Replace with spinner check if available
                    Thread.sleep(3000); // Additional wait for UI
                }

                String priceText = getFirstDealPriceText();
                if (priceText != null && !priceText.isEmpty()) {
                    System.out.println("✅ First deal price for '" + option + "': £" + priceText);
                } else {
                    System.out.println("⚠️  Price not found for sorting option: " + option);
                }

            } catch (Exception e) {
                System.out.println("❌ Failed for sorting option: " + option);
                e.printStackTrace();
            }
        }

        System.out.println("\n========================");
        System.out.println("✅ Sorting Verification Completed");
        System.out.println("========================\n");
    }

    public String getFirstDealPriceText() {
        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstDealPriceBold));
            String priceFullText = priceElement.getText(); // e.g., "£1599 per person"

            // Extract digits only
            String numericPrice = priceFullText.replaceAll("[^0-9]", "");
            return numericPrice;
        } catch (Exception e) {
            System.out.println("❌ Unexpected error while fetching first deal price.");
            e.printStackTrace();
        }
        return "";
    }

}