package com.liferay.sopapo.hotfixdata.util;

import org.springframework.stereotype.Component;

@Component
public class FixSpecification62 implements FixSpecification {

	@Override
	public String buildFixpackLink(String fixPackId) {
		return "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
	}

	@Override
	public String getVersion() {
		return "6.2.10";
	}

	@Override
	public String getDocumentationFileName() {
		return "fixpack_documentation.xml";
	}

	@Override
	public String getDocumentationContentFormat() {
		return "xml";
	}
}
