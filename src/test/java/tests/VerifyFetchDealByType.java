package tests;

import base.BaseTest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.DealsListingPage;
import pages.HomePage;
import utils.ConsoleLogger;

import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;

public class VerifyFetchDealByType extends BaseTest {

    @Test(priority = 1)
    public void searchDeals() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Deals Count");

        HomePage homePage = new HomePage(driver);
        DealsListingPage dealsPage = new DealsListingPage(driver);

        ConsoleLogger.logSection("📊 STARTING DEAL TYPE FETCH & EXPORT");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Deal Type");
        header.createCell(1).setCellValue("Deal Count");
        header.createCell(2).setCellValue("Breadcrumbs");

        int rowIndex = 1;
        List<WebElement> allOptions = homePage.getAllDealTypeOptions(wait);
        int totalOptions = allOptions.size();
        ConsoleLogger.logInfo("🎯 Total Deal Types Found: " + totalOptions);

        for (int i = 0; i < totalOptions; i++) {
            List<WebElement> options = homePage.getAllDealTypeOptions(wait); // Re-fetch to avoid stale element issues

            if (i >= options.size()) {
                ConsoleLogger.logWarning("⚠️ Skipping index " + i + ", out of bounds.");
                continue;
            }

            WebElement option = options.get(i);
            String dealName = option.getText().trim();
            ConsoleLogger.logInfo("🔍 Selecting Deal Type: " + dealName);

            try {
                homePage.selectDealTypeByElement(option);
            } catch (Exception e) {
                ConsoleLogger.logError("❌ Click failed for " + dealName + ": " + e.getMessage());
                continue;
            }

            homePage.clickSearch();

            int dealCount = dealsPage.getDealCount();
            String breadcrumbs = dealsPage.getBreadcrumbText();

            ConsoleLogger.logPlain("📍 Breadcrumbs: " + breadcrumbs);
            ConsoleLogger.logBullet(dealName, dealCount + " deals found", dealCount > 0);

            // Write to Excel
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(dealName);
            row.createCell(1).setCellValue(dealCount);
            row.createCell(2).setCellValue(breadcrumbs);

            ConsoleLogger.logSuccess("📝 Excel updated for: " + dealName);

            driver.navigate().back();
            Thread.sleep(3000); // Let page load properly
        }

        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

        String excelPath = "src/test/resources/DealCount_" + System.currentTimeMillis() + ".xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(excelPath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        ConsoleLogger.logSuccess("✅ Excel file written successfully at:\n📁 " + excelPath);
        ConsoleLogger.logSection("✅ DEAL TYPE FETCH & EXPORT COMPLETED");
    }
}
