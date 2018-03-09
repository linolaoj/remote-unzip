package com.remoteunzip.demo.util;

public class PatchLinkUtil {

	private static final String BASE_URL = "https://files.liferay.com/private/ee/fix-packs/";
	
	public static String buildHotfixLink(String version, String fixid) {
		
		return BASE_URL + version + "/hotfix/liferay-hotfix-" + fixid + ".zip";
	}
	
	public static String buildFixpackLink(String version, String fixPackId) {
		
		String fixPackLink = BASE_URL + version;

		if ("7.0.10".equals(version)) {
			fixPackLink = fixPackLink + "/de/liferay-fix-pack-de-" + fixPackId + ".zip";
		}
		else if ("6.2.10".equals(version)) {
			fixPackLink = fixPackLink + "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
		}
		
		return fixPackLink;
	}
}
