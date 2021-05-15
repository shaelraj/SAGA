/**
 * 
 */
package com.javamonk.estore.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaelraj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEventQuery {
	
	private String productId;

}
