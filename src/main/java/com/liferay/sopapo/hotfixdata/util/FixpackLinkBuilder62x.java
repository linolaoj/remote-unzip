package com.liferay.sopapo.hotfixdata.util;

import org.springframework.stereotype.Component;

@Component
public class FixpackLinkBuilder62x implements FixpackLinkBuilder {

	@Override
	public String buildFixpackLink(String fixPackId) {
		return "/portal/liferay-fix-pack-portal-" + fixPackId + ".zip";
	}

	@Override
	public String getVersion() {
		return "6.2.10";
	}

}
