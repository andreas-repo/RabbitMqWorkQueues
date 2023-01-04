package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

import static org.example.Send.fakeWork;

public class Receive {

    private static final String QUEUE_NAME = "hello";
    private final static String HOST = "localhost";

    public static void main(String[] arg) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //note that we declare the queue here as well, to be sure the queue exists even if Receive is the first to start up.
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        //Because we want to receive all the time, we wont use a try-catch-clause because it would close and reopen instead of receiving without pause.
        System.out.println("[*] Waiting for messages. To exit press Ctrl + c.");

        //Since the queue will deliver us messages asynchronously, we provide a callback in the form of a object that will buffer the messages until we're ready to use them.
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[x] Received '" + message + "'");

            try {
                fakeWork(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(" [x] Done");
            }
        };

        boolean autoAck = true; //acknowledgment

        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
    }
}
