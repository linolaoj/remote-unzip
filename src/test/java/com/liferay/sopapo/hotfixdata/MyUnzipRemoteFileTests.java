package com.liferay.sopapo.hotfixdata;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import static com.liferay.sopapo.hotfixdata.util.BasicAuthenticationUtil.getAuthorization;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyUnzipRemoteFileTests {

	private ConfigProperties properties;

	@Autowired
	public void setConfigProperties(ConfigProperties properties) {
	    this.properties = properties;
	}

	@Test
	public void getHotfixDocResponseStatusTest() {
		String username = properties.getUsername();
		String password = properties.getPassword();
		WebTestClient webClient = getWebTestClient();

		webClient.get()
		.uri("http://localhost:8080/unzip/hotfix/7.0.10/1222-7010")
		.header("Authorization",
				getAuthorization(username, password))
		.exchange().expectStatus().isOk();

	}

	@Test
	public void getFixpackDocResponseStatusTest() {
		String username = properties.getUsername();
		String password = properties.getPassword();
		WebTestClient webClient = getWebTestClient();

		webClient.get()
		.uri("http://localhost:8080/unzip/fixpack/6.2.10/164-6210")
		.header("Authorization",
				getAuthorization(username, password))
		.exchange().expectStatus().isOk();

	}

	@Test
	public void getFixpackDocResponseContentTest() {
		String username = properties.getUsername();
		String password = properties.getPassword();
		WebTestClient webClient = getWebTestClient();

		webClient.get()
		.uri("http://localhost:8080/unzip/fixpack/6.2.10/164-6210")
		.header("Authorization",
				getAuthorization(username, password))
		.exchange().expectStatus().isOk().expectBody()
		.consumeWith(response ->{
			Assertions.assertThat(response.getResponseBody()).isNotNull();

			String body = new String(response.getResponseBody());

			JSONObject fixDocJson = new JSONObject(body);

			JSONObject patchJson = fixDocJson.getJSONObject("patch");

			Assert.assertEquals("portal-164-6210", patchJson.getString("id"));
			Assert.assertEquals(83011128, patchJson.getLong("build-id"));
		});

	}
	
	@Test
	public void getFixpack72DocResponseContentTest() {
		String username = properties.getUsername();
		String password = properties.getPassword();
		WebTestClient webClient = getWebTestClient();

		webClient.get()
		.uri("http://localhost:8080/unzip/fixpack/7.2.10/1-7210")
		.header("Authorization",
				getAuthorization(username, password))
		.exchange().expectStatus().isOk().expectBody()
		.consumeWith(response ->{
			Assertions.assertThat(response.getResponseBody()).isNotNull();

			String body = new String(response.getResponseBody());

			JSONObject fixDocJson = new JSONObject(body);

			JSONObject patchJson = fixDocJson.getJSONObject("patch");

			Assert.assertEquals("dxp-1-7210", patchJson.getString("id"));
			Assert.assertEquals("dxp-1", patchJson.getString("build-name"));
		});

	}

	@Test
	public void getHotfixDocResponseContentTest() {
        String username = properties.getUsername();
        String password = properties.getPassword();
        WebTestClient webClient = getWebTestClient();

        webClient.get()
        .uri("http://localhost:8080/unzip/hotfix/7.0.10/1222-7010")
		.header("Authorization",
				getAuthorization(username, password))
		.exchange().expectStatus().isOk().expectBody()
		.consumeWith(response ->{
			Assertions.assertThat(response.getResponseBody()).isNotNull();

			String body = new String(response.getResponseBody());

			JSONObject fixDocJson = new JSONObject(body);

			JSONObject patchJson = fixDocJson.getJSONObject("patch");

			Assert.assertEquals("hotfix-1222-7010", patchJson.getString("id"));
			Assert.assertEquals(76752134, patchJson.getLong("build-id"));
		});

	}

	private WebTestClient getWebTestClient() {
		return WebTestClient.bindToServer()
		.responseTimeout(Duration.ofMillis(10000)).build();
	}
}
