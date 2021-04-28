package com.liferay.sopapo.hotfixdata.util;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixSpecificationRegister {

	private HashMap<String, FixSpecification> fixSpecifications;
	
	@Autowired
	public FixSpecificationRegister(List<FixSpecification> fixSpecifications) {
		this.fixSpecifications = new HashMap<String, FixSpecification>();
		
		for(FixSpecification fixSpecification : fixSpecifications) {
			this.fixSpecifications.put(fixSpecification.getVersion(), fixSpecification);
		}
		
	}
	
	public FixSpecification getFixSpecification(String version) {
		return this.fixSpecifications.get(version);
	}
	
}
