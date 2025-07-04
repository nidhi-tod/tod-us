package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.DealDetailPage;
import pages.ListingPage;
import utils.ConsoleLogger;

public class VerifyMainSliderTest extends BaseTest {

	 @Test(priority = 1)
    public void verifySliderDeals() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        DealDetailPage detailPage = new DealDetailPage(driver);

        ConsoleLogger.logSuccess("✅ Clicked on 'Accept' button.");
        ConsoleLogger.logSection("===========================================");
        homePage.printSliderTitle();

        // -------- 4th deal --------
        homePage.clickNextArrow(); // Make 4th card visible
        Thread.sleep(2000);

        String homePrice4 = homePage.getHomePagePriceByIndex(3);
        homePage.clickDealByIndex(3);
        Thread.sleep(3000);

        String detailPrice4 = detailPage.getDetailPrice();

        String cleanHomePrice4 = homePrice4.replaceAll("[^0-9]", "");
        String cleanDetailPrice4 = detailPrice4.replaceAll("[^0-9]", "");

        ConsoleLogger.logInfo("🔹 4th Deal");
        ConsoleLogger.logInfo("💰 Home Price: " + cleanHomePrice4);
        ConsoleLogger.logInfo("💰 Detail Price: " + cleanDetailPrice4);
        ConsoleLogger.logSection("===========================================\n");

        driver.navigate().back();
        Thread.sleep(2000);

        // -------- 8th deal --------
        homePage.clickNextArrow(); // To make 5th–7th visible
        Thread.sleep(2000);
        homePage.clickNextArrow(); // To make 8th card visible
        Thread.sleep(2000);
        homePage.clickNextArrow(); // Extra to ensure visibility
        Thread.sleep(2000);
        homePage.clickNextArrow(); // Extra to ensure visibility
        Thread.sleep(2000);
        homePage.clickNextArrow(); // Extra to ensure visibility
        Thread.sleep(2000);

        String homePrice8 = homePage.getHomePagePriceByIndex(7);
        homePage.clickDealByIndex(7);
        Thread.sleep(3000);

        String detailPrice8 = detailPage.getDetailPrice();

        String cleanHomePrice8 = homePrice8.replaceAll("[^0-9]", "");
        String cleanDetailPrice8 = detailPrice8.replaceAll("[^0-9]", "");

        ConsoleLogger.logInfo("🔹 8th Deal");
        ConsoleLogger.logInfo("💰 Home Price: " + cleanHomePrice8);
        ConsoleLogger.logInfo("💰 Detail Price: " + cleanDetailPrice8);
        ConsoleLogger.logSection("===========================================\n");

        driver.navigate().back();
        Thread.sleep(2000);

        // -------- View All + Deal Count --------
        homePage.clickViewAllButton();
        Thread.sleep(3000); // wait for navigation

        ListingPage listingPage = new ListingPage(driver);
        int dealCount = listingPage.getDealCount();

        ConsoleLogger.logInfo("📍 View All button clicked ");
        ConsoleLogger.logWarning("🔥 Total deals on listing page: " + dealCount);
        ConsoleLogger.logSection("===========================================");
    }
}
