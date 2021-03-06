package com.java.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Recv {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		boolean autoAck = false;
	    channel.basicConsume(QUEUE_NAME, autoAck, consumer);

	    while (true) {
	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	      String message = new String(delivery.getBody());
	      System.out.println("delivery.getEnvelope().getDeliveryTag() : " + delivery.getEnvelope().getDeliveryTag());
	      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	      System.out.println(" [x] Received '" + message + "'");
	    }
	}
}
