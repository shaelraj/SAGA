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
public class ProductReservationCancelEvent {
	private final String productId;
	private final String orderId;
	private final String userId;
	private final String reason;
	private final int quantity;
}
