package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NejmTest {

	WebDriver driver;

	@BeforeEach
	public void startWebDriver() {
		driver = new ChromeDriver();
		driver.get("https://stag.nejm.org/");
	}

	@AfterEach
	public void closeWebDriver() {
		driver.quit();
	}

	@Test
	public void openHomePage() {
		String title = driver.getTitle();
		System.out.println("title: " + title);

		assertEquals("The New England Journal of Medicine: Research & Review Articles on Disease & Clinical Practice", title);
	}

	@Test
	public void checkPartialTitle() {
		String title = driver.getTitle();
		System.out.println("title: " + title);

		assertThat(title).contains("The New England Journal of Medicine");

	}

	@Test
	public void createAccount() throws InterruptedException {
		WebElement create_account_link = driver.findElement(By.linkText("Create Account"));
		create_account_link.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email Address']")));
		emailField.sendKeys("aevans@mms.org");
	}
}
