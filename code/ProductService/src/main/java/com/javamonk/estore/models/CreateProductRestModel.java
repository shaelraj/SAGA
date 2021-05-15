/**
 * 
 */
package com.javamonk.estore.models;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaelraj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRestModel {

	@NotNull(message = "Product title is required field")
	private String title;

	@Min(value=1, message = "Price cannot be less than one")
	private BigDecimal price;
	
	@Min(value=1, message = "Quantity cannot be less than one")
	@Max(value=5, message = "Quantity cannot be greater than five")
	private int quantity;
}
