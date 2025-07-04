package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PopupCloser implements  Runnable {
    private WebDriver driver;
    private volatile boolean running = true;

    public PopupCloser(WebDriver driver) {
        this.driver = driver;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Check every 1 seconds
                //System.out.println("Checking for pop-up");
                WebElement closeButton = driver.findElement(By.xpath("//*[contains(@class, 'css-gew3o5')]//div//div//*[contains(@class, 'lucide-x')]"));
                //WebElement closeButton = driver.findElement(By.cssSelector("svg.lucide-x"));
                if (closeButton.isDisplayed()) {
                    closeButton.click();
                    System.out.println("Popup closed.");
                }
            } catch (Exception e) {
                // Do nothing if popup not present
            }
        }
    }
}