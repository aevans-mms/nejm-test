package org.mms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.nejm.org/");
		String title = driver.getTitle();
		System.out.println("title: " + title);

		driver.quit();
	}
}