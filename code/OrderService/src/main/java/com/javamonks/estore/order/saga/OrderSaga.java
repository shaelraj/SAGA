/**
 * 
 */
package com.javamonks.estore.order.saga;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.javamonks.estore.core.commands.CancelProductReservationCommand;
import com.javamonks.estore.core.commands.ProcessPaymentCommand;
import com.javamonks.estore.core.commands.ReserveProductCommand;
import com.javamonks.estore.core.events.PaymentProcessedEvent;
import com.javamonks.estore.core.events.ProductReservationCancelledEvent;
import com.javamonks.estore.core.events.ProductReservedEvent;
import com.javamonks.estore.core.model.User;
import com.javamonks.estore.core.query.dto.FetchUserPaymentDetailsQuery;
import com.javamonks.estore.order.command.ApproveOrderCommand;
import com.javamonks.estore.order.command.RejectOrderCommand;
import com.javamonks.estore.order.core.OrderSummary;
import com.javamonks.estore.order.core.events.OrderApprovedEvent;
import com.javamonks.estore.order.core.events.OrderCreatedEvent;
import com.javamonks.estore.order.core.events.OrderRejectedEvent;
import com.javamonks.estore.order.query.dto.FindOrderQuery;

/**
 * @author shaelraj
 *
 */
@Saga
public class OrderSaga {

	private static final Logger LOG = LoggerFactory.getLogger(OrderSaga.class);

	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	@Autowired
	private transient DeadlineManager deadlineManager;

	@Autowired
	private transient QueryUpdateEmitter queryUpdateEmitter;

	private final String PAYMENT_PROCESSING_TIMEOUT_DEADLINE = "payment-processing-deadline";

	private String scheduleId;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent event) {

		LOG.info("Ordercreated event called for orderID {} and productId {}", event.getOrderId(), event.getProductId());
		ReserveProductCommand command = ReserveProductCommand.builder().userId(event.getUserId())
				.productId(event.getProductId()).quantity(event.getQuantity()).orderId(event.getOrderId()).build();
		commandGateway.send(command, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				if (commandResultMessage.isExceptional()) {
					// start a compensation transaction
					RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId(),
							commandResultMessage.exceptionResult().getMessage());
					commandGateway.send(rejectOrderCommand);
				}

			}
		});
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent event) {
		LOG.info("ProductReservedEvent called for orderID {} and productId {}", event.getOrderId(),
				event.getProductId());
		FetchUserPaymentDetailsQuery query = new FetchUserPaymentDetailsQuery(event.getUserId());
		User paymentDetails = null;
		try {
			paymentDetails = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
		} catch (Exception ex) {
			LOG.error("Error occured in ProductReservedEvent : {}", ex.getMessage());
			// start compensation transaction
			cancelProductReservation(event, ex.getLocalizedMessage());
			return;
		}

		if (paymentDetails == null) {
			// start compensation transaction
			cancelProductReservation(event, "Unable to fetch user payment details");
			return;
		}

		LOG.info("Successfully fetched user payment details for user: {}", paymentDetails.getFirstName());

		scheduleId = deadlineManager.schedule(Duration.of(180, ChronoUnit.SECONDS), PAYMENT_PROCESSING_TIMEOUT_DEADLINE,
				event);

		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder().orderId(event.getOrderId())
				.paymentId(UUID.randomUUID().toString()).paymentDetail(paymentDetails.getPaymentDetails()).build();
		String result = null;
		try {
			result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
		} catch (Exception ex) {
			LOG.error("Error occured in sending ProcessPaymentCommand : {}", ex.getMessage());
			// start compensation transaction
			cancelProductReservation(event, ex.getLocalizedMessage());
			return;
		}

		if (result == null) {
			LOG.info("ProcessPaymentCommand result null. calling compensation transaction.");
			cancelProductReservation(event, "Unable to process user payments with provided details.");
		}
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		cancelDeadline();
		ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		commandGateway.send(approveOrderCommand);
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent event) {
		RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId(), event.getReason());
		commandGateway.send(rejectOrderCommand);
	}

	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent event) {
		LOG.info("Order is approved for orderId: {}", event.getOrderId());
		LOG.debug("Order is approved for orderId: {}", event.getOrderId());
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
				new OrderSummary(event.getOrderId(), event.getOrderStatus(), "Order Successful"));
//		SagaLifecycle.end();
	}

	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent event) {
		LOG.info("Order is rejected for orderId: {}", event.getOrderId());
		LOG.debug("Order is rejected for orderId: {}", event.getOrderId());
		
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
				new OrderSummary(event.getOrderId(), event.getOrderStatus(), event.getReason()));
	}

	private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {

		cancelDeadline();

		CancelProductReservationCommand publishProductReservationCommand = CancelProductReservationCommand.builder()
				.orderId(productReservedEvent.getOrderId()).productId(productReservedEvent.getProductId())
				.quantity(productReservedEvent.getQuantity()).userId(productReservedEvent.getUserId()).reason(reason)
				.build();

		commandGateway.send(publishProductReservationCommand);

	}

	// In case deadline not occured.
	private void cancelDeadline() {
		if (scheduleId != null) {
			deadlineManager.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT_DEADLINE, scheduleId);
			scheduleId = null;
		}
	}

	@DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
	public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
		LOG.info(
				"Payment processing deadline took place. Sending a compensating command to cancel the product reservation");
		cancelProductReservation(productReservedEvent, "Payment timeout");
	}

}
