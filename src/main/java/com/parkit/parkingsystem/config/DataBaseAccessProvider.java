package com.parkit.parkingsystem.config;

import java.io.IOException;
import java.util.Properties;



/**
 * This class aims to provide an access to the database as a {@link Properties} object.
 * Constructed of the stream from the resources file "database_access.properties".
 *
 * Auto generated serialVersioUID.
 */
public class DataBaseAccessProvider extends Properties {

	private static final long serialVersionUID = 6730941522732039857L;

	public DataBaseAccessProvider() {
		try {
			this.load(getClass().getClassLoader().getResourceAsStream("database_access.properties"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public String getProdDbUrl() {
		return getProperty("prodDbUrl");
	}

	public String getTestDbUrl() {
		return getProperty("testDbUrl");
	}

	public String getUser() {
		return getProperty("user");
	}

	public String getPassword() {
		return getProperty("password");
	}

}
