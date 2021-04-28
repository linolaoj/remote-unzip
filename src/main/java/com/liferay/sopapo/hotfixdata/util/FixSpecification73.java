package com.liferay.sopapo.hotfixdata.util;

import org.springframework.stereotype.Component;

@Component
public class FixSpecification73 extends FixSpecification72 {

	@Override
	public String getDocumentationFileName() {
		return "fixpack_documentation.json";
	}

	@Override
	public String getDocumentationContentFormat() {
		return "json";
	}
	
	@Override
	public String getVersion() {
		return "7.3.10";
	}
}
