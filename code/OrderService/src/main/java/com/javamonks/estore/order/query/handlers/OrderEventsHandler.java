/**
 * 
 */
package com.javamonks.estore.order.query.handlers;

import java.util.Optional;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.javamonks.estore.order.core.OrdersRepository;
import com.javamonks.estore.order.core.entity.OrderEntity;
import com.javamonks.estore.order.core.events.OrderApprovedEvent;
import com.javamonks.estore.order.core.events.OrderCreatedEvent;
import com.javamonks.estore.order.core.events.OrderRejectedEvent;

/**
 * @author shaelraj
 *
 */
@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

	private final OrdersRepository ordersRepository;

	public OrderEventsHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent event) throws Exception {
		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(event, orderEntity);

		ordersRepository.save(orderEntity);
	}

	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		Optional<OrderEntity> optional = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());

		if (!optional.isPresent()) {
			return;
		}
		OrderEntity orderEntity = optional.get();
		orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());

		ordersRepository.save(orderEntity);

	}

	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		Optional<OrderEntity> optional = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());

		if (!optional.isPresent()) {
			return;
		}
		OrderEntity orderEntity = optional.get();
		orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
		ordersRepository.save(orderEntity);
	}

}
