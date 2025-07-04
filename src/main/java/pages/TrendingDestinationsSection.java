package pages;

import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class TrendingDestinationsSection {
    WebDriver driver;
    WebDriverWait wait;

    public TrendingDestinationsSection(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By sectionTitle = By.xpath("//*[contains(text(), 'Trending Destinations')]");
    private By destinationTitles = By.cssSelector(".embla__slide h5");
    private By nextArrow = By.cssSelector(".embla__button--next");
    private By destinationTitle = By.cssSelector("div.border > div > h2");
    private By destinationDescription = By.cssSelector("p.country_custom");

    public void scrollToSection() {
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(sectionTitle));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);
    }

    public void clickAllDestinationsAndCheckFaq() throws InterruptedException {
        scrollToSection();

        int clicked = 0;

        while (true) {
            // ✅ Re-fetch all elements inside the loop
            List<WebElement> destinations = driver.findElements(destinationTitles);

            if (clicked >= destinations.size()) break;

            for (int i = clicked; i < destinations.size(); i++) {
                String destinationName = destinations.get(i).getText().trim();

                if (!destinationName.isEmpty()) {
                    System.out.println("Clicking on destination: " + destinationName);
                    try {
                        destinations.get(i).click();
                        Thread.sleep(3000); // Wait for page load

                        scrollToFaq();
                        boolean isVisible = isFaqSectionVisible();
                        System.out.println("FAQ section visible for " + destinationName + ": " + isVisible);

                        driver.navigate().back();
                        wait.until(ExpectedConditions.visibilityOfElementLocated(sectionTitle));
                        scrollToSection();
                        Thread.sleep(2000);

                        clicked++; // move to next destination
                        break; // restart loop with fresh elements
                    } catch (Exception e) {
                        System.out.println("Error clicking destination " + destinationName + ": " + e.getMessage());
                        clicked++;
                        break;
                    }
                }
            }

            // ✅ Re-fetch the next button AFTER the loop
            WebElement nextButton;
            try {
                nextButton = driver.findElement(nextArrow);
            } catch (StaleElementReferenceException e) {
                nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(nextArrow));
            }

            // ✅ Check next arrow status
            String cursor = nextButton.getCssValue("cursor");
            if ("not-allowed".equals(cursor)) {
                break;
            } else {
                nextButton.click();
                Thread.sleep(1000);
            }
        }
    }


    private void scrollToFaq() {
        try {
            WebElement faqHeader = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Vacation Questions Answered')]")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faqHeader);
        } catch (Exception e) {
            System.out.println("FAQ header not found: " + e.getMessage());
        }
    }

    private boolean isFaqSectionVisible() {
        try {
            WebElement faqSection = driver.findElement(
                By.xpath("//*[contains(text(), 'Vacation Questions Answered')]/ancestor::div[contains(@class,'border')]")
            );
            return faqSection.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickTwoRandomDestinationsAndCheckFaq() throws InterruptedException {
        scrollToSection();
        Set<String> uniqueDestinations = new LinkedHashSet<>();

        // Step 1: Loop through carousel to collect all destination names
        while (true) {
            List<WebElement> destinations = driver.findElements(destinationTitles);
            for (WebElement destination : destinations) {
                String name = destination.getText().trim();
                if (!name.isEmpty()) {
                    uniqueDestinations.add(name);
                }
            }

            WebElement nextButton;
            try {
                nextButton = driver.findElement(nextArrow);
            } catch (StaleElementReferenceException e) {
                nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(nextArrow));
            }

            String cursor = nextButton.getCssValue("cursor");
            if ("not-allowed".equals(cursor)) {
                break;
            } else {
                nextButton.click();
                Thread.sleep(1000);
            }
        }

        // Step 2: Pick 2 random destinations
        List<String> allDestinations = new ArrayList<>(uniqueDestinations);
        Collections.shuffle(allDestinations);
        List<String> randomTwo = allDestinations.subList(0, Math.min(2, allDestinations.size()));

        System.out.println("\n=========== Selected 2 Random Trending Destinations ===========");
        for (String dest : randomTwo) {
            System.out.println("▶ " + dest);
        }
        System.out.println("===============================================================\n");

        // Step 3: Loop to find and click each random destination (scroll through carousel again)
        for (String destinationName : randomTwo) {
            boolean clicked = false;

            scrollToSection();

            // Restart carousel navigation for each destination
            while (true) {
                List<WebElement> destinations = driver.findElements(destinationTitles);
                for (WebElement destination : destinations) {
                    if (destination.getText().trim().equalsIgnoreCase(destinationName)) {
                        System.out.println("👉 Clicking on destination: " + destinationName);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", destination);
                        wait.until(ExpectedConditions.elementToBeClickable(destination)).click();

                        // Step 4: Validate title & description
                        printTitleAndDescriptionInfo();

                        // Step 5: Validate FAQ section
                        scrollToFaq();
                        boolean isVisible = isFaqSectionVisible();
                        System.out.println("✅ FAQ section visible for " + destinationName + ": " + isVisible + "\n");

                        driver.navigate().back();
                        wait.until(ExpectedConditions.visibilityOfElementLocated(sectionTitle));
                        Thread.sleep(2000);
                        clicked = true;
                        break;
                    }
                }

                if (clicked) break;

                WebElement nextButton;
                try {
                    nextButton = driver.findElement(nextArrow);
                } catch (StaleElementReferenceException e) {
                    nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(nextArrow));
                }

                String cursor = nextButton.getCssValue("cursor");
                if ("not-allowed".equals(cursor)) {
                    break;
                } else {
                    nextButton.click();
                    Thread.sleep(1000);
                }
            }

            if (!clicked) {
                System.out.println("❌ Could not find clickable element for: " + destinationName + "\n");
            }
        }
    }

    
    public void printTitleAndDescriptionInfo() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(destinationTitle));
            String titleText = titleElement.getText().trim();
            System.out.println("🔹 Title: " + titleText);

            WebElement descriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(destinationDescription));
            String descriptionText = descriptionElement.getText().trim();
            System.out.println("🔹 Description character count: " + descriptionText.length());

        } catch (Exception e) {
            System.out.println("❌ Error fetching title/description: " + e.getMessage());
        }
    }

    
}
