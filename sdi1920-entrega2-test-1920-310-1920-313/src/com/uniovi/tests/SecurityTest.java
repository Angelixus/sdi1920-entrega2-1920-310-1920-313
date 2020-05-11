package com.uniovi.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.properties.PropertyLoader;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_View;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecurityTest {

	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";

	private String urlToGo = "usuarios";
	
	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	@Before
	public void setUp() {
		String urlLocal = "https://localhost:8081/" + urlToGo;
		driver.navigate().to(urlLocal);
	}

	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {

	}

	@AfterClass
	static public void end() {
		driver.quit();
	}

	@Test
	public void PR20() {	
		PO_LoginView.checkElement(driver, "class", "btn btn-primary");
	}
	
	@Test
	public void PR21() {
		urlToGo = "peticiones";
		PO_LoginView.checkElement(driver, "class", "btn btn-primary");
	}
	
	@Test
	public void PR22() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		driver.navigate().to("https://localhost:8081/5eb45cea0bf25f4f442972da/amigos");
		PO_View.checkElement(driver, "text", "Accion no permitida");
	}
	
	@Test
	public void testInvalidAccessToFriendView() {
		urlToGo = "amigos";
		PO_LoginView.checkElement(driver, "class", "btn btn-primary");
	}

}
