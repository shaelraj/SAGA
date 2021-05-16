/**
 * 
 */
package com.javamonks.estore.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

/**
 * @author shaelraj
 *
 */

@Value
public class RejectOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final String reason;
	
}
