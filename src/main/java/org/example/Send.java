package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

    private final static String QUEUE_NAME = "hello";
    private final static String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        //connection abstracts the socket connection and takes care of protocol version negotiation.
        //channel allows the connection to a node, localhost for local development and use IP/Hostname to access a node
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //To send, we must first declare a queue for us to send to, and then we can publish a message.
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            //create a message from the programs ARGS
            String message = String.join(" ", args);


            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }
    }

    //Fake workload
    static void fakeWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
