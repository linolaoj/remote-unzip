package com.liferay.sopapo.hotfixdata;

import com.liferay.sopapo.hotfixdata.util.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.liferay.sopapo.hotfixdata.util.BasicAuthenticationUtil.getAuthorization;

@Component
@ConfigurationProperties("sopapo")
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {
			String username = authentication.getName();
			String password = authentication.getCredentials().toString();
			URL url = new URL(getLiferayAuthURL());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod(HttpMethod.GET.name());

			con.setDoOutput(true);

			con.setRequestProperty("Authorization", getAuthorization(username, password));

			if (con.getResponseCode() == HttpStatus.OK.value()) {
				return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
				UsernamePasswordAuthenticationToken.class);
	}

	public String getLiferayAuthURL() {
		return liferayAuthURL;
	}

	public void setLiferayAuthURL(String liferayAuthURL) {
		this.liferayAuthURL = liferayAuthURL;
	}

	private String liferayAuthURL;
}