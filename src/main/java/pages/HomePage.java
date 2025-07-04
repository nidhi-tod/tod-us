
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.Duration;

public class HomePage {
	WebDriver driver;
	JavascriptExecutor js;

	By dealTypeDropdown = By.id("dealTypeDropdownId");
	By destinationInput = By.id("destinationInputId");
	By monthDropdown = By.id("monthDropdownId");
	By searchBtn = By.xpath("//div[contains(text(),'SEARCH') and contains(@class,'btn-main')]");

	By dealDropdown = By.xpath("//div[contains(@class, 'deal-drop')]");
	By dealTypeOptions = By.xpath("//ul[contains(@class,'deals_dropdown')]//li/p");

	By handpickedDealsTitle = By.xpath("//h1[contains(text(), 'Handpicked Deals of the Week')]");
	// By nextArrow = By.xpath("//svg[contains(@class, 'lucide-move-right') and
	// not(contains(@class, 'cursor-not-allowed'))]");
	By nextArrow = By.cssSelector("svg.lucide.lucide-move-right.cursor-pointer");
	By allViewDealButtons = By.cssSelector("span.btn-reposive");

	// Locator for deal prices on the home page
	By dealPrices = By.xpath("//p[contains(@class, 'font-bold') and contains(text(), '$')]");
	
	// Slider Section
    By sliderSection = By.cssSelector(".offer-slider");
    By sliderDots = By.cssSelector(".embla__dots button");
    By discoverDealTexts = By.xpath("//p[contains(text(),'Discover the Deal')]");
    
    // Locator for slider images (the clickable image/area)
    By sliderImage = By.cssSelector(".offer-inner-continer img");
    //By sliderImage = By.cssSelector(".slider-image-selector"); // replace with your actual slider image selector

    // Locator for "Discover the Deal" title on detail page
    By discoverDealTitle = By.xpath("//p[contains(text(),'Discover the Deal')]");

    // Locator for deal description paragraph
    By dealDescription = By.xpath("//p[contains(text(), 'Discover the Deal')]/following-sibling::div/div[1]"); // Adjust if needed
    By firstDealCard = By.cssSelector("div.flex.flex-col.bg-white.border.cursor-pointer");
    
