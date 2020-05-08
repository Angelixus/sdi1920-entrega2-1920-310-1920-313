package com.uniovi.tests;
import static org.junit.Assert.assertTrue;

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

import com.uniovi.properties.PropertyLoader;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_View;


//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class SocialNetworkTests {
	//En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens automáticas)):
	//static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	//static String Geckdriver024 = "C:\\Path\\geckodriver024win64.exe";
	//En MACOSX (Debe ser la versión 65.0.1 y desactivar las actualizacioens automáticas):
	static String PathFirefox65 = PropertyLoader.getInstance().getProperty("firefox_path");
	//static String PathFirefox64 = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
	static String Geckdriver024 = PropertyLoader.getInstance().getProperty("geckodriver_path");
	//static String Geckdriver022 = "/Users/delacal/Documents/SDI1718/firefox/geckodriver023mac";
	//Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024); 
	static String URL = "https://localhost:8081";


	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}


	@Before
	public void setUp(){
		driver.navigate().to(URL);
	}
	@After
	public void tearDown(){
		driver.manage().deleteAllCookies();
	}
	@BeforeClass 
	static public void begin() {
		PO_View.setTimeout(3);

	}
	@AfterClass
	static public void end() {
		driver.quit();
	}

	//PR01. Sin hacer /
	@Test
	public void PR01() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
	}

	//PR02. Sin hacer /
	@Test
	public void PR02() {
		assertTrue("PR02 sin hacer", false);			
	}

	//PR03. Sin hacer /
	@Test
	public void PR03() {
		assertTrue("PR03 sin hacer", false);			
	}
	
	//PR04. Sin hacer /
	@Test
	public void PR04() {
		assertTrue("PR04 sin hacer", false);			
	}
	
	//PR05. Sin hacer /
	@Test
	public void PR05() {
		assertTrue("PR05 sin hacer", false);			
	}
	
	//PR06. Sin hacer /
	@Test
	public void PR06() {
		assertTrue("PR06 sin hacer", false);			
	}
	
	//PR07. Sin hacer /
	@Test
	public void PR07() {
		assertTrue("PR07 sin hacer", false);			
	}	
	
	//PR08. Sin hacer /
	@Test
	public void PR08() {
		assertTrue("PR08 sin hacer", false);			
	}	
	
	//PR09. Sin hacer /
	@Test
	public void PR09() {
		assertTrue("PR09 sin hacer", false);			
	}	
	//PR10. Sin hacer /
	@Test
	public void PR10() {
		assertTrue("PR10 sin hacer", false);			
	}	
	
	//PR11. Sin hacer /
	@Test
	public void PR11() {
		assertTrue("PR11 sin hacer", false);			
	}	
	
	//PR12. Sin hacer /
	@Test
	public void PR12() {
		assertTrue("PR12 sin hacer", false);			
	}	
	
	//PR13. Sin hacer /
	@Test
	public void PR13() {
		assertTrue("PR13 sin hacer", false);			
	}	
	
	//PR14. Sin hacer /
	@Test
	public void PR14() {
		assertTrue("PR14 sin hacer", false);			
	}	
	
	//PR15. Sin hacer /
	@Test
	public void PR15() {
		assertTrue("PR15 sin hacer", false);			
	}	
	
	//PR16. Sin hacer /
	@Test
	public void PR16() {
		assertTrue("PR16 sin hacer", false);			
	}	
	
	//PR017. Sin hacer /
	@Test
	public void PR17() {
		assertTrue("PR17 sin hacer", false);			
	}	
	
	//PR18. Sin hacer /
	@Test
	public void PR18() {
		assertTrue("PR18 sin hacer", false);			
	}	
	
	//PR19. Sin hacer /
	@Test
	public void PR19() {
		assertTrue("PR19 sin hacer", false);			
	}	
	
	//P20. Sin hacer /
	@Test
	public void PR20() {
		assertTrue("PR20 sin hacer", false);			
	}	
	
	//PR21. Sin hacer /
	@Test
	public void PR21() {
		assertTrue("PR21 sin hacer", false);			
	}	
	
	//PR22. Sin hacer /
	@Test
	public void PR22() {
		assertTrue("PR22 sin hacer", false);			
	}	
	
	//PR23. Sin hacer /
	@Test
	public void PR23() {
		assertTrue("PR23 sin hacer", false);			
	}	
	
	//PR24. Sin hacer /
	@Test
	public void PR24() {
		assertTrue("PR24 sin hacer", false);			
	}	
	//PR25. Sin hacer /
	@Test
	public void PR25() {
		assertTrue("PR25 sin hacer", false);			
	}	
	
	//PR26. Sin hacer /
	@Test
	public void PR26() {
		assertTrue("PR26 sin hacer", false);			
	}	
	
	//PR27. Sin hacer /
	@Test
	public void PR27() {
		assertTrue("PR27 sin hacer", false);			
	}	
	
	//PR029. Sin hacer /
	@Test
	public void PR29() {
		assertTrue("PR29 sin hacer", false);			
	}

	//PR030. Sin hacer /
	@Test
	public void PR30() {
		assertTrue("PR30 sin hacer", false);			
	}
	
	//PR031. Sin hacer /
	@Test
	public void PR31() {
		assertTrue("PR31 sin hacer", false);			
	}
	
		
}

