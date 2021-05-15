/**
 * 
 */
package com.javamonk.estore.controllers.query;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javamonk.estore.models.ProductRestModel;
import com.javamonk.estore.query.ProductEventQuery;
import com.javamonk.estore.query.ProductsQuery;

/**
 * @author shaelraj
 *
 */
@RestController
@RequestMapping(value = "/api/v1/query")
public class ProductQueryController {
	
	@Autowired
	private QueryGateway queryGateway;
	
	@GetMapping("/products")
	public List<ProductRestModel> getProducts(){
		ProductsQuery productsQuery = new ProductsQuery();
		List<ProductRestModel> products = queryGateway.query(productsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
		return products;
		
	}
	
	@GetMapping("/products/{productId}")
	public List<Object> getProductEvents(@PathVariable(value = "productId") String productId){
		ProductEventQuery productsQuery = new ProductEventQuery();
		productsQuery.setProductId(productId);
		List<Object> products = queryGateway.query(productsQuery, ResponseTypes.multipleInstancesOf(Object.class)).join();
		return products;
		
	}

}
