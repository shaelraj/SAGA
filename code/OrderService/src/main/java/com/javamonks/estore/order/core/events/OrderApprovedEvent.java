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
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
	
}