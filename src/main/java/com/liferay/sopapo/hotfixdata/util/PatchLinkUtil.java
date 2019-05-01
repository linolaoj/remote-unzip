package com.liferay.sopapo.hotfixdata.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sopapo")
public class PatchLinkUtil {

	public String buildHotfixLink(String version, String fixid) {
		
		return getFixpacksBaseURL() + version + "/hotfix/liferay-hotfix-" + fixid + ".zip";
	}
	
	public String buildFixpackLink(String version, String fixPackId) {
		
		String fixPackLink = getFixpacksBaseURL() + version;

		if ("7.0.10".equals(version)) {
			fixPackLink = fixPackLink + "/de/liferay-fix-pack-de-" + fixPackId + ".zip";
		}
		else if ("6.2.10".equals(version)) {
			fixPackLink = fixPackLink + "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
		}
		
		return fixPackLink;
	}

	public String getFixpacksBaseURL() {
		return fixpacksBaseURL;
	}

	public void setFixpacksBaseURL(String fixpacksBaseURL) {
		this.fixpacksBaseURL = fixpacksBaseURL;
	}

	private String fixpacksBaseURL;
}
