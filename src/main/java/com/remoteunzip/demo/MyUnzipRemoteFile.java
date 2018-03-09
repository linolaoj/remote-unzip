package com.remoteunzip.demo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.remoteunzip.demo.util.Credentials;
import com.remoteunzip.demo.util.FileUtil;
import com.remoteunzip.demo.util.PatchLinkUtil;

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
			 @PathVariable String version, @PathVariable String fixid) 
				throws FileNotFoundException {
	
		String hotfixLink = PatchLinkUtil.buildHotfixLink(version, fixid);
		
		return sendFileFromLink(basicAuth, hotfixLink);
	}

	@RequestMapping(value="/fixpack/{version}/{fixPackId}", method=RequestMethod.GET)
	 ResponseEntity<Resource> getFixPackDoc(
		 @RequestHeader(value="auth") String basicAuth,
		 @PathVariable String version, @PathVariable String fixPackId) 
			throws FileNotFoundException {

		String fixPackLink = PatchLinkUtil.buildFixpackLink(version, fixPackId);
		
		return  sendFileFromLink(basicAuth, fixPackLink);
	}

   @ExceptionHandler(FileNotFoundException.class)
   ResponseEntity<Object> handleFileNotFound(
		   FileNotFoundException ex) {

	   return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }
	

	private ResponseEntity<Resource> sendFileFromLink(String basicAuth, String patchLink) 
		throws FileNotFoundException {
		
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
			if(e instanceof FileNotFoundException) {
				throw new FileNotFoundException();
			}
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
