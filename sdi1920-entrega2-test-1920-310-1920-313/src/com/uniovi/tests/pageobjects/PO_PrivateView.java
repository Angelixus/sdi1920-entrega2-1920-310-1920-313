package com.uniovi.tests.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.uniovi.tests.util.SeleniumUtils;


public class PO_PrivateView extends PO_NavView{
	static public void fillFormAddMark(WebDriver driver, int userOrder, String descriptionp, String scorep)
	{	
		//Espero por que se cargue el formulario de asñadir nota (Concretamente el botón class="btn")
		PO_View.checkElement(driver, "class", "btn");
		//Seleccionamos el alumnos userOrder
	    new Select (driver.findElement(By.id("user"))).selectByIndex(userOrder);
	    //Rellenemos el campo de descripción
	    WebElement description = driver.findElement(By.name("description"));
		description.clear();
		description.sendKeys(descriptionp);
		WebElement score = driver.findElement(By.name("score"));
		score.click();
		score.clear();
		score.sendKeys(scorep);
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}
	
	public static void sendFriendRequest(WebDriver driver, String xpath) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", xpath, getTimeout());
		elementos.get(0).click();
	}

	public static void acceptFriendRequest(WebDriver driver, String xpath) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", xpath, getTimeout());
		elementos.get(0).click();		
	}
	
}