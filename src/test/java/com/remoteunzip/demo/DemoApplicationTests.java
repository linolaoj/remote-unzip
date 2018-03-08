package com.remoteunzip.demo;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	
	private WebTestClient webClient = WebTestClient.bindToServer().build();
	
	@Test
	public void exampleTest() {
		this.webClient.get().uri("http://localhost:8080/").exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Hello World!");
	}

}
