package tests;

import org.testng.annotations.Test;
import pages.HomePage;
import base.BaseTest;
import utils.ConsoleLogger;

public class VerifyHomePageSliderTest extends BaseTest {

	 @Test(priority = 1)
    public void verifySliderDotsAndDiscoverDealsText() throws Exception {
        HomePage homePage = new HomePage(driver);

        Thread.sleep(1000);
        homePage.scrollToSlider();
        Thread.sleep(1000);

        int dotCount = homePage.getSliderDotCount();
        ConsoleLogger.logInfo("🔵 Total slider dots found: " + dotCount);

        for (int i = 0; i < dotCount; i++) {
            // Click current slider dot
            homePage.clickSliderDot(i);
            Thread.sleep(1000); // Allow slider to transition
            
            // Click the slider image to go to detail page
            homePage.clickSliderImage();
            Thread.sleep(5000); // Wait for deal detail page to load

            // Check if "Discover the Deal" title is visible
            boolean isTitleVisible = homePage.isDiscoverDealTitleVisible();
            ConsoleLogger.logBullet("'Discover the Deal' title visible for Dot " + (i + 1), String.valueOf(isTitleVisible), isTitleVisible);

            // Print character count of deal description
            int charCount = homePage.getDealDescriptionCharCount();
            ConsoleLogger.logInfo("🟢 Deal description character count for Dot " + (i + 1) + ": " + charCount);

            Thread.sleep(2000); // Wait for homepage to load
            // Navigate back to homepage
            driver.navigate().back();
            Thread.sleep(3000); // Wait for homepage to load
            // Scroll back to slider before next iteration
            homePage = new HomePage(driver); // Refresh page object
            homePage.scrollToSlider();
            Thread.sleep(1000);
        }
    }
}