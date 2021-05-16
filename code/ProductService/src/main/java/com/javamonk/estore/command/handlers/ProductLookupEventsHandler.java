/**
 * 
 */
package com.javamonk.estore.command.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.javamonk.estore.entity.ProductLookupEntity;
import com.javamonk.estore.events.ProductCreatedEvent;
import com.javamonk.estore.repositries.ProductLookupRepositry;

/**
 * @author shaelraj
 *
 */
@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	
	private ProductLookupRepositry repo;
	
	
	
	public ProductLookupEventsHandler(ProductLookupRepositry repo) {
		this.repo = repo;
	}



	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());
		repo.save(entity);
	} 

}
