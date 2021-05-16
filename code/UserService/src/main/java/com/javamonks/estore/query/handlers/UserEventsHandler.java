/**
 * 
 */
package com.javamonks.estore.query.handlers;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.javamonks.estore.core.model.PaymentDetails;
import com.javamonks.estore.core.model.User;
import com.javamonks.estore.core.query.dto.FetchUserPaymentDetailsQuery;

/**
 * @author shaelraj
 *
 */
@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("1234565544666")
                .cvv("123")
                .name("SHAEL RAJ")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Bipin")
                .lastName("Kumar")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return user;
    }
    
    
}
