package com.meettwo.spring.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;


/**
 * {@link DataSourceConfig} class used to configure the {@link DataSource} to
 * project by creating the {@link DriverManagerDataSource} class bean object
 * 
 * @author amit.patel
 *
 */
@Configuration
public class DataSourceConfig {

	@Autowired
    private Environment environment;
	
	@Bean(name = "dataSource")
	public DataSource dataSource() throws NamingException {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
	    driverManagerDataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
	    driverManagerDataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
	    driverManagerDataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
	    return driverManagerDataSource;
		
		//return (DataSource)new JndiTemplate().lookup("java:/MySqlDS");
	}
	
}
