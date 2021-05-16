/**
 * 
 */
package com.javamonks.estore.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javamonks.estore.payment.entity.PaymentEntity;

/**
 * @author shaelraj
 *
 */
public interface PaymentRepository extends JpaRepository <PaymentEntity, String>{
    
}
