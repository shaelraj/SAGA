/**
 * 
 */
package com.javamonk.estore.models;

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
public class ProductRestModel {
	private  String productId; 
	private  String title;

	private  BigDecimal price;
	private  int quantity;
}
