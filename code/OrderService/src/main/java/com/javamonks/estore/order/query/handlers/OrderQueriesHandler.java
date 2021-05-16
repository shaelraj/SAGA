/**
 * 
 */
package com.javamonks.estore.order.query.handlers;

import java.util.Optional;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.javamonks.estore.order.core.OrderSummary;
import com.javamonks.estore.order.core.OrdersRepository;
import com.javamonks.estore.order.core.entity.OrderEntity;
import com.javamonks.estore.order.query.dto.FindOrderQuery;

/**
 * @author shaelraj
 *
 */
@Component
public class OrderQueriesHandler {

	OrdersRepository ordersRepository;

	public OrderQueriesHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
		Optional<OrderEntity> optional = ordersRepository.findByOrderId(findOrderQuery.getOrderId());

		if (!optional.isPresent()) {
			throw new IllegalStateException("Order id not found");
		}
		OrderEntity orderEntity = optional.get();
		return new OrderSummary(orderEntity.getOrderId(), 
				orderEntity.getOrderStatus(), "");
	}

}