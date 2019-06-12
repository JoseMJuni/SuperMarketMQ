package com.javafx.RabbitMQ.entities;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Productor {
	private static final String EXCHANGE_NAME = "Exchange_SuperMercado";
	private Connection connection;
	private Channel channel;
	private ConnectionFactory factory = new ConnectionFactory();

	private String[] mensaje;

	public Productor(String[] mensaje) {
		factory.setHost("IP");
		factory.setUsername("master");
		factory.setPassword("master");
		factory.setPort(5672);
		this.mensaje = mensaje;

	}

	public void enviar() {
		Thread publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
				try {
					connection = factory.newConnection();
					channel = connection.createChannel();
					channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
					String routingKey = getRouting(mensaje);
					String message = getMessage(mensaje);
					channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
					System.out.println("Enviado: "+routingKey+" "+message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			// System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
		});
		publishThread.start();
	}

	private static String getRouting(String[] strings) {
		if (strings.length < 1)
			return "anonymous.info";
		return strings[0];
	}

	private static String getMessage(String[] strings) {
		// if (strings.length < 2)
		// return "Hello World!";
		return joinStrings(strings, " ", 1);
	}

	private static String joinStrings(String[] strings, String delimiter, int startIndex) {
		int length = strings.length;
		if (length == 0)
			return "";
		if (length < startIndex)
			return "";
		StringBuilder words = new StringBuilder(strings[startIndex]);
		for (int i = startIndex + 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

}
