package com.meettwo.spring.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


/**
 * {@link MeetTwoConfig} class used to provide the common utility method to read
 * the properties from property file of project configuration
 * 
 * @author Nageswara rao.ch
 * @version 1.0
 */
public class MeetTwoConfig {
	
	@Autowired
	private Environment environment;
	
	private static MeetTwoConfig meettwoConfig = null;
	
	public static MeetTwoConfig getInstance() {
		return meettwoConfig;
	}
	
	public String getConfigValue(String configKey) {
		return environment.getProperty(configKey);
	}
	
	@PostConstruct
	public void initIt() throws Exception {
		meettwoConfig = this;
	}

}
