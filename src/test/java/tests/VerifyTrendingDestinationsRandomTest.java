package tests;

import base.BaseTest;

import org.testng.annotations.Test;
import pages.TrendingDestinationsSection;

public class VerifyTrendingDestinationsRandomTest extends BaseTest {

	 @Test(priority = 1)
    public void clickTwoRandomTrendingDestinationsWithFaqCheck() throws InterruptedException {
        TrendingDestinationsSection trendingSection = new TrendingDestinationsSection(driver);
        trendingSection.clickTwoRandomDestinationsAndCheckFaq();
    }
}
