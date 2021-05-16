/**
 * 
 */
package com.javamonks.estore.order.core.events;

import com.javamonks.estore.order.core.OrderStatus;

import lombok.Value;

/**
 * @author shaelraj
 *
 */
@Value
public class OrderRejectedEvent {
	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}