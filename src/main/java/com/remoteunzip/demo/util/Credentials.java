package com.remoteunzip.demo.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Credentials {

	public Credentials(String basicAuth) throws UnsupportedEncodingException {
		byte[] credentials = Base64.getDecoder().decode(basicAuth);

		String[] credentialsArray = new String(credentials, "UTF-8").split(":");
		
		username = credentialsArray[0];

		password = credentialsArray[1];	
		
		this.basicAuth = basicAuth;
	}
	
	public Credentials(String username, String password) {

		String concat = username + ":" + password;
		
		basicAuth = Base64.getEncoder().encodeToString(concat.getBytes());
		
		this.username = username;

		this.password = password;	
	}
	
	public String getBasicAuth() {
		return basicAuth;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private String username;

	private String password;	
		
	private String basicAuth;
	
}
