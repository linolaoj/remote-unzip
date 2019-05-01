package com.liferay.sopapo.hotfixdata.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sopapo")
public class Configs {
    public String getFixpacksBaseURL() {
        return fixpacksBaseURL;
    }

    public void setFixpacksBaseURL(String fixpacksBaseURL) {
        this.fixpacksBaseURL = fixpacksBaseURL;
    }

    public String getLiferayAuthURL() {
        return liferayAuthURL;
    }

    public void setLiferayAuthURL(String liferayAuthURL) {
        this.liferayAuthURL = liferayAuthURL;
    }

    private String fixpacksBaseURL;
    private String liferayAuthURL;
}
