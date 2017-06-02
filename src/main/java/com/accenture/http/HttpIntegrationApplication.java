package com.accenture.http;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageSources;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.store.MessageStore;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class HttpIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpIntegrationApplication.class, args);
	}

	
	@Bean
	public SimpleMessageStore simpleMessageStore(){
		return new SimpleMessageStore();
	}
	
@Bean
public MessageChannel requestChannel(){
	return new DirectChannel();
}

@Bean
public MessageChannel replyChannel(){
	return new DirectChannel();
}

@Bean
public MessageChannel inputChannel(){
	return new DirectChannel();
}

@Bean
public IntegrationFlow inboundFlow() {
	System.out.println(requestChannel());
	return IntegrationFlows.from(Http.inboundGateway("/test")
					.requestMapping( r -> r.methods(HttpMethod.GET,HttpMethod.POST))
					.replyChannel(replyChannel())
					)
					.channel(requestChannel())					
					.handle("requestHandler","handleRequest")
					.claimCheckIn(simpleMessageStore())
					.handle("requestHandler","afterRequest")
					.get();
}

@Bean
public IntegrationFlow outboundFlow(){
	return IntegrationFlows.from(inputChannel())
		.handle(Http.outboundGateway("some/test"))
		.handle("requestHandler","handleRequest")
		.channel(replyChannel())
		.get();
}
}