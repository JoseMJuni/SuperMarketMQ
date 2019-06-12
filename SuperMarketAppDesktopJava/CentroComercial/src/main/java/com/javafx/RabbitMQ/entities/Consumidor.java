package com.javafx.RabbitMQ.entities;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Consumidor extends Thread  {
	Controlador controlador = Controlador.getControlador();
	private static final String EXCHANGE_NAME = "Exchange_SuperMercado";
	private Connection connection;
	private Channel channel;
	private ConnectionFactory factory = new ConnectionFactory();
	private String[] canal = { "cliente.*.informacion.centro" }; //Escucho siempre a los clientes

	public Consumidor() {
        factory.setHost("IP");
		factory.setUsername("master");
		factory.setPassword("master");
		factory.setPort(5672);

	}

	public void run() {
		String queueName = null;
		try {
			
			connection = factory.newConnection();
			this.channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, canal[0]);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		//System.out.println(" [*] Waiting for clients. To exit press CTRL+C ");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				//System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

				decision(message);
//				if(!Controlador.getControlador().getClientes().isEmpty())
//					System.out.println(Controlador.getControlador().getClientes().getFirst().getNombre());
			}
		};
		try {
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void  decision(String message) {
		String[] splitMessage = message.split(" ");
		String intereses = "";
		System.out.println(splitMessage[0]); //tipo de mensaje que nos indicara que se quiere
		System.out.println(splitMessage[1]); //Id del cliente
		if(splitMessage.length > 2)
			intereses = splitMessage[2];
		switch (splitMessage[0]) {
			case "Hall": 		controlador.addCliente(splitMessage[1], splitMessage[0], intereses); break;//El cliente acaba de entrar
			case "disconnect":  controlador.removeCliente(splitMessage[1]); break; //El cliente se va
			case "intereses" :	controlador.setInteresCliente(splitMessage[1], intereses); break;
			default: 			controlador.setLocalizacion(splitMessage[1], splitMessage[0]);
		} 
			
	}

}
