package com.meettwo.spring.config.initializers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.meettwo.security.OAuth2ServerConfiguration;
import com.meettwo.security.WebSecurityConfig;
import com.meettwo.spring.config.AppConfig;
import com.meettwo.spring.config.RabbitMQConfig;
import com.meettwo.spring.config.SolrConfig;

/**
 * {@link SpringMvcInitializer} provides the convenient methods for mvc project
 * configuration by extending the
 * {@link AbstractAnnotationConfigDispatcherServletInitializer} class
 * 
 * @author Nageswara rao.ch
 * @version 1.0
 */

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[]{AppConfig.class,WebSecurityConfig.class,OAuth2ServerConfiguration.class,SolrConfig.class,RabbitMQConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		
		return new String[]{"/"};
	}
	
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
		filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
		servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");

	}

}
