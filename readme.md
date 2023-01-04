Start docker container with RabbitMQ:

docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management

User: guest
Password: guest

http://localhost:15672/

###

Round-Robin

By default, RabbitMQ will send each message to the next consumer, in sequence.
On average every consumer will get the same number of messages. This way of distributing
messages is called ROUND-ROBIN.