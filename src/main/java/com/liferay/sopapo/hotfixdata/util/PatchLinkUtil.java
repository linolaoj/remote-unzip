package com.liferay.sopapo.hotfixdata.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sopapo")
public class PatchLinkUtil {

	public String buildHotfixLink(String version, String fixid) {

		return getFixpacksBaseURL() + version + "/hotfix/liferay-hotfix-" + fixid + ".zip";
	}

	public String buildFixpackLink(String version, String fixPackId) {

		String fixpackLink = getFixpacksBaseURL() + version;

		FixpackLinkBuilder fixpackLinkBuilder =
			fixpackLinkBuilderRegister.getFixpackLinkBuilder(version);
		
		fixpackLink = fixpackLink + fixpackLinkBuilder.buildFixpackLink(fixPackId);

		return fixpackLink;
	}

	public String getFixpacksBaseURL() {
		return fixpacksBaseURL;
	}

	public void setFixpacksBaseURL(String fixpacksBaseURL) {
		this.fixpacksBaseURL = fixpacksBaseURL;
	}

	@Autowired
	private FixpackLinkBuilderRegister fixpackLinkBuilderRegister;
	
	private String fixpacksBaseURL;
}
