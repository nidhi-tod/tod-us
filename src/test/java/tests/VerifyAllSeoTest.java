package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utils.ConsoleLogger;
import java.util.List;

public class VerifyAllSeoTest extends BaseTest {

    @Test(priority = 1)
    public void robotsTxtChecker() {
        ConsoleLogger.logSection("SEO Test 1: robotsTxtChecker");

        try {
            String baseUrl = driver.getCurrentUrl();
            String robotsUrl = baseUrl + (baseUrl.endsWith("/") ? "robots.txt" : "/robots.txt");
            String sitemapUrl = baseUrl + (baseUrl.endsWith("/") ? "sitemap-index.xml" : "/sitemap-index.xml");

            driver.navigate().to(robotsUrl);
            String robotsSource = driver.getPageSource();

            if (robotsSource != null && !robotsSource.trim().isEmpty() && !robotsSource.contains("404")
                    && robotsSource.contains(sitemapUrl)) {
                ConsoleLogger.logSuccess("✅ robots.txt is present and contains the sitemap entry.");
                ConsoleLogger.logPlain("   • robots.txt URL:     " + robotsUrl);
                ConsoleLogger.logPlain("   • Expected sitemap:   " + sitemapUrl);
            } else if (robotsSource == null || robotsSource.trim().isEmpty() || robotsSource.contains("404")) {
                ConsoleLogger.logError("❌ robots.txt is missing or returned 404.");
                ConsoleLogger.logError("❌ robots.txt is missing or returned 404.");
                ConsoleLogger.logPlain("   • Checked URL:        " + robotsUrl);
            } else {
                ConsoleLogger.logError("❌ robots.txt found but missing sitemap entry.");
                ConsoleLogger.logPlain("   • Expected sitemap:   sdfasdf" + sitemapUrl);
            }

        } catch (Exception e) {
            ConsoleLogger.logError("❌ ERROR in robotsTxtChecker: " + e.getMessage());
        } finally {
            try {
                driver.navigate().back();
            } catch (Exception ignore) {
            }
        }
    }

    @Test(priority = 2)
    public void canonicalTagChecker() {
        ConsoleLogger.logSection("SEO Test 2: canonicalTagChecker");

        try {
            String expectedCanonical = driver.getCurrentUrl();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String rawHref = (String) js.executeScript(
                    "var link = document.querySelector('link[rel=\"canonical\"]');" +
                            "return link ? link.getAttribute('href') : null;"
            );

            ConsoleLogger.logPlain("• Expected URL:     " + expectedCanonical);
            ConsoleLogger.logPlain("• Canonical href:   " + rawHref);

            if (expectedCanonical.equals(rawHref)) {
                ConsoleLogger.logSuccess("✅ PASS: Canonical tag matches the page URL.");
            } else {
                ConsoleLogger.logError("❌ FAIL: Canonical tag does NOT match the page URL.");
            }

        } catch (Exception e) {
            ConsoleLogger.logError("❌ ERROR in canonicalTagChecker: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void metaTagsChecker() {
        ConsoleLogger.logSection("SEO Test 3: metaTagsChecker");

        try {
            // Title tag
            String pageTitle = driver.getTitle();
            boolean hasTitle = pageTitle != null && !pageTitle.trim().isEmpty();
            ConsoleLogger.logPlain("• <title>:          " + (hasTitle ? "✅ \"" + pageTitle + "\"" : "❌ MISSING or empty"));

            // Meta keywords
            List<WebElement> keywordsMeta = driver.findElements(By.cssSelector("meta[name='keywords']"));
            boolean hasKeywords = !keywordsMeta.isEmpty()
                    && keywordsMeta.get(0).getAttribute("content") != null
                    && !keywordsMeta.get(0).getAttribute("content").trim().isEmpty();
            ConsoleLogger.logPlain("• <meta keywords>:  " + (hasKeywords ? "✅ \"" + keywordsMeta.get(0).getAttribute("content") + "\"" : "❌ MISSING or empty"));

            // Meta description
            List<WebElement> descMeta = driver.findElements(By.cssSelector("meta[name='description']"));
            boolean hasDescription = !descMeta.isEmpty()
                    && descMeta.get(0).getAttribute("content") != null
                    && !descMeta.get(0).getAttribute("content").trim().isEmpty();
            ConsoleLogger.logPlain("• <meta description>: " + (hasDescription ? "✅ \"" + descMeta.get(0).getAttribute("content") + "\"" : "❌ MISSING or empty"));

            if (hasTitle && hasKeywords && hasDescription) {
                ConsoleLogger.logSuccess("✅ SUCCESS: All meta tags are present and valid.");
            } else {
                ConsoleLogger.logError("❌ FAIL: One or more meta tags are missing or empty.");
            }

        } catch (Exception e) {
            ConsoleLogger.logError("❌ ERROR in metaTagsChecker: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void robotsMetaTagChecker() {
        ConsoleLogger.logSection("SEO Test 4: robotsMetaTagChecker");

        try {
            List<WebElement> robotMetaTags = driver.findElements(By.xpath("//meta[@name='robots']"));

            if (robotMetaTags.isEmpty()) {
                ConsoleLogger.logSuccess("✅ SUCCESS: <meta name=\"robots\"> tag not found on Production (as expected).");
            } else {
                ConsoleLogger.logError("❌ FAIL: <meta name=\"robots\"> tag should NOT be present on Production.");
                ConsoleLogger.logPlain("• Found: " + robotMetaTags.get(0).getAttribute("outerHTML"));
            }
        } catch (Exception e) {
            ConsoleLogger.logError("❌ robotsMetaTagChecker failed: " + e.getMessage());
        }
    }
}
