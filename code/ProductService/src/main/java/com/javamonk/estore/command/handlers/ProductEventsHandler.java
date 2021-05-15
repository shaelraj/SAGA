/**
 * 
 */
package com.javamonk.estore.command.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.javamonk.estore.entity.ProductEntity;
import com.javamonk.estore.events.ProductCreatedEvent;
import com.javamonk.estore.repositries.ProductRepositry;

/**
 * @author shaelraj
 *
 */
@Component
public class ProductEventsHandler {
	
	private ProductRepositry repo;

	public ProductEventsHandler(ProductRepositry repo) {
		this.repo = repo;
	}


	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductEntity entity = new ProductEntity();
		BeanUtils.copyProperties(event, entity);
		repo.save(entity);
	}
}
