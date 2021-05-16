/**
 * 
 */
package com.javamonks.estore.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.javamonks.estore.order.core.OrderStatus;

import lombok.Builder;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@Builder
public class CreateOrderCommand {
	@TargetAggregateIdentifier
	public final String orderId;

	private final String userId;
	private final String productId;
	private final int quantity;
	private final String addressId;
	private final OrderStatus orderStatus;
}
