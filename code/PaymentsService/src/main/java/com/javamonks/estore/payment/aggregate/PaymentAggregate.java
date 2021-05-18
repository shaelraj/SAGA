/**
 * 
 */
package com.javamonks.estore.payment.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.javamonks.estore.core.commands.ProcessPaymentCommand;
import com.javamonks.estore.core.events.PaymentProcessedEvent;

/**
 * @author shaelraj
 *
 */
@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;

    private String orderId;
    
    public PaymentAggregate() { }
    
    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){

    	if(processPaymentCommand.getPaymentDetail() == null) {
    		throw new IllegalArgumentException("Missing payment details");
    	}
    	
    	if(processPaymentCommand.getOrderId() == null) {
    		throw new IllegalArgumentException("Missing orderId");
    	}
    	
    	if(processPaymentCommand.getPaymentId() == null) {
    		throw new IllegalArgumentException("Missing paymentId");
    	}
	
        AggregateLifecycle.apply(new PaymentProcessedEvent(processPaymentCommand.getPaymentId(), 
                processPaymentCommand.getOrderId()));
    }

    @EventSourcingHandler
    protected void on(PaymentProcessedEvent paymentProcessedEvent){
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }
}
