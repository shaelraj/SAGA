/**
 * 
 */
package com.javamonks.estore.core.events;

import lombok.Builder;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@Builder
public class ProductReservedEvent {
	private  String productId;

	private  String orderId;
	private  String userId;
	private  int quantity;
}
