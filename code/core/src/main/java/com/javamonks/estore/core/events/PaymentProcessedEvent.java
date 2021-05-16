/**
 * 
 */
package com.javamonks.estore.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@AllArgsConstructor
public class PaymentProcessedEvent {
	
	private String paymentId;
	private String orderId;

}