    By firstDealPrice = By.xpath("(//div[contains(@class,'flex-col') and contains(@class,'relative')])[1]//p[contains(@class,'font-bold')]");
    
    
    private By emailSection = By.xpath("//h3[contains(text(),'Get the Best Deals and Travel Ideas')]");
    private By emailInput = By.cssSelector("input[name='email']");
    private By submitButton = By.cssSelector("button[type='submit']");
    private By thankYouMessage = By.xpath("//*[contains(text(),'Thank you')]"); // Update if actual text differs

   
    public void scrollToEmailSection() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(emailSection));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", section);
    }

    public void submitEmail(String email) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean verifyThankYouMessage() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(thankYouMessage)).isDisplayed();
    }


	public List<WebElement> getAllDealTypeOptions(WebDriverWait wait) {
		try {
			((JavascriptExecutor) driver).executeScript("document.activeElement.blur();");
			new org.openqa.selenium.interactions.Actions(driver).moveByOffset(0, 0).click().perform();
			Thread.sleep(500);

			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dealDropdown));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);

			wait.until(ExpectedConditions.visibilityOfElementLocated(dealTypeOptions));
			return driver.findElements(dealTypeOptions);

		} catch (Exception e) {
			System.out.println("❌ Failed to get deal type options: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public void selectDealTypeByElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public HomePage(WebDriver driver) {
		this.driver = driver;
		if (driver instanceof JavascriptExecutor) {
            this.js = (JavascriptExecutor) driver;
        }
	}

	public void selectDealType(String dealType) {
		new Select(driver.findElement(dealTypeDropdown)).selectByVisibleText(dealType);
	}

	public void enterDestination(String destination) {
		driver.findElement(destinationInput).clear();
		driver.findElement(destinationInput).sendKeys(destination);
	}

	public void selectMonth(String month) {
		new Select(driver.findElement(monthDropdown)).selectByVisibleText(month);
	}

	public void clickSearch() {
		driver.findElement(searchBtn).click();
	}

	// XPath for country card (can be used for all countries)
	public By countryCard(String countryName) {
		return By.xpath("//a[@href='/country/" + countryName.toLowerCase() + "/']");
	}

	// XPath for FAQ section (used for all country pages)
	public By faqSection = By.xpath("//section[contains(@class, 'faq')] | //h2[contains(text(),'FAQ')]");

	// Method to click on any country card based on country name
	public void clickCountryCard(String countryName) {
		WebElement countryCard = driver.findElement(countryCard(countryName));
		countryCard.click();
		System.out.println("✅ Clicked on the " + countryName + " country card.");
	}

	// Method to get the FAQ section element
	public WebElement getFaqSection() {
		return driver.findElement(faqSection);
	}

	// Method to print the slider title
	public void printSliderTitle() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(handpickedDealsTitle));
		System.out.println("===========================================");
		System.out.println("   " + title.getText());
		System.out.println("===========================================");
	}

	// Method to click the "Next" arrow
	public void clickNextArrow() throws InterruptedException {
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement next = wait.until(ExpectedConditions.elementToBeClickable(nextArrow));
		next.click();
	}

	public void clickDealByIndex(int index) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Use presenceOfAllElements to avoid issues with visibility
		List<WebElement> allDeals = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("span.btn-reposive")));

		if (index >= 0 && index < allDeals.size()) {
			WebElement deal = allDeals.get(index);

			// Scroll and JS click
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", deal);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", deal);
		} else {
			System.out.println("Invalid index: " + index + ". Available deals: " + allDeals.size());
		}
	}

	public String getHomePagePriceByIndex(int index) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    List<WebElement> prices = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(dealPrices));
	    Thread.sleep(1000);
	    if (index >= 0 && index < prices.size()) {
	        return prices.get(index).getText().trim();
	    } else {
	        System.out.println("Invalid price index: " + index + ". Total available: " + prices.size());
	        return "N/A";
	    }
	}
	
	public void clickViewAllButton() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    By viewAllButton = By.xpath("//div[contains(@class, 'flex justify-end')]/span[contains(., 'View All')]");
	    wait.until(ExpectedConditions.elementToBeClickable(viewAllButton)).click();
	}
	
	// ===== Slider Interaction =====
    public void scrollToSlider() {
        WebElement slider = driver.findElement(By.xpath("//div[contains(@class,'offer-slider')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider);
    }

    public int getSliderDotCount() {
        return driver.findElements(sliderDots).size();
    }

    public void clickSliderDot(int index) {
        List<WebElement> dots = driver.findElements(sliderDots);
        dots.get(index).click();
    }

    public String getVisibleDiscoverDealText() {
        // Return text from currently visible slide deal text if needed, else just placeholder
        return ""; // you can implement if you want, for now we don't need this
    }
    
	public void clickSliderImage() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(sliderImage));
            
            List<WebElement> images = driver.findElements(sliderImage);
            System.out.println("Total slider images found: " + images.size());

            WebElement visibleImg = null;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).isDisplayed()) {
                    visibleImg = images.get(i);
                    break;
                }
            }

            if (visibleImg == null) {
                throw new NoSuchElementException("No visible slider image found");
            }

            Thread.sleep(2000);
            
            wait.until(ExpectedConditions.visibilityOf(visibleImg));
            wait.until(ExpectedConditions.elementToBeClickable(visibleImg));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibleImg);
            visibleImg.click();

        } catch (Exception e) {
            System.out.println("Exception while clicking slider image: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
	
    // Check if "Discover the Deal" title is visible on detail page
    public boolean isDiscoverDealTitleVisible() {
        return !driver.findElements(discoverDealTitle).isEmpty() && driver.findElement(discoverDealTitle).isDisplayed();
    }

    // Get character count of deal description on detail page
    public int getDealDescriptionCharCount() {
        WebElement desc = driver.findElement(dealDescription);
        return desc.getText().length();
    }
    
    public void clickFirstDealCard() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(firstDealCard));
        card.click();
    }
    
    public String getFirstDealPrice() {
        WebElement card = driver.findElement(firstDealCard);
        WebElement priceElement = card.findElement(firstDealPrice);

        String fullText = priceElement.getText().trim(); // "$1599per person" or similar
        // Remove non-digit/letter characters after the price (if any)
        String priceOnly = fullText.replaceAll("[^\\d$.,]", ""); // Keeps $, digits, comma, dot
        return priceOnly;
    }
}
