/**
 * 
 */
package com.javamonk.estore.query.handlers;

import java.util.Optional;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.javamonk.estore.entity.ProductEntity;
import com.javamonk.estore.events.ProductCreatedEvent;
import com.javamonk.estore.repositries.ProductRepositry;
import com.javamonks.estore.core.events.ProductReservationCancelEvent;
import com.javamonks.estore.core.events.ProductReservedEvent;

/**
 * @author shaelraj
 *
 */
@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

	private ProductRepositry repo;

	public ProductEventsHandler(ProductRepositry repo) {
		this.repo = repo;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductEntity entity = new ProductEntity();
		BeanUtils.copyProperties(event, entity);
		try {
			repo.save(entity);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {
		throw exception;
	}

	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}

	@EventHandler
	public void on(ProductReservedEvent event) {
		Optional<ProductEntity> optional = repo.getProductByProductId(event.getProductId());
		optional.ifPresent(entity -> {
			entity.setQuantity(entity.getQuantity() - event.getQuantity());
			repo.save(entity);
		});
	}
	
	@EventHandler
	public void on(ProductReservationCancelEvent event) {
		Optional<ProductEntity> optional = repo.getProductByProductId(event.getProductId());
		optional.ifPresent(entity -> {
			entity.setQuantity(entity.getQuantity() + event.getQuantity());
			repo.save(entity);
		});
	}
}
