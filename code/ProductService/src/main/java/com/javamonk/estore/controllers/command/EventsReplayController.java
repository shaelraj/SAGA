/**
 * 
 */
package com.javamonk.estore.controllers.command;

import java.util.Optional;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaelraj
 *
 */
@RestController
@RequestMapping("/api/v1/management")
public class EventsReplayController {
	
	@Autowired
	private EventProcessingConfiguration eventProcessingConfiguration;

	@PostMapping("/eventProcessor/{processorName}/reset")
	public ResponseEntity<String> replayEvents(@PathVariable String processorName) {
		
		Optional<TrackingEventProcessor> trackingEventProcessor = 
				eventProcessingConfiguration.eventProcessor(processorName, TrackingEventProcessor.class);
		
		if(trackingEventProcessor.isPresent()) {
			TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
			eventProcessor.shutDown();
			eventProcessor.resetTokens();
			eventProcessor.start(); 
			
			return ResponseEntity.ok()
					.body(String.format("The event processor with a name [%s] has been reset",
					processorName));
		} else {
			return ResponseEntity.badRequest()
					.body(String.format("The event processor with a name [%s] is not a tracking event processor."
							+ " Only Tracking event processor is supported",processorName));
		}
	}
	
}
