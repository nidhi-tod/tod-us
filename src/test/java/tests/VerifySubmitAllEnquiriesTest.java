package tests;

import base.BaseTest;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;
import pages.CalendarEnquiryPage;
import pages.ContactUsPage;
import pages.DealDetailPage;
import pages.DealsListingPage;
import pages.GroupBookingPage;
import pages.HomePage;
import utils.ConfigReader;
import utils.ConsoleLogger;
import utils.TestData;

public class VerifySubmitAllEnquiriesTest extends BaseTest {

    @Test(priority = 1)
    public void SubscribeEnquiry() throws InterruptedException {
        ConsoleLogger.logSection("Subscribe Enquiry Test");

        HomePage homePage = new HomePage(driver);

        // Scroll to the email section
        homePage.scrollToEmailSection();

        // Submit email address from test data
        homePage.submitEmail(TestData.EMAIL);

        // Verify thank you message
        boolean success = homePage.verifyThankYouMessage();
        ConsoleLogger.logBullet("Subscribe Enquiry submission", success ? "Success" : "Failed", success);

        ConsoleLogger.logSection("End of Subscribe Enquiry Test");
    }

    @Test(priority = 2)
    public void submitContactUsEnquiry() throws InterruptedException {
        ConsoleLogger.logSection("Contact Us Enquiry Test");

        driver.get(ConfigReader.get("baseUrl") + "/contactus");

        ContactUsPage contactUsPage = new ContactUsPage(driver);
        contactUsPage.enterFirstName(TestData.FIRST_NAME);
        contactUsPage.enterLastName(TestData.LAST_NAME);
        contactUsPage.enterEmail(TestData.EMAIL);
        contactUsPage.enterContactNo(TestData.PHONE);
        contactUsPage.enterBookingReference(TestData.BOOKING_REF);
        contactUsPage.selectQueryType(TestData.QUERY_TYPE);
        contactUsPage.enterMessage(TestData.MESSAGE);
        contactUsPage.submitForm();

        boolean success = contactUsPage.isSubmissionSuccess();
        ConsoleLogger.logBullet("Contact Us form submission", success ? "Success" : "Failed", success);

        ConsoleLogger.logSection("End of Contact Us Enquiry Test");
    }

    @Test(priority = 3)
    public void submitGroupBookingEnquiry() throws InterruptedException {
        ConsoleLogger.logSection("Group Booking Enquiry Test");

        driver.get(ConfigReader.get("baseUrl") + "/groupbooking");

        GroupBookingPage groupBookingPage = new GroupBookingPage(driver);
        groupBookingPage.selectSalutation("Mr");
        groupBookingPage.enterLeadContactName(TestData.FIRST_NAME + " " + TestData.LAST_NAME);
        groupBookingPage.enterEmail(TestData.EMAIL);
        groupBookingPage.enterContactNumber(TestData.PHONE);
        groupBookingPage.enterAdults(TestData.ADULTS);
        groupBookingPage.enterChildren(TestData.CHILDREN);
        groupBookingPage.enterCallTime(TestData.CALL_TIME);
        groupBookingPage.enterMessage(TestData.MESSAGE);
        groupBookingPage.clickSubmit();

        boolean success = groupBookingPage.isSubmissionSuccess();
        ConsoleLogger.logBullet("Group Booking form submission", success ? "Success" : "Failed", success);

        ConsoleLogger.logSection("End of Group Booking Enquiry Test");
    }

    @Test(priority = 4)
    public void searchDeal() throws Exception {
        ConsoleLogger.logSection("Search Deal Test");

        driver.get(ConfigReader.get("baseUrl"));
        try {
            HomePage homePage = new HomePage(driver);
            DealsListingPage listingPage = new DealsListingPage(driver);
            DealDetailPage detailPage = new DealDetailPage(driver);

            // Click search button
            homePage.clickSearch();

            // Print all deal count
            int dealCount = listingPage.getDealCount();
            ConsoleLogger.logInfo("📦 All deals => " + dealCount);

            // Click first "View deal"
            listingPage.clickFirstDeal();

            // Fill enquiry form
            detailPage.submitEmailEnquiryForm(
                TestData.FIRST_NAME,
                TestData.PHONE,
                TestData.EMAIL,
                TestData.MESSAGE
            );

            ConsoleLogger.logSuccess("✅ Deal enquiry form submitted successfully.");
        } catch (Exception e) {
            ConsoleLogger.logError("❌ Test failed due to: " + e.getMessage());
            throw e;
        }

        ConsoleLogger.logSection("End of Search Deal Test");
    }

    @Test(priority = 5)
    public void testEnquiryAndDealClick() throws InterruptedException {
        ConsoleLogger.logSection("Enquiry and Deal Click Test");

        driver.get(ConfigReader.get("baseUrl"));
        CalendarEnquiryPage enquiryPage = new CalendarEnquiryPage(driver);

        // Click search
        enquiryPage.clickSearchButton();

        DealsListingPage dealsPage = new DealsListingPage(driver);

        // Print deal count
        int dealCount = dealsPage.getDealCount();
        ConsoleLogger.logInfo("📦 Total deals found: " + dealCount);

        // Click first deal
        dealsPage.clickFirstDeal();
        Thread.sleep(10000);

        DealDetailPage detailPage = new DealDetailPage(driver);

        // Scroll before interaction
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100);");
        Thread.sleep(1000);

        // Extract and submit enquiry based on date
        String date = detailPage.extractDateFromText();
        ConsoleLogger.logInfo("📦 Deal Date: " + date);
        Thread.sleep(1000);
        detailPage.clickDateAndFillPopup(date, TestData.FIRST_NAME, TestData.PHONE, TestData.EMAIL);

        ConsoleLogger.logSuccess("✅ Enquiry and deal click test completed.");
        ConsoleLogger.logSection("End of Enquiry and Deal Click Test");
    }
}
