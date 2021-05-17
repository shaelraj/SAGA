package com.javamonk.estore;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.javamonk.estore.command.interceptor.CreateProductCommandInterceptor;
import com.javamonk.estore.error.handler.ProductServiceEventErrorHandler;
import com.javamonks.estore.core.config.AxonConfig;

@Import(AxonConfig.class)
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	@Autowired
	public void registerCommandMessageInterceptor(ApplicationContext context, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group",
				conf-> new ProductServiceEventErrorHandler());
		
		/*
		 * config.registerListenerInvocationErrorHandler("product-group", conf->
		 * PropagatingErrorHandler.instance());
		 */
	}

}
