/**
 * 
 */
package com.javamonks.estore.core.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shaelraj
 *
 */
@Data
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
	private String userId;
}
