/**
 * 
 */
package com.javamonk.estore.repositries;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javamonk.estore.entity.ProductEntity;

/**
 * @author shaelraj
 *
 */
@Repository
public interface ProductRepositry extends JpaRepository<ProductEntity, String>{

	Optional<ProductEntity> getProductByProductId(String productId);
	
	Optional<ProductEntity> getProductByProductIdOrTitle(String productId, String title);
}
