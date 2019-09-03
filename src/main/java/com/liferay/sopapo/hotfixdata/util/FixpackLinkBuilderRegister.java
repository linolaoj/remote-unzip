package com.liferay.sopapo.hotfixdata.util;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixpackLinkBuilderRegister {

	private HashMap<String, FixpackLinkBuilder> fixpackLinkBuilders;
	
	@Autowired
	public FixpackLinkBuilderRegister(List<FixpackLinkBuilder> fixpackLinkBuilders) {
		this.fixpackLinkBuilders = new HashMap<String, FixpackLinkBuilder>();
		
		for(FixpackLinkBuilder fixpackLinkBuilder : fixpackLinkBuilders) {
			this.fixpackLinkBuilders.put(fixpackLinkBuilder.getVersion(), fixpackLinkBuilder);
		}
		
	}
	
	public FixpackLinkBuilder getFixpackLinkBuilder(String version) {
		return this.fixpackLinkBuilders.get(version);
	}
	
}
