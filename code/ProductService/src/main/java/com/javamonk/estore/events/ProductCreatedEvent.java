/**
 * 
 */
package com.javamonk.estore.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaelraj
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {
	private  String productId; 
	private  String title;

	private  BigDecimal price;
	private  int quantity;
}
