package com.tea.mservice.config.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean(name = "httpClientFactory")
	public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {

		SimpleClientHttpRequestFactory facotry = new SimpleClientHttpRequestFactory();
		facotry.setReadTimeout(30 * 1000);
		facotry.setConnectTimeout(30 * 1000);
		facotry.setBufferRequestBody(false);
		return facotry;
	}

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate(SimpleClientHttpRequestFactory factory) {

		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		return restTemplate;
	}
}
