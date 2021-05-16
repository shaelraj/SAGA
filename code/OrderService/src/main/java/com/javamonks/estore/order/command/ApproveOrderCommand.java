/**
 * 
 */
package com.javamonks.estore.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@AllArgsConstructor
public class ApproveOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
}
