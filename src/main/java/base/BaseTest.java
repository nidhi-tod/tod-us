package base;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import utils.PopupCloser;

public class BaseTest {

    protected WebDriver driver;
    private PopupCloser popupCloser;
    private Thread popupThread;

    @Parameters("browser")
    @BeforeClass
    public void setup(@Optional("chrome") String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("❌ Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get(ConfigReader.get("baseUrl"));
        acceptCookiesIfPresent();

        popupCloser = new PopupCloser(driver);
        popupThread = new Thread(popupCloser);
        popupThread.start();
    }

    public void acceptCookiesIfPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'bg-[linear-gradient(90deg,#FF8533_0%,#EF5117_100%)')]")));
            acceptButton.click();
            System.out.println("✅ Clicked on 'Accept' button.");
        } catch (TimeoutException e) {
            System.out.println("⚠️ 'Accept' button not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error clicking 'Accept': " + e.getMessage());
        }
    }

    @AfterClass
    public void teardown() throws InterruptedException {
        popupCloser.stop();
        popupThread.join();
        if (driver != null) {
            driver.quit();
        }
    }
}
