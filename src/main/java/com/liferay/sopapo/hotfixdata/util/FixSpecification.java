package com.liferay.sopapo.hotfixdata.util;

public interface FixSpecification {
    
    public String buildFixpackLink(String fixPackId);

    public String getDocumentationFileName();

    public String getDocumentationContentFormat();
      
    public String getVersion();
}
