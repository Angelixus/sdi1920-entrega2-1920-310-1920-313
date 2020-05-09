package com.uniovi.tests;

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
public class FriendRequestTest {
	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";

	private String email = "";

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
		if (!email.equals("")) {
			MongoClient client = new MongoClient(
					new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
			MongoDatabase database = client.getDatabase("socialnetwork");
			Bson filter = Filters.eq("email", email);
			BasicDBObject updateQuery = new BasicDBObject();
			updateQuery.append("$set", new BasicDBObject().append("friendRequest_ids", new ObjectId[0]));
			database.getCollection("usuarios").updateOne(filter, updateQuery);
			client.close();
		}
		email = "";
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
	public void PR15() {
		email = "GOHWinner@gmail.com";
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Rage@Sobrevalorado.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_PrivateView.sendFriendRequest(driver, "//*[@id=\"sendRequestButton5eb2cd388d2c8f56dc489a08\"]");
				
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		
		PO_LoginView.fillForm(driver, email, "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_NavView.clickOption(driver, "peticiones", "class", "btn btn-info");
		
		PO_PrivateView.checkElement(driver, "text", "Petra");
		PO_PrivateView.checkElement(driver, "text", "Picapiedra");
	}
	
	@Test
	public void PR16() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Rage@Sobrevalorado.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_PrivateView.sendFriendRequest(driver, "//*[@id=\"sendRequestButton5eb2cd388d2c8f56dc489a08\"]");
				
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		
		email = "GOHWinner@gmail.com";
		PO_LoginView.fillForm(driver, "Rage@Sobrevalorado.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
				
		PO_PrivateView.checkElement(driver, "text", "Petición de amistad ya enviada o recibida.");
	}
	
	@Test
	public void PR17() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "Rage@Sobrevalorado.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_PrivateView.sendFriendRequest(driver, "//*[@id=\"sendRequestButton5eb2cd388d2c8f56dc489a08\"]");
				
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		
		email = "GOHWinner@gmail.com";
		PO_LoginView.fillForm(driver, "Tortuga@Negra.com", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
				
		PO_PrivateView.sendFriendRequest(driver, "//*[@id=\"sendRequestButton5eb2cd388d2c8f56dc489a08\"]");

		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		
		PO_LoginView.fillForm(driver, email, "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-default");
		
		PO_NavView.clickOption(driver, "peticiones", "class", "row text-center");
		
		PO_PrivateView.checkElement(driver, "text", "Petra");
		PO_PrivateView.checkElement(driver, "text", "Picapiedra");
		PO_PrivateView.checkElement(driver, "text", "Rak");
		PO_PrivateView.checkElement(driver, "text", "Cocodrilo");
	}

}
