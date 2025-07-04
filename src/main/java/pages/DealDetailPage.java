package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class DealDetailPage {
    WebDriver driver;
    WebDriverWait wait;

    public DealDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locator for price on the detail page (third page)
    By detailPrice = By.xpath("//p[contains(text(),'$') and span[contains(text(),'per person')]]");
    By emailInquiryButton = By.xpath("//p[contains(.,'Email Inquiry')]");
    By nameInput = By.xpath("//input[@name='name']");
    //By phoneInput = By.xpath("//input[@name='contactNo']");
    By phoneInput = By.xpath("//input[@class='PhoneInputInput']");
    By emailInput = By.xpath("/html/body/div[2]/div[3]/form/div/div[1]/div[2]/div[2]/div/input");
    By messageBox = By.xpath("//textarea[@name='query']");
    By submitButton = By.xpath("//button[contains(text(),'SUBMIT')]");
    
    By dateInfo = By.xpath("//*[@id=\"root\"]/div[4]/div/div[2]/div[1]/div[2]/div[1]/p"); 
    
    By dealTitle = By.xpath("//p[contains(text(), 'Discover the Deal')]");
    By dealDescription = By.xpath("//p[contains(text(), 'Discover the Deal')]/following-sibling::div/div");
    
    
    private By airportDropdown = By.xpath("(//div[contains(@class, 'css-b62m3t-container')])[1]");
    private String dropdownOptionByText = "//div[@role='listbox']//div[@role='option' and normalize-space(text())='%s']";
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private By calendarPrices = By.cssSelector(".innerDayContent.lowest-price span");

    
    // Locator for Accommodation section heading
    private By accommodationSection = By.xpath("//p[contains(text(),'Accommodation')]");

    // Locator for all visible View Details buttons
    private By viewDetailsButtons = By.xpath("//p[contains(text(),'View Details') and not(@disabled)]");

    // Locator for About Hotel tab inside popup
    private By aboutHotelTab = By.xpath("//div[contains(@class,'MuiPaper-root')]//nav[contains(@class,'border-b-2')]//a[text()='About Hotel']");

    // Locator for hotel description inside popup
    private By hotelDescription = By.xpath("//div[starts-with(@id, 'about-hotel-')]");

    
    public String getDetailPrice() {
        String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(detailPrice)).getText();
        String price = priceText.replaceAll("[^0-9]", "");
        System.out.println("💰 Detail page price: " + price);
        return price;
    }

    public void submitEnquiryForm() throws InterruptedException {
    	    try {
    	        // Handle potential offer popup
    	        try {
    	            WebElement popupClose = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class,'close')]")));
    	            if (popupClose.isDisplayed()) {
    	                popupClose.click();
    	                System.out.println("✅ Closed offer popup before form.");
    	                Thread.sleep(15000); // wait for popup to fully close
    	            }
    	        } catch (Exception e) {
    	            System.out.println("⚠️ No popup appeared before enquiry form.");
    	        }

    	        // Click "Email Inquiry"
    	        WebElement emailBtn = wait.until(ExpectedConditions.elementToBeClickable(emailInquiryButton));
    	        emailBtn.click();
    	        System.out.println("✅ Clicked on Email Inquiry");

    	        // Fill Name
    	        WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(nameInput));
    	        nameField.clear();
    	        nameField.sendKeys("Test");
    	        System.out.println("Name entered");

    	        // Fill Phone
    	        WebElement phoneField = wait.until(ExpectedConditions.elementToBeClickable(phoneInput));
    	        phoneField.clear();
    	        phoneField.sendKeys("+1 (123) 456-7890");
    	        System.out.println("Contact number entered");
    	        
    	        // Fill Email using JavaScript (if element is not interactable)
    	        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(emailInput));
    	        emailField.clear();
	            emailField.sendKeys("qatest@travelodeal.net");
    	        System.out.println("Email entered");
    	     
    	        // Fill Message
    	        WebElement messageField = wait.until(ExpectedConditions.elementToBeClickable(messageBox));
    	        messageField.clear();
    	        messageField.sendKeys("This is a test enquiry.");
    	        System.out.println("Message entered");

    	        // Submit
    	        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
    	        submit.click();
    	        System.out.println("✅ Enquiry form submitted.");
    	        
    	        // ✅ Add this block to verify success popup
    	        try {
    	            
    	            By successMessage = By.xpath("//p[contains(text(), 'Thank you')]");
    	            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    	            longWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
    	            System.out.println("✅ Success popup appeared after enquiry submission.");

    	        } catch (Exception e) {
    	            System.err.println("❌ Success popup not found or took too long: " + e.getMessage());
    	        }

    	    } catch (Exception e) {
    	        System.err.println("❌ Test failed during enquiry form: " + e.getMessage());
    	        throw e;
    	    }
    	}
    
    
    public void compareDetailAndCalendarPrice1() {
        try {
            // Get main detail price
            String mainPriceText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'$') and span[contains(text(),'per person')]]"))).getText();
            String mainPrice = mainPriceText.replaceAll("[^0-9]", "");
            System.out.println("💰 Main detail page price: $" + mainPrice);

            // Get all calendar prices
            java.util.List<WebElement> allPrices = driver.findElements(By.xpath("//div[contains(@class,'DayPicker-Day')]//p[contains(text(),'$')]"));
            int lowestPrice = Integer.MAX_VALUE;
            for (WebElement el : allPrices) {
                String priceText = el.getText().replaceAll("[^0-9]", "");
                if (!priceText.isEmpty()) {
                    int price = Integer.parseInt(priceText);
                    if (price < lowestPrice) {
                        lowestPrice = price;
                    }
                }
            }
            System.out.println("📉 Lowest price in calendar: $" + lowestPrice);

            // Get green (cheapest) date's price
            WebElement greenPriceElement = driver.findElement(By.xpath("//div[contains(@class,'DayPicker-Day') and contains(@class,'green')]//p[contains(text(),'$')]"));
            String greenPriceText = greenPriceElement.getText().replaceAll("[^0-9]", "");
            int greenPrice = Integer.parseInt(greenPriceText);
            System.out.println("✅ Green colored (cheapest highlight) price: $" + greenPrice);

            // Compare main detail price and calendar (green) price
            if (mainPrice.equals(String.valueOf(greenPrice))) {
                System.out.println("✅ Main detail price matches green calendar price.");
            } else {
                System.err.println("❌ Main detail price does NOT match green calendar price.");
            }

            // Check if green is truly the cheapest
            if (greenPrice == lowestPrice) {
                System.out.println("✅ Green price is the lowest in the calendar.");
            } else {
                System.err.println("❌ Green price is not the lowest! Expected $" + lowestPrice + " but found $" + greenPrice);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed comparing calendar prices: " + e.getMessage());
        }
    }
    
    public String extractDateFromText() {
    	
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String dateText = wait.until(ExpectedConditions.visibilityOfElementLocated(dateInfo)).getText();
        // Extract only the date part before the dash
        String dateOnly = dateText.split("-")[0].trim();  // "10 May 2025"
        System.out.println("📅 Extracted date: " + dateOnly);
        return dateOnly;
    }
    
    public void clickDateAndFillPopup(String date, String name, String number, String email) {
        String[] parts = date.split(" ");
        String day = parts[0]; // e.g., "10"

        List<WebElement> calendarDays = driver.findElements(By.cssSelector(".rdrDayNumber div"));

        for (WebElement dayElement : calendarDays) {
            if (dayElement.getText().trim().equals(day)) {
                dayElement.click();
                System.out.println("✅ Clicked on date: " + date);

                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                    // Wait for popup to appear
                    WebElement popupTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(),'Connect with our Concierge')]")
                    ));
                    System.out.println("✅ Popup is visible" + popupTitle);

                    // Fill Name
                    WebElement nameInput = driver.findElement(By.name("name"));
                    nameInput.sendKeys(name);
                    System.out.println("✅ Entered name: " + name);

                    // Fill Phone
                    WebElement phoneInput = wait.until(ExpectedConditions.elementToBeClickable(By.className("PhoneInputInput")));
                    phoneInput.sendKeys(number);
                    System.out.println("✅ Entered number: " + number);

                    // Fill Email
                    // optional: avoid premature email interaction
                    //WebElement emailInput = driver.findElement(By.xpath("//input[@type='email' and @name='email' and contains(@placeholder, 'Email')]"));
					/*
					 * WebElement emailInput =
					 * wait.until(ExpectedConditions.visibilityOfElementLocated(
					 * By.xpath("//form//input[contains(@placeholder, 'Email')]") ));
					 * emailInput.sendKeys(email);
					 */
                   
                    WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    	    By.xpath("//div[contains(@class, 'MuiBox-root') and contains(@class, 'css-1g8ztiu')]//form//input[@type='email' and @name='email']")
                    	));
                    	emailInput.sendKeys(email);
                    	System.out.println("✅ Entered email: " + email);
                    	 
                    // Submit Form
                    Thread.sleep(5000); 	
                    WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'MuiBox-root') and contains(@class, 'css-1g8ztiu')]//form//button[@type='submit' and contains(@class, 'btn-main') and normalize-space(text())='Submit']")));
                    submitButton.click();
                    System.out.println("✅ Submitted popup form.");
                    Thread.sleep(5000);
                    // ✅ Verify Success Message
                    WebElement thankYouMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(), 'Thank you')]")
                    ));
                    System.out.println("✅ Submitted popup form." + thankYouMsg.getText());

                    if (thankYouMsg.getText().contains(name)) {
                        System.out.println("🎉 Success popup verified: " + thankYouMsg.getText());
                    } else {
                        System.err.println("⚠️ Success popup appeared, but name doesn't match.");
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error filling popup form: " + e.getMessage());
                }

                return;
            }
        }

        throw new NoSuchElementException("Date not found in calendar: " + date);
    }
    
    public void submitEmailEnquiryForm(String name, String phone, String email, String message) throws InterruptedException {
        try {
            try {
                WebElement popupClose = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class,'close')]")));
                if (popupClose.isDisplayed()) {
                    popupClose.click();
                    System.out.println("✅ Closed offer popup before form.");
                    Thread.sleep(15000);
                }
            } catch (Exception e) {
                System.out.println("⚠️ No popup appeared before enquiry form.");
            }

            WebElement emailBtn = wait.until(ExpectedConditions.elementToBeClickable(emailInquiryButton));
            emailBtn.click();
            System.out.println("✅ Clicked on Email Inquiry");

            WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(nameInput));
            nameField.clear();
            nameField.sendKeys(name);
            System.out.println("Name entered");

            WebElement phoneField = wait.until(ExpectedConditions.elementToBeClickable(phoneInput));
            phoneField.clear();
            phoneField.sendKeys(phone);
            System.out.println("Contact number entered");

            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(emailInput));
            emailField.clear();
            emailField.sendKeys(email);
            System.out.println("Email entered");

            WebElement messageField = wait.until(ExpectedConditions.elementToBeClickable(messageBox));
            messageField.clear();
            messageField.sendKeys(message);
            System.out.println("Message entered");

            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            submit.click(); // Uncomment this to actually submit the form
            System.out.println("✅ Enquiry form submitted.");

            try {
                By successMessage = By.xpath("//p[contains(text(), 'Thank you')]");
                WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
                longWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
                System.out.println("✅ Success popup appeared after enquiry submission.");
            } catch (Exception e) {
                System.err.println("❌ Success popup not found or took too long: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Test failed during enquiry form: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean isDiscoverTheDealTitleDisplayed() {
        try {
            return driver.findElement(dealTitle).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public int getDealDescriptionCharacterCount() {
        try {
            String desc = driver.findElement(dealDescription).getText();
            return desc.length();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
    
    public String getDetailPagePrice() {
        
        String fullText = driver.findElement(detailPrice).getText().trim(); // "$1599per person" or similar
        // Remove non-digit/letter characters after the price (if any)
        String priceOnly = fullText.replaceAll("[^\\d$.,]", ""); // Keeps $, digits, comma, dot
        return priceOnly;
    }
    
    public void compareDetailAndCalendarPrice() {
        try {
            // Get main detail price
            String mainPriceText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'$') and span[contains(text(),'per person')]]"))).getText();
            String mainPrice = mainPriceText.replaceAll("[^0-9]", "");
            System.out.println("💰 Main detail page price: $" + mainPrice);

            // Get all calendar prices
            List<WebElement> allPrices = driver.findElements(By.xpath("//div[contains(@class,'innerDayContent')]//span[contains(text(),'$')]"));
            int lowestPrice = Integer.MAX_VALUE;
            for (WebElement el : allPrices) {
                String priceText = el.getText().replaceAll("[^0-9]", "");
                if (!priceText.isEmpty()) {
                    int price = Integer.parseInt(priceText);
                    if (price < lowestPrice) {
                        lowestPrice = price;
                    }
                }
            }

            if (lowestPrice == Integer.MAX_VALUE) {
                System.err.println("❌ No valid calendar price found!");
            } else {
                System.out.println("📉 Lowest price in calendar: $" + lowestPrice);
            }

            // Get green-highlighted (selected + lowest) price
            List<WebElement> greenPriceElements = driver.findElements(
                By.xpath("//div[contains(@class,'innerDayContent') and contains(@class,'selected') and contains(@class,'lowest-price')]//span[contains(text(),'$')]"));

            if (greenPriceElements.isEmpty()) {
                System.err.println("❌ No green-highlighted date with price found.");
                return;
            }

            String greenPriceText = greenPriceElements.get(0).getText().replaceAll("[^0-9]", "");
            int greenPrice = Integer.parseInt(greenPriceText);
            System.out.println("✅ Green colored (cheapest highlight) price: $" + greenPrice);

            // Compare main detail price with green calendar price
            if (mainPrice.equals(String.valueOf(greenPrice))) {
                System.out.println("✅ Main detail price matches green calendar price.");
            } else {
                System.err.println("❌ Main detail price does NOT match green calendar price.");
            }

            // Check if green is actually the lowest
            if (greenPrice == lowestPrice) {
                System.out.println("✅ Green price is the lowest in the calendar.");
            } else {
                System.err.println("❌ Green price is not the lowest! Expected $" + lowestPrice + " but found $" + greenPrice);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed comparing calendar prices: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void selectRandomAirportAndPrintLowestPrice() {
        System.out.println("\n---------- ✈️ Selecting Random Airport and Finding Lowest Price ----------");
        try {
            // Click the airport dropdown
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(airportDropdown));
            dropdown.click();
            System.out.println("✈️ Airport dropdown opened.");

            // Fetch all dropdown options
            List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptions));
            if (options.isEmpty()) {
                System.err.println("❌ No airport options available.");
                return;
            }

            // Pick a random airport option
            Random rand = new Random();
            WebElement randomOption = options.get(rand.nextInt(options.size()));
            String airportName = randomOption.getText().trim();

            // Click the selected airport
            WebElement airportToSelect = driver.findElement(By.xpath(String.format(dropdownOptionByText, airportName)));
            airportToSelect.click();
            System.out.println("✅ Selected airport: " + airportName);

            // Wait for calendar refresh - better to use explicit wait for ajax if possible
            Thread.sleep(3000);

            // Read all calendar prices
            List<WebElement> prices = driver.findElements(calendarPrices);
            if (prices.isEmpty()) {
                System.err.println("❌ No prices found on the calendar.");
                return;
            }

            int lowestPrice = Integer.MAX_VALUE;
            String lowestDay = "";

            for (WebElement priceElement : prices) {
                String priceText = priceElement.getText().replaceAll("[^0-9]", "");
                if (!priceText.isEmpty()) {
                    int price = Integer.parseInt(priceText);

                    // Get the day text relative to price element
                    WebElement dayContainer = priceElement.findElement(By.xpath(".."));
                    String dayText = dayContainer.findElement(By.xpath("./div")).getText().trim();

                    if (price < lowestPrice) {
                        lowestPrice = price;
                        lowestDay = dayText;
                    }
                }
            }

            if (lowestPrice == Integer.MAX_VALUE) {
                System.err.println("❌ No valid prices detected on the calendar.");
            } else {
                System.out.println("📅 Lowest price for airport '" + airportName + "' is $" + lowestPrice + " on day: " + lowestDay);
            }

        } catch (Exception e) {
            System.err.println("❌ Error during airport selection or price retrieval: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("---------- ✈️ Airport Price Check Completed ----------\n");
    }

 // Scroll to Accommodation section
    public void scrollToAccommodationSection() {
        WebElement section = driver.findElement(accommodationSection);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", section);
    }

   
    public void verifyAccommodationDetails() throws InterruptedException {
        System.out.println("\n---------- 🏨 Verifying Accommodation Details ----------");
        
        // Scroll to the Accommodation section
        WebElement accommodationHeading = driver.findElement(accommodationSection);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", accommodationHeading);
        Thread.sleep(2000);
        System.out.println("⬇️ Scrolled to Accommodation section.");

        List<WebElement> buttons = driver.findElements(viewDetailsButtons);
        System.out.println("🔘 Found " + buttons.size() + " 'View Details' buttons.");

        for (int i = 0; i < buttons.size(); i++) {
            Thread.sleep(2000);

            // Re-fetch buttons to avoid stale element exception
            buttons = driver.findElements(viewDetailsButtons);
            WebElement btn = buttons.get(i);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            Thread.sleep(1000);
            
            System.out.println("\n➡️ Clicking 'View Details' button #" + (i + 1));
            btn.click();

            try {
                // Wait for About Hotel tab visibility
                WebElement aboutTab = wait.until(ExpectedConditions.visibilityOfElementLocated(aboutHotelTab));
                if (aboutTab.isDisplayed()) {
                    System.out.println("✅ 'About Hotel' tab is visible.");
                } else {
                    System.out.println("❌ 'About Hotel' tab NOT visible.");
                }

                // Get hotel description text length
                WebElement desc = driver.findElement(hotelDescription);
                String descriptionText = desc.getText().trim();
                System.out.println("📝 Hotel description length: " + descriptionText.length() + " characters");

                Thread.sleep(2000);

                // Close the popup by clicking outside
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.elementFromPoint(10, 10).click();");
                Thread.sleep(1000);
                System.out.println("✅ Popup closed by clicking outside.");

            } catch (Exception e) {
                System.err.println("⚠️ Exception during popup processing: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("---------- 🏨 Accommodation verification completed ----------\n");
    }
    
    // First visible image in the gallery
    private By firstGalleryImage = By.cssSelector("a[data-fancybox='gallery']:not(.hidden)");

    // Fancybox modal (usually adds class fancybox__container or similar)
    private By fancyboxModal = By.cssSelector(".fancybox__container");

    public void clickFirstGalleryImage() {
        driver.findElement(firstGalleryImage).click();
    }

    public boolean isFancyboxModalVisible() {
        try {
            return driver.findElement(fancyboxModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
}
