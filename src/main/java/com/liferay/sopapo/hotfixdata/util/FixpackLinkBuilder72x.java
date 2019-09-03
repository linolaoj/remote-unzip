package com.liferay.sopapo.hotfixdata.util;

import org.springframework.stereotype.Component;

@Component
public class FixpackLinkBuilder72x implements FixpackLinkBuilder {

	@Override
	public String buildFixpackLink(String fixPackId) {
		return "/dxp/liferay-fix-pack-dxp-" + fixPackId + ".zip";
	}

	@Override
	public String getVersion() {
		return "7.2.10";
	}

}
