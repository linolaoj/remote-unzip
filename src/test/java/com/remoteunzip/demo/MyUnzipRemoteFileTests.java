package com.remoteunzip.demo;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.remoteunzip.demo.util.Credentials;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyUnzipRemoteFileTests {

	private WebTestClient webClient = WebTestClient.bindToServer() 
            .responseTimeout(Duration.ofMillis(30000)).build();
		
	
	private ConfigProperties properties;
	
	@Autowired
	public void setConfigProperties(ConfigProperties properties) {
	    this.properties = properties;
	}

	@Test
	public void fetchHotfixInfoTest() {
		Credentials credentials = new Credentials(properties.getUsername(), properties.getPassword());
		
		String basicAuth = 	credentials.getBasicAuth();
		
		this.webClient.get()
		.uri("http://localhost:8080/unzip/hotfix/7.0.10/1222-7010")
		.header("auth", basicAuth).exchange().expectStatus().isOk();
		
	}
}
