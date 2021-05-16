/**
 * 
 */
package com.javamonks.estore.order.core;

import lombok.Value;

/**
 * @author shaelraj
 *
 */
@Value
public class OrderSummary {

	private final String orderId;
	private final OrderStatus orderStatus;
	private final String message;
	
}