/**
 * 
 */
package com.javamonk.estore.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@Builder
public class CreateProductCommand {
	@TargetAggregateIdentifier
	private final String productId; 
	private final String title;

	private final BigDecimal price;
	private final int quantity;
}
