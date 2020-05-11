package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.uniovi.properties.PropertyLoader;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_PrivateView;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTest {

	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081/cliente.html";

	boolean readAllMessages = false;
	
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
		if(readAllMessages) {
			MongoClient client = new MongoClient(
					new MongoClientURI(PropertyLoader.getInstance().getProperty("mongodb_connection")));
			MongoDatabase database = client.getDatabase("socialnetwork");
			Bson filter = Filters.eq("destino", "Monarch@gmail.com");

			BasicDBObject updateQuery = new BasicDBObject();
			updateQuery.append("$set", new BasicDBObject().append("leido", true));
			database.getCollection("mensajes").updateMany(filter, updateQuery);
			client.close();
		}
		readAllMessages = false;
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
	public void PR23() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");
	}

	@Test
	public void PR24() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("UselessEmail@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_LoginView.checkElement(driver, "text", "Usuario no encontrado");
	}

	@Test
	public void PR25() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Akira", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Kurusu", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sasuga@Joker.com", 8);
	}

	@Test
	public void PR26() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);

		List<WebElement> input = PO_PrivateView.checkElementCustomTimeout(driver, "id", "filtro-nombre", 8);
		assertTrue(input.size() == 1);
		input.get(0).click();
		input.get(0).sendKeys("Silva");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Silva", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Bosque", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Equilibrio@Loca.com", 8);

		Boolean shouldBeTrue = PO_PrivateView.checkElementDoesNotExistCustomTimeout(driver,
				"//*[contains(text(),'Monarch@gmail.com')]", 8);
		assertTrue(shouldBeTrue);
	}

	@Test
	public void PR27() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);

		List<WebElement> chatButton = PO_PrivateView.checkElement(driver, "free", "//*[@id=\"5eb45cea0bf25f4f442972da\"]/td[5]/a");

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
	}
	
	@Test
	public void PR28() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);

		List<WebElement> chatButton = PO_PrivateView.checkElement(driver, "free", "//*[@id=\"5eb45cea0bf25f4f442972da\"]/td[5]/a");

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
		
		List<WebElement> input = PO_PrivateView.checkElement(driver, "id", "agregar-texto");
		assertTrue(input.size() == 1);
		input.get(0).click();
		String uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		
		List<WebElement> send = PO_PrivateView.checkElement(driver, "id", "boton-agregar");
		assertTrue(send.size() == 1);
		send.get(0).click();
		
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		readAllMessages = true;
	}
	
	@Test
	public void PR29() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);

		List<WebElement> chatButton = PO_PrivateView.checkElement(driver, "free", "//*[@id=\"5eb45cea0bf25f4f442972da\"]/td[5]/a");

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
		
		List<WebElement> input = PO_PrivateView.checkElement(driver, "id", "agregar-texto");
		assertTrue(input.size() == 1);
		input.get(0).click();
		String uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		
		List<WebElement> send = PO_PrivateView.checkElement(driver, "id", "boton-agregar");
		assertTrue(send.size() == 1);
		send.get(0).click();
		
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		
		driver.manage().deleteAllCookies();

		driver.navigate().to(URL);
		
		
		button = PO_LoginView.checkElement(driver, "id", "boton-login");

		email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("Monarch@gmail.com");
		password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Mori", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "GOHWinner@gmail.com", 8);

		PO_PrivateView.checkElementCustomTimeout(driver, "free", "//*[@id=\"5eb2cd388d2c8f56dc489a08\"]/td[4]/div/span[contains(text(), \"1\")]", 8);
		chatButton = PO_PrivateView.checkElement(driver, "free", "//*[@id=\"5eb2cd388d2c8f56dc489a08\"]/td[5]/a");

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
		PO_PrivateView.checkElementCustomTimeout(driver, "free", "((//div[@class=\'chat-message-container\']/div)[last()])//span[last()][contains(text(), \"Leido\")]", 4);
		
	}
	
	@Test
	public void PR30() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Sung", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Monarch@gmail.com", 8);

		List<WebElement> chatButton = PO_PrivateView.checkElement(driver, "free", "//*[@id=\"5eb45cea0bf25f4f442972da\"]/td[5]/a");

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
		
		List<WebElement> input = PO_PrivateView.checkElement(driver, "id", "agregar-texto");
		assertTrue(input.size() == 1);
		input.get(0).click();
		String uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		
		List<WebElement> send = PO_PrivateView.checkElement(driver, "id", "boton-agregar");
		assertTrue(send.size() == 1);
		send.get(0).click();
		
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		input.get(0).click();
		uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		send.get(0).click();
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		input.get(0).click();
		uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		send.get(0).click();
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		
		driver.manage().deleteAllCookies();

		driver.navigate().to(URL);
		
		
		button = PO_LoginView.checkElement(driver, "id", "boton-login");

		email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("Monarch@gmail.com");
		password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Jin", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "Mori", 8);
		PO_PrivateView.checkElementCustomTimeout(driver, "text", "GOHWinner@gmail.com", 8);

		PO_PrivateView.checkElementCustomTimeout(driver, "free", "//*[@id=\"5eb2cd388d2c8f56dc489a08\"]/td[4]/div/span[contains(text(), \"3\")]", 8);
	}
	
	@Test
	public void PR31() {
		List<WebElement> button = PO_LoginView.checkElement(driver, "id", "boton-login");

		WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		List<WebElement> chatButton = PO_PrivateView.checkElementCustomTimeout(driver, "free", "((//tbody/tr)[last()]//td)[last()]/a", 8);
		List<WebElement> userEmailElement = PO_PrivateView.checkElementCustomTimeout(driver, "free", "(//tbody/tr)[last()]//td[3]", 8);
		assertTrue(userEmailElement.size() == 1);
		String userEmail = userEmailElement.get(0).getText();
		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		PO_PrivateView.checkElementCustomTimeout(driver, "id", "chat-messages", 4);
		
		List<WebElement> input = PO_PrivateView.checkElement(driver, "id", "agregar-texto");
		assertTrue(input.size() == 1);
		input.get(0).click();
		String uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		
		List<WebElement> send = PO_PrivateView.checkElement(driver, "id", "boton-agregar");
		assertTrue(send.size() == 1);
		send.get(0).click();
		
		PO_PrivateView.checkElementCustomTimeout(driver, "text", uuid, 4);
		
		driver.manage().deleteAllCookies();

		driver.navigate().to(URL);
		
		button = PO_LoginView.checkElement(driver, "id", "boton-login");

		email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		userEmailElement = PO_PrivateView.checkElementCustomTimeout(driver, "free", "//tbody/tr[1]//td[3]", 8);
		assertTrue(userEmailElement.size() == 1);
		String userEmailNow = userEmailElement.get(0).getText();
		assertTrue(userEmail.equals(userEmailNow));
		
		userEmailElement = PO_PrivateView.checkElementCustomTimeout(driver, "free", "(//tbody/tr)[last()]//td[3]", 8);
		assertTrue(userEmailElement.size() == 1);
		String userEmailLater = userEmailElement.get(0).getText();
		
		driver.manage().deleteAllCookies();

		driver.navigate().to(URL);
		
		button = PO_LoginView.checkElement(driver, "id", "boton-login");

		email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys(userEmailLater);
		password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");

		chatButton = PO_PrivateView.checkElementCustomTimeout(driver, "free", "//*[@id=\"5eb2cd388d2c8f56dc489a08\"]/td[5]/a", 8);

		assertTrue(chatButton.size() == 1);
		chatButton.get(0).click();

		input = PO_PrivateView.checkElement(driver, "id", "agregar-texto");
		assertTrue(input.size() == 1);
		input.get(0).click();
		uuid = UUID.randomUUID().toString();
		input.get(0).sendKeys(uuid);
		
		send = PO_PrivateView.checkElement(driver, "id", "boton-agregar");
		assertTrue(send.size() == 1);
		send.get(0).click();
		
		driver.manage().deleteAllCookies();

		driver.navigate().to(URL);
		
		button = PO_LoginView.checkElement(driver, "id", "boton-login");

		email = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/input"));
		email.clear();
		email.click();
		email.sendKeys("GOHWinner@gmail.com");
		password = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/input"));
		password.clear();
		password.click();
		password.sendKeys("123456");

		assertTrue(button.size() == 1);
		button.get(0).click();

		PO_PrivateView.checkElement(driver, "id", "filtro-nombre");
		
		userEmailElement = PO_PrivateView.checkElementCustomTimeout(driver, "free", "//tbody/tr[1]//td[3]", 8);
		assertTrue(userEmailElement.size() == 1);
		userEmailNow = userEmailElement.get(0).getText();
		assertTrue(userEmailLater.equals(userEmailNow));
	}
}
