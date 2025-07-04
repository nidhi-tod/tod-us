package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.DealDetailPage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.ConsoleLogger;

public class verifyImageSliderTest extends BaseTest {

	 @Test(priority = 1)
    public void verifyImageSliderOnDealDetailPage() throws InterruptedException {
        ConsoleLogger.logSection("🖼️ IMAGE SLIDER VERIFICATION STARTED");

        HomePage homePage = new HomePage(driver);
        DealDetailPage dealDetailPage = new DealDetailPage(driver);
        Thread.sleep(2000);

        // Step 1: Navigate to Deal Detail Page
        homePage.clickFirstDealCard();
        ConsoleLogger.logInfo("🖱️ Navigated to Deal Detail Page.");
        Thread.sleep(3000);

        // Step 2: Click the first image in the gallery
        dealDetailPage.clickFirstGalleryImage();
        ConsoleLogger.logInfo("📸 Clicked the first image in the slider.");

        // Step 3: Wait for the Fancybox modal to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fancybox__container")));

        // Step 4: Verify modal is displayed
        Assert.assertTrue(dealDetailPage.isFancyboxModalVisible(), "❌ Fancybox modal did not appear.");
        ConsoleLogger.logSuccess("✅ Fancybox modal is displayed successfully.");

        ConsoleLogger.logSection("✅ IMAGE SLIDER VERIFICATION COMPLETED");
    }
}
