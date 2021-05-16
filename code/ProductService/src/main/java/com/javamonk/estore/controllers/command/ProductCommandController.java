/**
 * 
 */
package com.javamonk.estore.controllers.command;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javamonk.estore.commands.CreateProductCommand;
import com.javamonk.estore.models.CreateProductRestModel;

/**
 * @author shaelraj
 *
 */
@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductCommandController {

	private Environment env;

	private CommandGateway commandGateway;

	@Autowired
	public ProductCommandController(Environment env, CommandGateway commandGateway) {
		this.env = env;
		this.commandGateway = commandGateway;
	}

	@PostMapping("/create")
	public String createProduct(@Valid @RequestBody CreateProductRestModel model) {
		CreateProductCommand createCommand = CreateProductCommand.builder()
				.price(model.getPrice())
				.quantity(model.getQuantity())
				.title(model.getTitle())
				.productId(UUID.randomUUID().toString()).build();
		/* try { */
		return commandGateway.sendAndWait(createCommand);
		/*
		 * } catch(Exception e) { return e.getLocalizedMessage(); }
		 */
	}

	@GetMapping("/getProduct")
	public String getProduct() {
		return "HTTP GET handled" + env.getProperty("local.server.port");
	}

}
