package com.uniovi.tests.pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegisterView extends PO_NavView {	
	
	static public void fillForm(WebDriver driver, String namep, String surnamep, String emailp, String passwordp, String passwordconfp) {
		WebElement dni = driver.findElement(By.name("nombre"));
		dni.click();
		dni.clear();
		dni.sendKeys(namep);
		WebElement name = driver.findElement(By.name("apellidos"));
		name.click();
		name.clear();
		name.sendKeys(surnamep);
		WebElement lastname = driver.findElement(By.name("email"));
		lastname.click();
		lastname.clear();
		lastname.sendKeys(emailp);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		WebElement passwordConfirm = driver.findElement(By.name("repeatpassword"));
		passwordConfirm.click();
		passwordConfirm.clear();
		passwordConfirm.sendKeys(passwordconfp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}
	
}
