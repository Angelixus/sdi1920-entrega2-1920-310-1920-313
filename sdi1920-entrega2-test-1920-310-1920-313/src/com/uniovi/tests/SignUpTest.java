package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.bson.Document;
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

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.uniovi.properties.PropertyLoader;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_RegisterView;

//Ordenamos las pruebas por el nombre del mÃ©todo
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpTest {
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
		MongoClient client = new MongoClient(new MongoClientURI("mongodb://admin:solo_leveling@socialnetwork-shard-00-00-iaytk.mongodb.net:27017,socialnetwork-shard-00-01-iaytk.mongodb.net:27017,socialnetwork-shard-00-02-iaytk.mongodb.net:27017/test?ssl=true&replicaSet=socialnetwork-shard-0&authSource=admin&retryWrites=true&w=majority"));
		MongoDatabase database = client.getDatabase("socialnetwork");
		Block<Document> printBlock = new Block<Document>() {
		       @Override
		       public void apply(final Document document) {
		           System.out.println(document.toJson());
		       }
		};
		database.getCollection("usuarios").find().forEach(printBlock);
		client.close();
	}

	@AfterClass
	static public void end() {
		driver.quit();
	}

	@Test
	public void PR01() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString() + "@uniovi.com", "123456", "123456");
		PO_LoginView.checkElement(driver, "class", "btn btn-primary");
	}

	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "", "123456", "123456");
		PO_HomeView.checkElement(driver, "text", "El campo email no puede estar vacio.");
		PO_RegisterView.fillForm(driver, "", UUID.randomUUID().toString(), UUID.randomUUID().toString() + "@uniovi.com", "123456", "123456");
		PO_HomeView.checkElement(driver, "text", "El campo nombre no puede estar vacio.");
		PO_RegisterView.fillForm(driver, UUID.randomUUID().toString(), "", UUID.randomUUID().toString() + "@uniovi.com", "123456", "123456");
		PO_HomeView.checkElement(driver, "text", "El campo apellidos no puede estar vacio.");
	}

	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString() + "@uniovi.com", "123456", "1234567");
		PO_HomeView.checkElement(driver, "text", "Las contraseñas no coinciden.");
	}
	@Test
	public void PR04() {
		assertTrue("PR04 sin hacer", false);
	}
}
