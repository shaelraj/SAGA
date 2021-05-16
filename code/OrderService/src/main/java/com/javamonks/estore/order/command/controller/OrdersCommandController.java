/**
 * 
 */
package com.javamonks.estore.order.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javamonks.estore.order.command.CreateOrderCommand;
import com.javamonks.estore.order.command.dto.OrderCreateRest;
import com.javamonks.estore.order.core.OrderStatus;
import com.javamonks.estore.order.core.OrderSummary;
import com.javamonks.estore.order.query.dto.FindOrderQuery;

/**
 * @author shaelraj
 *
 */
@RestController
@RequestMapping(value = "/api/v1/order")
public class OrdersCommandController {

	private QueryGateway queryGateway;

	private CommandGateway commandGateway;

	@Autowired
	public OrdersCommandController(QueryGateway queryGateway, CommandGateway commandGateway) {
		this.queryGateway = queryGateway;
		this.commandGateway = commandGateway;
	}

	@PostMapping("/create")
	public OrderSummary createOrder(@Valid @RequestBody OrderCreateRest order) {

		String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
		String orderId = UUID.randomUUID().toString();

		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(order.getAddressId())
				.productId(order.getProductId()).userId(userId).quantity(order.getQuantity()).orderId(orderId)
				.orderStatus(OrderStatus.CREATED).build();

		SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
				new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummary.class),
				ResponseTypes.instanceOf(OrderSummary.class));

		try {
			commandGateway.sendAndWait(createOrderCommand);
			return queryResult.updates().blockFirst();
		} finally {
			queryResult.close();
		}

	}

}
