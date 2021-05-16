/**
 * 
 */
package com.javamonks.estore.order.core;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javamonks.estore.order.core.entity.OrderEntity;

/**
 * @author shaelraj
 *
 */
public interface OrdersRepository extends JpaRepository <OrderEntity, String>{
	Optional<OrderEntity> findByOrderId(String orderId);
}
