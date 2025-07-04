package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.DealDetailPage;
import org.testng.Assert;
import utils.ConsoleLogger;

public class VerifyDealDetailTest extends BaseTest {

	 @Test(priority = 1)
    public void verifyDealDetail() throws InterruptedException {
        ConsoleLogger.logSection("🧪 DEAL DETAIL VERIFICATION STARTED");

        HomePage homePage = new HomePage(driver);
        DealDetailPage dealDetailPage = new DealDetailPage(driver);
        Thread.sleep(2000);

        // ✅ Get price from listing page
        String listingPrice = homePage.getFirstDealPrice().trim();
        ConsoleLogger.logWarning("📋 Listing Page Price   : " + listingPrice);

        // ✅ Click on first deal card
        homePage.clickFirstDealCard();
        ConsoleLogger.logSuccess("🖱️ Clicked on first deal card.");
        Thread.sleep(2000);

        // ✅ Verify 'Discover the Deal' title is displayed
        boolean isTitleDisplayed = dealDetailPage.isDiscoverTheDealTitleDisplayed();
        Assert.assertTrue(isTitleDisplayed, "❌ 'Discover the Deal' title is not displayed");
        ConsoleLogger.logSuccess("✅ 'Discover the Deal' title is displayed.");

        Thread.sleep(2000);

        // ✅ Get description character count
        int charCount = dealDetailPage.getDealDescriptionCharacterCount();
        ConsoleLogger.logPlain("📝 Deal Description Length: " + charCount + " characters");

        Thread.sleep(2000);

        // ✅ Get price from detail page
        String detailPrice = dealDetailPage.getDetailPagePrice().trim();
        ConsoleLogger.logWarning("💲 Detail Page Price    : " + detailPrice);

        // ✅ Compare listing and detail prices
        boolean priceMatch = listingPrice.equals(detailPrice);
        ConsoleLogger.logBullet("Prices Match", listingPrice + " vs " + detailPrice, priceMatch);
        Assert.assertEquals(detailPrice, listingPrice, "❌ Price mismatch between listing and detail page!");

        // ✅ Calendar Price Comparison
        ConsoleLogger.logSection("📆 CALENDAR PRICE COMPARISON");
        dealDetailPage.compareDetailAndCalendarPrice();

        // ✅ Airport-based Lowest Price
        ConsoleLogger.logSection("✈️ LOWEST PRICE BY AIRPORT SELECTION");
        dealDetailPage.selectRandomAirportAndPrintLowestPrice();

        // ✅ Accommodation Details
        ConsoleLogger.logSection("🏨 ACCOMMODATION DETAILS");
        dealDetailPage.verifyAccommodationDetails();

        ConsoleLogger.logSection("✅ DEAL DETAIL VERIFICATION COMPLETED");
    }
}
