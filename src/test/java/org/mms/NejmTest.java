package org.mms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class NejmTest {

	WebDriver driver;
	Duration timeout = Duration.ofSeconds(10);
	WebDriverWait wait;

	String nejmUrl = "https://www.nejm.org/";

	static Logger log = LoggerFactory.getLogger(NejmTest.class);

	@BeforeEach
	public void startWebDriver() {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, timeout);
		driver.manage().window().maximize();
		loadWithTestCookie(nejmUrl);
		dismissGdpr();
//		dismissSurvey();
//		dismissPopup();
	}

	public void loadWithTestCookie(String url) {
		log.debug("reload site with cookie qatest=true");
		driver.get(url);
		driver.manage().addCookie(new Cookie("qatest", "true"));
		driver.get(url);
	}

	@AfterEach
	public void closeWebDriver() {
		sleep(10);
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
	public void createAccount() {
		WebElement create_account_link = driver.findElement(By.linkText("Create Account"));
		create_account_link.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email Address']")));
		emailField.sendKeys("aevans@mms.org");
	}

	@Test
	public void mouseOverSpecialtiesAndClick() {
		By specialtiesXpath = By.xpath("//a[@aria-label='Specialties']");
		WebElement specialties = whenVisible(specialtiesXpath);
		jsMoveMouseTo(specialties);

		By rheumatologyXpath = By.xpath("//nav//*[contains(text(), 'Rheumatology')]");
		WebElement rheumatologyLink = whenVisible(rheumatologyXpath);
		rheumatologyLink.click();

		dismissPopup();

		assertThat(driver.getCurrentUrl()).containsIgnoringCase("Rheumatology");
		assertThat(driver.getTitle()).containsIgnoringCase("Rheumatology");
	}


	@Test
	public void navigateToSpecialties() {
		By specialtiesXpath = By.xpath("//a[@aria-label='Specialties']");
		WebElement specialtiesTab = whenVisible(specialtiesXpath);
		jsMoveMouseTo(specialtiesTab).click();
	}

	@Test
	public void OpenSpecialtiesMenuAndClick() {
		By specialtiesXpath = By.xpath("//a[@aria-label='Specialties']");
		WebElement specialtiesTab = whenVisible(specialtiesXpath);
		specialtiesTab.click();

		By rheumatologyXpath = By.xpath("//nav//*[contains(text(), 'Rheumatology')]");
		WebElement rheumatologyLink = whenVisible(rheumatologyXpath);
		rheumatologyLink.click();

		dismissPopup();

		assertThat(driver.getCurrentUrl()).containsIgnoringCase("Rheumatology");
		assertThat(driver.getTitle()).containsIgnoringCase("Rheumatology");
	}

	@Test
	public void selectRheum() {
		WebElement specialties = driver.findElement(By.xpath("//a[@aria-label='Specialties']"));

//		/* unfortunately, this does not actually move the mouse */
//		Actions actions = new Actions(driver);
//		actions.moveToElement(specialties).build().perform();

		/* this will trigger an event that displays the specialties menu */
		moveMouseTo(specialties);

//		/* this complex nested xpath may not always work */
//		WebElement rheum = driver.findElement(By.xpath("//*[@id=\"nav\"]/ul/li[4]/div/div[1]/ul/li[18]/a"));
//		rheum.click();

		/* This simpler xpath searches for the link text is more reliable */
//		By rheumatologyXpath = By.xpath("//nav//*[contains(text(), 'Rheumatology')]");
//		WebElement rheumatologyLink = wait.until(elementToBeClickable(rheumatologyXpath));

		/* after the Specialities tab is activated  we can wait for the submenu to appear and then find Rheumatology by link text */
		WebElement rheumatologyLink = wait.until(elementToBeClickable(By.linkText("Rheumatology")));
		rheumatologyLink.click();

		String url = driver.getCurrentUrl();
		String expectedURL = "https://www.nejm.org/rheumatology?query=main_nav_lg";

		/* assertions should not depend on variables like the hostname or query parameters that may change */
		assertThat(driver.getCurrentUrl()).containsIgnoringCase("Rheumatology");
		assertThat(driver.getTitle()).containsIgnoringCase("Rheumatology");

		dismissPopup();

		WebElement heading = whenVisible(By.xpath("//h1[@id='mainContent']"));
		assertThat(heading.getText()).contains("Rheumatology");
	}

	@Test
	public void selectTopicsTab() {
		By topicsTabXpath = By.xpath("//a[@aria-label='Topics']");
		WebElement topicsTab = whenVisible(topicsTabXpath);
		log.debug("topicsTab element: \n" + getOuterHtml(topicsTab));

		jsMoveMouseTo(topicsTab);
		sleep(2);
		log.debug("topicsTab element: \n" + getOuterHtml(topicsTab));

		topicsTab.click();

//		Actions moveClick = new Actions(driver);
//		moveClick.moveToElement(topicsTab.findElement(By.xpath("./.."))).build().perform();
//
//		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		wait.until(ExpectedConditions.elementToBeClickable(topicsTabXpath)).click();

//
//		Actions moveClick = new Actions(driver);
//		moveClick.moveToElement(topicsTab).click().click().build().perform();
//
//		topicsTab.click();
//		System.out.println(topicsTab.getAttribute("outerHTML"));
//		topicsTab.click();


		String url = driver.getCurrentUrl();
		String title = driver.getTitle();

		log.info("got url: " + url);
		log.info("got title: " + title);

		dismissPopup();

		assertThat(url).containsIgnoringCase("topics");
		assertThat(title).containsIgnoringCase("topics");

	}


	public WebElement whenVisible(By locator) {
		return wait.until(visibilityOfElementLocated(locator));
	}

	public WebElement whenVisible(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(visibilityOfElementLocated(locator));
	}

	public WebElement whenClickable(By locator) {
		return wait.until(elementToBeClickable(locator));
	}

	public WebElement whenClickable(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(elementToBeClickable(locator));
	}

	public void findByXpath(String xpath) {
		var jsFindByXpath = "var findByXpath = function(xpath) {  return document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; };";
		executeScript(jsFindByXpath + "findByXpath(" + xpath + ")");
	}

	public void moveMouseTo(WebElement element) {
		String moveMouseScript =
				"var element = arguments[0];" +
				"var mouseMoveEvent = new MouseEvent('mouseenter', {bubbles: true, cancelable: true});" +
				"element.dispatchEvent(mouseMoveEvent);";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(moveMouseScript, element);
	}


	public WebElement jsMoveMouseTo(WebElement element) {
		log.debug("move mouse to : " + getOuterHtml(element));
		var jsMoveMouseTo = "var moveMouseTo = function(element) { element.dispatchEvent(new MouseEvent('mouseenter', {bubbles: true, cancelable: true})); return element; };";
		executeScript(jsMoveMouseTo + "moveMouseTo(element)", element);

		return element;
	}

	public void jsMoveMouseTo(By locator) {
		jsMoveMouseTo(whenVisible(locator));
	}

	public void jsClick(WebElement element) {
		log.debug("click: " + getOuterHtml(element));
		String jsClick = "element.click()";
		executeScript(jsClick, element);
	}

	public void jsClickHarder(WebElement element) {
		log.debug("click harder: " + getOuterHtml(element));

		String jsClickHarder = "var clickHarder = function(element) { element.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true})); return element; };";
		executeScript(jsClickHarder, element);
	}
	public void executeScript(String script, WebElement element) {
		script = "var element = arguments[0]; " + script;
		log.info("execute script: " + script);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var element = arguments[0]; " + script, element);
	}

	public void executeScript(String script) {
		log.info("execute script: " + script);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script);
	}

	public void dismissGdpr() {
		log.info("dismiss GDPR");
		By closeGDPRSelector = By.cssSelector("div.gdpr-msg__close");
		tryClick(closeGDPRSelector);
	}


	public void dismissSurvey() {
		log.info("dismiss Survey");
		By surveySelector =  By.cssSelector(".QSIPopOver");
		By dismissSurveySelector = By.cssSelector("*.QSIPopOver img[height='12']");

		tryClick(dismissSurveySelector, 10);
	}


	public void dismissPopup() {
		log.info("dismiss popup");
		By closeX = By.xpath("//button[@aria-label='Close']");
		tryClick(closeX);

	}

	public void tryClick(By locator) {
		tryClick(locator, timeout.getSeconds());
	}

	public void tryClick(By locator, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		WebElement element = null;

		try {
			element = driver.findElement(locator);
			element.click();
		}
		catch (NoSuchElementException e1) {
			log.debug("Element not found: " + locator);
//			log.info(e1.getMessage());

			try {
				element = wait.until(elementToBeClickable(locator));
			}
			catch (TimeoutException e2) {
				log.warn("Timeout waiting for element: " + locator);
//				log.debug(e2.getMessage());
			}
		}

		if (element != null) {
			element.click();
		}
	}

	public String getOuterHtml(WebElement element) {
		String html = element.getAttribute("outerHTML");
		return html.substring(0, html.indexOf(">") + 1);
	}

	public void sleep(int seconds) {
		try {
			long milliseconds = 1000L * seconds;
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.debug(e.getMessage());
		}
	}
}
