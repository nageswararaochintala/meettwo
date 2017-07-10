package com.meettwo.spring.config;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * {@link AppConfig} class is the main configuration class that imports the all
 * other configuration classes and creates the necessary beans
 * 
 * @author Nageswara rao.ch
 * @version 1.0
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
//@EnableSpringDataWebSupport
@PropertySource(value="file:///${MeetTwoConfigPath}")
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "com.meettwo.dao", "com.meettwo.service", 
		"com.meettwo.rest.controller", 
		"com.meettwo.spring.aop",
})
@Import({DataSourceConfig.class, HibernateConfiguration.class, EmailConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter implements ApplicationListener<ContextStartedEvent> {
	
	
	
	@Bean
	public PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public MeetTwoConfig capeConfig() {
		return new MeetTwoConfig();
	}
	
	@Bean
	public ViewResolver configureViewResolver() {
		InternalResourceViewResolver viewResolve = new InternalResourceViewResolver();
		viewResolve.setOrder(2);
		viewResolve.setPrefix("/WEB-INF/home/");
		viewResolve.setSuffix(".jsp");

		return viewResolve;
	}

	@Bean(name={"multipartResolver"})
    public CommonsMultipartResolver createMultipartResolver() {
      CommonsMultipartResolver resolver = new CommonsMultipartResolver();
      resolver.setDefaultEncoding("utf-8");
      return resolver;
    }
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

	    
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		mapper.registerModule(new Hibernate4Module());
		mapper.setSerializationInclusion(Include.NON_NULL);
	    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(mapper);
	    converters.add(jsonConverter);
	    
	}

	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/imgs/**").addResourceLocations("imgs/");
	    registry.addResourceHandler("/css/**").addResourceLocations("css/");
	    registry.addResourceHandler("/js/**").addResourceLocations("js/");
	    registry.addResourceHandler("/templates/**").addResourceLocations("templates/");
	  }
	
	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		System.out.println("***********Spring Application Context Initialized***************");
		//artemisScheduledTasksService.removeExpiredSubscription();
		
	}
	
	
}
