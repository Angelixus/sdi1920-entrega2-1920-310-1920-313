package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
//Paquetes JUnit 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
//Paquetes Selenium 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.uniovi.properties.PropertyLoader;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_PrivateView;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserListTest {
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
	public void PR11() {
		MongoClient client = new MongoClient(
				new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
		MongoDatabase database = client.getDatabase("socialnetwork");
		FindIterable<Document> users = database.getCollection("usuarios").find();

		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		List<String> emails = new ArrayList<>();
		for (Document doc : users) {
			String email = (String) doc.get("email");
			if (!(email.equals("GOHWinner@gmail.com")))
				emails.add(email);
		}
		
		int modulus = emails.size() % 5;
		int pages = emails.size() / 5;
		if (modulus != 0)
			pages++;

		int localCounter = 0;
		for (int counter = 0; counter < pages; counter++) {
			for (int i = localCounter; i < emails.size(); i++) {
				PO_PrivateView.checkElement(driver, "text", emails.get(i));
				if (counter < pages) {
					if ((localCounter % ((5 * (counter + 1)) - 1)) == 0 && localCounter != 0) {
						localCounter++;
						break;
					}
				}
				localCounter++;
			}

			if (counter != pages - 1) {
				// Go to next page
				List<WebElement> paginationClicks = new ArrayList<WebElement>();
				if (counter == 0)
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[2]/a");
				else if(counter == 1)
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[3]/a");
				else
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[4]/a");

				// Only one page should be obtained
				assertTrue(paginationClicks.size() == 1);
				paginationClicks.get(0).click();
			}
		}
		client.close();
	}
	
	@Test
	public void PR12() {
		MongoClient client = new MongoClient(
				new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
		MongoDatabase database = client.getDatabase("socialnetwork");
		FindIterable<Document> users = database.getCollection("usuarios").find();

		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		List<String> emails = new ArrayList<>();
		for (Document doc : users) {
			String email = (String) doc.get("email");
			if (!(email.equals("GOHWinner@gmail.com")))
				emails.add(email);
		}
		
		WebElement password = driver.findElement(By.name("searchText"));
		password.click();
		password.clear();
		password.sendKeys("");
		
		PO_PrivateView.checkElement(driver, "free",
				"/html/body/div/div/form/button").get(0).click();
		
		int modulus = emails.size() % 5;
		int pages = emails.size() / 5;
		if (modulus != 0)
			pages++;

		int localCounter = 0;
		for (int counter = 0; counter < pages; counter++) {
			for (int i = localCounter; i < emails.size(); i++) {
				PO_PrivateView.checkElement(driver, "text", emails.get(i));
				if (counter < pages) {
					if ((localCounter % ((5 * (counter + 1)) - 1)) == 0 && localCounter != 0) {
						localCounter++;
						break;
					}
				}
				localCounter++;
			}

			if (counter != pages - 1) {
				// Go to next page
				List<WebElement> paginationClicks = new ArrayList<WebElement>();
				if (counter == 0)
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[2]/a");
				else if(counter == 1)
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[3]/a");
				else
					paginationClicks = PO_NavView.checkElement(driver, "free",
							"/html/body/div/div/div[2]/ul/li[4]/a");

				// Only one page should be obtained
				assertTrue(paginationClicks.size() == 1);
				paginationClicks.get(0).click();
			}
		}
		client.close();
	}
	
	@Test
	public void PR13() {
		MongoClient client = new MongoClient(
				new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
		MongoDatabase database = client.getDatabase("socialnetwork");
		FindIterable<Document> users = database.getCollection("usuarios").find();

		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		List<String> emails = new ArrayList<>();
		for (Document doc : users) {
			String email = (String) doc.get("email");
			if (!(email.equals("GOHWinner@gmail.com")))
				emails.add(email);
		}
		
		WebElement password = driver.findElement(By.name("searchText"));
		password.click();
		password.clear();
		password.sendKeys("THISDOESNOTEXIST");
		
		PO_PrivateView.checkElement(driver, "free",
				"/html/body/div/div/form/button").get(0).click();
		
		Boolean shouldBeTrue = PO_PrivateView.checkElementDoesNotExist(driver, "//*[@id=\"tableUsers\"]/tbody/tr[1]");
		assertTrue(shouldBeTrue);
		client.close();
	}
	
	@Test
	public void PR14() {
		MongoClient client = new MongoClient(
				new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
		MongoDatabase database = client.getDatabase("socialnetwork");
		FindIterable<Document> users = database.getCollection("usuarios").find();

		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "GOHWinner@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		List<String> emails = new ArrayList<>();
		for (Document doc : users) {
			String email = (String) doc.get("email");
			if (!(email.equals("GOHWinner@gmail.com")))
				emails.add(email);
		}
		
		WebElement password = driver.findElement(By.name("searchText"));
		password.click();
		password.clear();
		password.sendKeys("Monarch@gmail.com");
		
		PO_PrivateView.checkElement(driver, "free",
				"/html/body/div/div/form/button").get(0).click();
		
		PO_PrivateView.checkElement(driver, "text", "Monarch@gmail.com");
		PO_PrivateView.checkElement(driver, "text", "Jin");
		PO_PrivateView.checkElement(driver, "text", "Sung");
		Boolean shouldBeTrue = PO_PrivateView.checkElementDoesNotExist(driver, "//*[@id=\"tableUsers\"]/tbody/tr[2]");
		assertTrue(shouldBeTrue);
		
		password = driver.findElement(By.name("searchText"));
		password.click();
		password.clear();
		password.sendKeys("Jin");
		
		PO_PrivateView.checkElement(driver, "free",
				"/html/body/div/div/form/button").get(0).click();
		
		PO_PrivateView.checkElement(driver, "text", "Monarch@gmail.com");
		PO_PrivateView.checkElement(driver, "text", "Jin");
		PO_PrivateView.checkElement(driver, "text", "Sung");	
		shouldBeTrue = PO_PrivateView.checkElementDoesNotExist(driver, "//*[@id=\"tableUsers\"]/tbody/tr[2]");
		assertTrue(shouldBeTrue);
		
		password = driver.findElement(By.name("searchText"));
		password.click();
		password.clear();
		password.sendKeys("Sung");
		
		PO_PrivateView.checkElement(driver, "free",
				"/html/body/div/div/form/button").get(0).click();
		
		PO_PrivateView.checkElement(driver, "text", "Monarch@gmail.com");
		PO_PrivateView.checkElement(driver, "text", "Jin");
		PO_PrivateView.checkElement(driver, "text", "Sung");
		shouldBeTrue = PO_PrivateView.checkElementDoesNotExist(driver, "//*[@id=\"tableUsers\"]/tbody/tr[2]");
		assertTrue(shouldBeTrue);
		
		client.close();
	}

}
