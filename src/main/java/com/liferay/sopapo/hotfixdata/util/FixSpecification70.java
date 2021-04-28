package com.liferay.sopapo.hotfixdata.util;

import org.springframework.stereotype.Component;

@Component
public class FixSpecification70 extends FixSpecification62 {

	@Override
	public String buildFixpackLink(String fixPackId) {
		return "/de/liferay-fix-pack-de-" + fixPackId + ".zip";
	}

	@Override
	public String getVersion() {
		return "7.0.10";
	}
	
}
