/**
 * 
 */
package com.javamonk.estore.query.handlers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.javamonk.estore.entity.ProductEntity;
import com.javamonk.estore.models.ProductRestModel;
import com.javamonk.estore.query.ProductEventQuery;
import com.javamonk.estore.query.ProductsQuery;
import com.javamonk.estore.repositries.ProductRepositry;

/**
 * @author shaelraj
 *
 */
@Component
public class ProductQueryHandler {

	private ProductRepositry productRepositry;

	private final EventStore eventStore;

	public ProductQueryHandler(ProductRepositry productRepositry, EventStore eventStore) {
		this.productRepositry = productRepositry;
		this.eventStore = eventStore;
	}

	@QueryHandler
	public List<ProductRestModel> findProducts(ProductsQuery query) {

		List<ProductEntity> productsEntity = productRepositry.findAll();
		Function<ProductEntity, ProductRestModel> entityToModelFunc = (entity) -> {
			ProductRestModel model = new ProductRestModel();
			BeanUtils.copyProperties(entity, model);
			return model;
		};
		List<ProductRestModel> products = productsEntity.stream()
				.map(entityToModelFunc).collect(Collectors.toList());

		return products;
	}

	@QueryHandler
	public List<Object> findProductsEvent(ProductEventQuery query) {
		return eventStore.readEvents(query.getProductId()).asStream().map(s -> s.getPayload())
				.collect(Collectors.toList());

	}
}
