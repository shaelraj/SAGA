/**
 * 
 */
package com.javamonk.estore.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaelraj
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="product_lookup")
public class ProductLookupEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7180291687193296077L;
	
	@Id
	private  String productId; 
	
	@Column(unique = true)
	private  String title;

}
