package com.remoteunzip.demo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@RestController
@RequestMapping("/unzip")
@EnableAutoConfiguration
public class MyUnzipRemoteFile {

		
	private static final String DOCUMENTATION_FILE_NAME = "fixpack_documentation.xml";


	@RequestMapping(value="/hotfix/{version}/{fixid}", method=RequestMethod.GET)
	 ResponseEntity<Resource> getHotfixDoc(
			 @RequestHeader(value="auth") String basicAuth,
			 @PathVariable String version, @PathVariable String fixid) {
	
		String hotfixLink = "https://files.liferay.com/private/ee/fix-packs/" + version + "/hotfix/liferay-hotfix-" + fixid + ".zip";
		
		_sendFileFromLink(basicAuth, hotfixLink);
		
		return null;
	}

	@RequestMapping(value="/fix/{version}/{fixPackId}", method=RequestMethod.GET)
	 ResponseEntity<Resource> getFixPackDoc(
			 @RequestHeader(value="auth") String basicAuth,
			 @PathVariable String version, @PathVariable String fixPackId) {
	
		String fixPackLink = "https://files.liferay.com/private/ee/fix-packs/" + version;

		if ("7.0.10".equals(version)) {
			fixPackLink = fixPackLink + "/de/liferay-fix-pack-de-" + fixPackId + ".zip";
		}
		else if ("6.2.10".equals(version)) {
			fixPackLink = fixPackLink + "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
		}

		_sendFileFromLink(basicAuth, fixPackLink);
		
		return null;
	}


	private void _sendFileFromLink(String basicAuth, String patchLink) {
		byte[] credentials = Base64.getDecoder().decode(basicAuth);
		
		try {

			String[] credentialsArray = new String(credentials, "UTF-8").split(":");
			
			String username = credentialsArray[0];
			
			String password = credentialsArray[1];	
				
			prepareAuthenticator(username, password);
			
			File extracted = extractFile(DOCUMENTATION_FILE_NAME, patchLink);
			
			if(extracted != null) {
				
				ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(extracted.toPath()));

			    ResponseEntity.ok()
			            .contentLength(extracted.length())
			            .contentType(MediaType.parseMediaType("application/xml"/*"application/octet-stream"*/))
			            .body(resource);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public File extractFile(String fileToExtract, String zipUrl) 
		throws IOException {
		
		if(fileToExtract == null || zipUrl == null)
			return null;
		
		URL url = new URL(zipUrl);
		
		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();
		
		while (!ze.getName().equals(fileToExtract)) {
		    zin.closeEntry();
		    ze = zin.getNextEntry();
		}
		
		File tempFile = File.createTempFile(fileToExtract, "unziped");
		
		
		FileOutputStream fos = new FileOutputStream(tempFile);
		
		int next = zin.read();

		while(next != -1) {
			
			fos.write(next);
			next = zin.read();
			
		} 
		
		fos.flush();
		fos.close();
		
		return tempFile;
	}


	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		 
		protected void configure(HttpSecurity http) throws Exception {
			
			 http
             .authorizeRequests()
             .anyRequest().permitAll();

		}
		
		
	   @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth)
	            throws Exception {
	        auth
	            .inMemoryAuthentication()
	                .withUser("user").password("password").roles("USER");
	    	
	    	
	    }
	
	}
	

	private void prepareAuthenticator(String user, String password) {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (user, password.toCharArray());
		    }
		});
	}

}
