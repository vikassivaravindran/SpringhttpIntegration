package com.accenture.http;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.store.MessageGroup;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class RequestHandler implements MessageHandler{
	
	@Autowired
	private MessageChannel requestChannel;
	
	@Autowired
	private SimpleMessageStore simpleMessageStore;
	
	
	
	public Message<?> handleRequest(Message<?> message){
		System.out.println("INPUT"+message);
		System.out.println("MESSAGE COUNT "+simpleMessageStore.getMessageCount());
		//System.out.println(simpleMessageStore.addMessage(message));
		return MessageBuilder.withPayload("Hello World").build();
	}
	@Override
	public void handleMessage(Message<?> arg0) throws MessagingException {
		// TODO Auto-generated method stub
	}
	
	public Message<?> afterRequest(Message<?> message){
		System.out.println("The Message that enters is :::"+message);
		Message<?> mess= simpleMessageStore.getMessage((UUID) message.getPayload());
		System.out.println("The message is:"+mess);
		//simpleMessageStore.iterator();
		System.out.println("After calling the claim checkin:::"+mess.getHeaders().getId().compareTo(message.getHeaders().getId()));
		return mess;
		
	}

}
