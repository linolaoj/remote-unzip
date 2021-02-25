package com.liferay.sopapo.hotfixdata;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


import static com.liferay.sopapo.hotfixdata.util.BasicAuthenticationUtil.getAuthorization;

@Component
@ConfigurationProperties("sopapo")
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		HttpResponse<String> response = Unirest.get(getLiferayAuthURL())
		  .header("Authorization", getAuthorization(username, password))
		  .asString();

		if (response.getStatus() == HttpStatus.OK.value()) {
			return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
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