package com.uniovi.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

	private Properties props;
	private static PropertyLoader loader;

	private PropertyLoader() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("src/testConfig.properties");
			props = new Properties();
			props.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static PropertyLoader getInstance() {
		if (loader == null) {
			loader = new PropertyLoader();
		}
		return loader;
	}
	
	public String getProperty(String prop) {
		return props.getProperty(prop);
	}
}
