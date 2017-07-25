package com.meettwo.spring.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.MulticoreSolrClientFactory;

@Configuration
@EnableSolrRepositories(basePackages={"com.meettwo.solr.repositories"},multicoreSupport=true)
public class SolrConfig {

	@Autowired
	private Environment environment;

	@Bean
	public SolrClient solrClient() throws Exception {
		HttpSolrClient httpSolrClient = new HttpSolrClient(environment.getRequiredProperty("solr.host"));
		return httpSolrClient;
	}

	@Bean
	public SolrClientFactory solrClientFactory(SolrClient solrClient) {
		SolrClientFactory clientFactory = new MulticoreSolrClientFactory(solrClient);
		return clientFactory;
	}

	@Bean
	public SolrTemplate solrTemplateUser(SolrClientFactory solrClientFactory) {
		SolrTemplate solrTemplate = new SolrTemplate(solrClientFactory);
		solrTemplate.setSolrCore("user");
		return solrTemplate;
	}
}
