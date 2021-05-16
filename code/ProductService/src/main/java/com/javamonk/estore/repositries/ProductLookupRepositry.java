/**
 * 
 */
package com.javamonk.estore.repositries;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javamonk.estore.entity.ProductLookupEntity;

/**
 * @author shaelraj
 *
 */
@Repository
public interface ProductLookupRepositry extends JpaRepository<ProductLookupEntity, String>{

	Optional<ProductLookupEntity> getProductByProductId(String productId);
	
	Optional<ProductLookupEntity> getProductByProductIdOrTitle(String productId, String title);
}
