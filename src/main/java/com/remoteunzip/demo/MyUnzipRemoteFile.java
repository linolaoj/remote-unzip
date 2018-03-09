package com.remoteunzip.demo;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.remoteunzip.demo.util.Credentials;
import com.remoteunzip.demo.util.FileUtil;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@RestController
@RequestMapping("/unzip")
@EnableAutoConfiguration
public class MyUnzipRemoteFile {
		
	private static final String DOCUMENTATION_FILE_NAME = "fixpack_documentation.xml";

	private static final String BASE_URL = "https://files.liferay.com/private/ee/fix-packs/";
	
	@RequestMapping(value="/hotfix/{version}/{fixid}", method=RequestMethod.GET)
	 ResponseEntity<Resource> getHotfixDoc(
			 @RequestHeader(value="auth") String basicAuth,
			 @PathVariable String version, @PathVariable String fixid) {
	
		String hotfixLink = BASE_URL + version + "/hotfix/liferay-hotfix-" + fixid + ".zip";
		
		return sendFileFromLink(basicAuth, hotfixLink);
	}

	@RequestMapping(value="/fixpack/{version}/{fixPackId}", method=RequestMethod.GET)
	 ResponseEntity<Resource> getFixPackDoc(
		 @RequestHeader(value="auth") String basicAuth,
		 @PathVariable String version, @PathVariable String fixPackId) {

		String fixPackLink = BASE_URL + version;

		if ("7.0.10".equals(version)) {
			fixPackLink = fixPackLink + "/de/liferay-fix-pack-de-" + fixPackId + ".zip";
		}
		else if ("6.2.10".equals(version)) {
			fixPackLink = fixPackLink + "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
		}

		return sendFileFromLink(basicAuth, fixPackLink);
	}


	private ResponseEntity<Resource> sendFileFromLink(String basicAuth, String patchLink) {
		
		try {
			prepareAuthenticator(new Credentials(basicAuth));
			
			File extracted = FileUtil.extractFile(DOCUMENTATION_FILE_NAME, patchLink);
			
			if(extracted != null) {
				
				ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(extracted.toPath()));

				return  ResponseEntity.ok()
		            .contentLength(extracted.length())
		            .contentType(MediaType.parseMediaType("application/xml"/*"application/octet-stream"*/))
		            .body(resource);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		 
		protected void configure(HttpSecurity http) throws Exception {
			 http
             .authorizeRequests()
             .anyRequest().permitAll();

		}
		
	}

	private void prepareAuthenticator(Credentials credentials) {
		String username = credentials.getUsername();
		String password = credentials.getPassword();	
		
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (username, password.toCharArray());
		    }
		});
	}

}
