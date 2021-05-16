/**
 * 
 */
package com.javamonk.estore.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.javamonk.estore.commands.CreateProductCommand;
import com.javamonk.estore.events.ProductCreatedEvent;
import com.javamonks.estore.core.commands.CancelProductReservationCommand;
import com.javamonks.estore.core.commands.ReserverProductCommand;
import com.javamonks.estore.core.events.ProductReservationCancelEvent;
import com.javamonks.estore.core.events.ProductReservedEvent;

/**
 * @author shaelraj
 *
 */
@Aggregate
public class ProductAggregate {
	
	@AggregateIdentifier
	private  String productId; 
	private  String title;

	private  BigDecimal price;
	private  int quantity;

	public ProductAggregate() {
	}
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
		if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price cannot less than or equal  to zero");
		}
		
		if(createProductCommand.getTitle()== null ||createProductCommand.getTitle().isEmpty() ) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		
		ProductCreatedEvent event = new ProductCreatedEvent();
		BeanUtils.copyProperties(createProductCommand, event);
		AggregateLifecycle.apply(event);
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent event) {
		this.price= event.getPrice();
		this.productId= event.getProductId();
		this.quantity= event.getQuantity();
		this.title= event.getTitle();
	}
	
	@CommandHandler
	public ProductAggregate(ReserverProductCommand command) {
		// Here axon framework replay the event and 
		// create the aggregate so  we don't need to fetch quantity.
		if(quantity < command.getQuantity()) {
			throw new IllegalArgumentException("Insuffiecient no. of items in stocks");
		}
		
		ProductReservedEvent event = ProductReservedEvent.builder()
				.userId(command.getUserId())
				.productId(command.getProductId())
				.quantity(command.getQuantity())
				.orderId(command.getOrderId())
				.build();
		AggregateLifecycle.apply(event);
	}
	
	
	@EventSourcingHandler
	public void on(ProductReservedEvent event) {
		this.quantity -= event.getQuantity();
	}
	
	@CommandHandler
	public ProductAggregate(CancelProductReservationCommand command) {
		ProductReservationCancelEvent event = ProductReservationCancelEvent.builder()
				.userId(command.getUserId())
				.productId(command.getProductId())
				.quantity(command.getQuantity())
				.orderId(command.getOrderId())
				.build();
		AggregateLifecycle.apply(event);
	}
	
	@EventSourcingHandler
	public void on(ProductReservationCancelEvent event) {
		this.quantity += event.getQuantity();
	}
	

}
