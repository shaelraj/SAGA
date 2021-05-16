package com.javamonk.estore.command.interceptor;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javamonk.estore.commands.CreateProductCommand;
import com.javamonk.estore.entity.ProductLookupEntity;
import com.javamonk.estore.repositries.ProductLookupRepositry;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

	private final ProductLookupRepositry productLookupRepo;

	public CreateProductCommandInterceptor(ProductLookupRepositry productLookupRepo) {
		this.productLookupRepo = productLookupRepo;
	}

	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		return (index, command) -> {
			LOG.info("Command interceptor {}", command.getPayloadType());

			if (CreateProductCommand.class.equals(command.getPayloadType())) {

				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				Optional<ProductLookupEntity> optionalEntity = productLookupRepo.getProductByProductIdOrTitle(
						createProductCommand.getProductId(), createProductCommand.getTitle());

				/*
				 * if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) { throw
				 * new IllegalArgumentException("Price cannot less than or equal  to zero"); }
				 * 
				 * if (createProductCommand.getTitle() == null ||
				 * createProductCommand.getTitle().isEmpty()) { throw new
				 * IllegalArgumentException("Title cannot be empty"); }
				 */
				if(optionalEntity.isPresent()) {
					throw new IllegalArgumentException(String.format("Product with this productid: %s or title exist: %s"
							,createProductCommand.getProductId(),createProductCommand.getTitle()));
				}

			}
			return command;
		};
	}

}
