package tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ListingPage;
import utils.ConfigReader;
import utils.ConsoleLogger;

public class VerifyShowMore20DealsTest extends BaseTest {

	 @Test(priority = 1)
    public void testShow20ButtonFunctionality() throws InterruptedException {
        ListingPage listingPage = new ListingPage(driver);
        driver.get(ConfigReader.get("show20Url"));
        Thread.sleep(2000); // Wait for initial load
        Thread.sleep(2000);

        // Step 1: Capture initial total deals
        int expectedDealCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("🔢 Initial Total Deals (Expected): " + expectedDealCount);

        // Step 2: Click "SHOW NEXT 20" button until not visible
        int clickCount = 0;
        while (true) {
            listingPage.slowScrollDown();
            WebElement showMoreButton = listingPage.getShowNext20Button();
            if (showMoreButton != null && showMoreButton.isDisplayed() && showMoreButton.isEnabled()) {
                showMoreButton.click();
                clickCount++;
                ConsoleLogger.logWarning("🟠 Clicked 'SHOW NEXT 20' (" + clickCount + ")");
                Thread.sleep(2000); // wait for new deals to load
            } else {
                break;
            }
        }

        // Step 3: Verify number of visible "View deal" buttons
        Thread.sleep(2000); // Wait to ensure all deals are rendered
        List<WebElement> viewDealButtons = listingPage.getViewDealButtons();
        int actualDealCount = viewDealButtons.size();
        ConsoleLogger.logSuccess("✅ Final Loaded Deals (Actual): " + actualDealCount);
        ConsoleLogger.logInfo("🧮 'SHOW NEXT 20' clicked: " + clickCount + " times");

        // Step 4: Compare expected and actual deal counts
        if (actualDealCount == expectedDealCount) {
            ConsoleLogger.logSuccess("✅ Success: Both expected and actual deal counts match! (" + expectedDealCount + ")");
        } else {
            ConsoleLogger.logError("❌ Mismatch: Expected " + expectedDealCount + " deals, but found " + actualDealCount + " deals.");
        }

        // Step 5: Assert
        assert actualDealCount == expectedDealCount :
                "Mismatch! Expected: " + expectedDealCount + ", but got: " + actualDealCount;
    }
}
