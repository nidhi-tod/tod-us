package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.ListingPage;
import utils.ConfigReader;
import utils.ConsoleLogger;

public class VerifyFiltersAndSortingTest extends BaseTest {

    @Test(priority = 1)
    public void verifyRatingFilterCounts() throws InterruptedException {
        ConsoleLogger.logSection("Test 1: Verify Rating Filter Counts");

        driver.get(ConfigReader.get("baseUrl") + "/vacations/alldeal/");
        ListingPage listingPage = new ListingPage(driver);

        int initialCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("Initial Total Deals: " + initialCount);

        Thread.sleep(10000); // Wait for filters to load

        listingPage.selectFiveStar();
        waitShort();
        int fiveStarCount = listingPage.getCurrentlyShowingDealsCount();
        ConsoleLogger.logInfo("➤ 5★ Count: " + fiveStarCount);

        listingPage.selectFourStar(); // Unchecks 5-star
        waitShort();
        int fourStarCount = listingPage.getCurrentlyShowingDealsCount();
        ConsoleLogger.logInfo("➤ 4★ Count: " + fourStarCount);

        listingPage.selectThreeStar(); // Unchecks 4-star
        Thread.sleep(10000);
        int threeStarCount = listingPage.getCurrentlyShowingDealsCount();
        ConsoleLogger.logInfo("➤ 3★ Count: " + threeStarCount);

        int totalFilteredCount = fiveStarCount + fourStarCount + threeStarCount;
        ConsoleLogger.logInfo("Total of 5★ + 4★ + 3★ Counts: " + totalFilteredCount);
        ConsoleLogger.logInfo("Initial Total Deals: " + initialCount);

        ConsoleLogger.logBullet(
            "Filtered total matches initial total deals count",
            totalFilteredCount + " == " + initialCount,
            totalFilteredCount == initialCount
        );

        ConsoleLogger.logSection("End of Test 1");
    }

    @Test(priority = 2)
    public void testPriceFilter500to1000() throws InterruptedException {
        ConsoleLogger.logSection("Test 2: Price Filter ($500 - $1000)");

        driver.get(ConfigReader.get("baseUrl") + "/vacations/alldeal/");
        ListingPage listingPage = new ListingPage(driver);

        int initialCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("Initial Total Deals: " + initialCount);

        Thread.sleep(2000);

        listingPage.selectPriceRange500to1000();
        listingPage.waitForResultsToLoad();
        int filteredCount = listingPage.getCurrentlyShowingDealsCount();
        ConsoleLogger.logInfo("➤ Filtered Count ($500 - $1000): " + filteredCount);

        Thread.sleep(2000);

        listingPage.unselectPriceRange500to1000();
        listingPage.waitForResultsToLoad();

        Thread.sleep(2000);
        int resetCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("➤ Reset Count (after unselecting): " + resetCount);

        ConsoleLogger.logSection("End of Test 2");
    }

    @Test(priority = 3)
    public void testFilterByFiveToTenNights() throws InterruptedException {
        ConsoleLogger.logSection("Test 3: Filter by 5 to 10 Nights");

        driver.get(ConfigReader.get("baseUrl") + "/vacations/alldeal/");
        ListingPage listingPage = new ListingPage(driver);

        int initialCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("Initial Total Deals: " + initialCount);

        Thread.sleep(2000);

        listingPage.selectFiveToTenNights();
        listingPage.waitForResultsToLoad();
        int filteredCount = listingPage.getCurrentlyShowingDealsCount();
        ConsoleLogger.logInfo("➤ Filtered Count (5 - 10 Nights): " + filteredCount);

        Thread.sleep(2000);

        listingPage.unselectFiveToTenNights();
        listingPage.waitForResultsToLoad();

        Thread.sleep(2000);
        int resetCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("➤ Reset Count (after unselecting): " + resetCount);

        ConsoleLogger.logSection("End of Test 3");
    }

    @Test(priority = 4)
    public void verifySortingFilterCounts() throws InterruptedException {
        ConsoleLogger.logSection("Test 4: Verify Sorting Filters");

        driver.get(ConfigReader.get("baseUrl") + "/vacations/alldeal/");
        ListingPage listingPage = new ListingPage(driver);

        int initialCount = listingPage.getTotalDealsCount();
        ConsoleLogger.logInfo("Initial Total Deals: " + initialCount);

        Thread.sleep(2000);

        listingPage.applyAllSortingOptionsAndPrintFirstDealPrice();

        Thread.sleep(2000);

        ConsoleLogger.logSection("End of Test 4");
    }

    private void waitShort() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
