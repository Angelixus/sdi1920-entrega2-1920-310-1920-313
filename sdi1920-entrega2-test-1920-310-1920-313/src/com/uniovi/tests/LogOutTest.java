package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.bson.conversions.Bson;
//Paquetes JUnit 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
//Paquetes Selenium 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.uniovi.properties.PropertyLoader;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogOutTest {
	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";

	private String name;

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	@After
	public void tearDown() {
		if (name != "") {
			MongoClient client = new MongoClient(
					new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
			MongoDatabase database = client.getDatabase("socialnetwork");
			Bson filter = Filters.eq("nombre", name);
			database.getCollection("usuarios").deleteOne(filter);
			client.close();
		}
		name = "";
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
	public void PR09() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
	}

	@Test
	public void PR10() {
		Boolean shouldBeTrue = PO_View.checkElementDoesNotExist(driver, "//nav//li//a[@href=\"/desconectarse\"]");
		assertTrue(shouldBeTrue);		
	}
}
