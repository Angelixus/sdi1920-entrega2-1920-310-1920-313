package com.uniovi.tests;

import static org.junit.Assert.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.uniovi.properties.PropertyLoader;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_PrivateView;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendTest {

	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";


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
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {
	}

	@AfterClass
	static public void end() {
		MongoClient client = new MongoClient(
				new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
		MongoDatabase database = client.getDatabase("socialnetwork");
		Bson filterEmailA = Filters.eq("email", "Dick@Wizard.com");
		Bson filterEmailB = Filters.eq("email", "Borgar@gmail.com");

		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("friend_ids", new ObjectId[0]));
		database.getCollection("usuarios").updateOne(filterEmailA, updateQuery);
		database.getCollection("usuarios").updateOne(filterEmailB, updateQuery);
		client.close();
		driver.quit();
	}

	@Test
	public void PR18() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Dick@Wizard.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		PO_NavView.checkElement(driver, "free", "/html/body/div/div/div[2]/ul/li[3]/a").get(0).click();
		PO_PrivateView.sendFriendRequest(driver, "//*[@id=\"sendRequestButton5eb4654de75a5a53e89ad072\"]");

		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");

		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");

		PO_LoginView.fillForm(driver, "Borgar@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");

		PO_NavView.clickOption(driver, "peticiones", "class", "btn btn-info");

		PO_PrivateView.checkElement(driver, "text", "Merlin");
		PO_PrivateView.checkElement(driver, "text", "Avalon");

		PO_PrivateView.acceptFriendRequest(driver, "//*[@id=\"acceptFriendRequest5eb46524e75a5a53e89ad071\"]");
		Boolean shouldBeTrue = PO_PrivateView.checkElementDoesNotExist(driver,
				"//*[@id=\\\"acceptFriendRequest5eb46524e75a5a53e89ad071\\\"]");

		assertTrue(shouldBeTrue);
	}

	@Test
	public void PR19() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Dick@Wizard.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_NavView.clickOption(driver, "amigos", "class", "row text-center");
		
		PO_PrivateView.checkElement(driver, "free", "/html/body/div/div/div[1]/table/tbody/tr");
		PO_PrivateView.checkElement(driver, "text", "Artoria");
		PO_PrivateView.checkElement(driver, "text", "Pendragon");
		PO_PrivateView.checkElement(driver, "text", "Borgar@gmail.com");
		
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Borgar@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_NavView.clickOption(driver, "amigos", "class", "row text-center");
		
		PO_PrivateView.checkElement(driver, "free", "/html/body/div/div/div[1]/table/tbody/tr");
		PO_PrivateView.checkElement(driver, "text", "Merlin");
		PO_PrivateView.checkElement(driver, "text", "Avalon");
		PO_PrivateView.checkElement(driver, "text", "Dick@Wizard.com");
	}

}
