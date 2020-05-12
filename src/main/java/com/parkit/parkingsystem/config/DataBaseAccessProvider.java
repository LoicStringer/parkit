package com.parkit.parkingsystem.config;

import java.io.IOException;
import java.util.Properties;

public class DataBaseAccessProvider extends Properties {

	public DataBaseAccessProvider() {
		try {
			this.load(getClass().getClassLoader().getResourceAsStream("database_access.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getProdDbUrl() {
		return getProperty("urlProdDb");
	}

	public String getTestDbUrl() {
		return getProperty("urlTestDb");
	}

	public String getUser() {
		return getProperty("user");
	}

	public String getPassword() {
		return getProperty("password");
	}

}
