package com.liferay.sopapo.hotfixdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;

import com.liferay.sopapo.hotfixdata.util.FileUtil;
import com.liferay.sopapo.hotfixdata.util.FixSpecification;
import com.liferay.sopapo.hotfixdata.util.FixSpecificationRegister;
import com.liferay.sopapo.hotfixdata.util.PatchLinkUtil;
import org.json.JSONObject;
import org.json.XML;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unzip")
@EnableAutoConfiguration
public class MyUnzipRemoteFile {

    @Autowired
    private PatchLinkUtil patchLinkUtil;
    @Autowired
    private FixSpecificationRegister fixSpecificationRegister;

    @GetMapping("/hotfix/{version}/{fixid}")
    ResponseEntity<Resource> getHotfixDoc(
            Authentication auth, @PathVariable String version,
            @PathVariable String fixid)
            throws FileNotFoundException {

        FixSpecification fixSpecification =
            fixSpecificationRegister.getFixSpecification(version);

        String hotfixLink = patchLinkUtil.buildHotfixLink(version, fixid);
       
        return sendFileFromLink(auth, hotfixLink, fixSpecification);
    }

    @GetMapping("/fixpack/{version}/{fixPackId}")
    ResponseEntity<Resource> getFixPackDoc(
            Authentication auth, @PathVariable String version,
            @PathVariable String fixPackId)
            throws FileNotFoundException {

        FixSpecification fixSpecification =
            fixSpecificationRegister.getFixSpecification(version);
        
        String fixPackLink = patchLinkUtil.buildFixpackLink(version, fixPackId);

        return sendFileFromLink(auth, fixPackLink, fixSpecification);
    }

    @ExceptionHandler(FileNotFoundException.class)
    ResponseEntity<Object> handleFileNotFound(
            FileNotFoundException ex) {

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Resource> sendFileFromLink(Authentication auth, String patchLink, FixSpecification fixSpecification)
            throws FileNotFoundException {

        try {
            prepareAuthenticator(auth);

            File extracted = FileUtil.extractFile(fixSpecification.getDocumentationFileName(), patchLink);

            if (extracted != null) {

                String rawContent = new String(Files.readAllBytes(extracted.toPath()));

                JSONObject obj = extractJsonObject(rawContent, fixSpecification.getDocumentationContentFormat());

                ByteArrayResource resource = new ByteArrayResource(obj.toString().getBytes());
                
                return ResponseEntity.ok()
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(resource);
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new FileNotFoundException();
            }
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject extractJsonObject(String content, String contentFormat) {
        if("json".equalsIgnoreCase(contentFormat)) {
            return new JSONObject(content);
        }
        return XML.toJSONObject(content);
    }

    private void prepareAuthenticator(Authentication auth) {
        String username = auth.getPrincipal().toString();
        String password = auth.getCredentials().toString();

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

}
