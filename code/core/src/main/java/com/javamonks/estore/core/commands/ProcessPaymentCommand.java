/**
 * 
 */
package com.javamonks.estore.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.javamonks.estore.core.model.PaymentDetails;

import lombok.Builder;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@Builder
public class ProcessPaymentCommand {
	@TargetAggregateIdentifier
	private final String paymentId;
	private final String orderId;
	private final PaymentDetails paymentDetail;
}
