package consumer;

import consumer.rabbit.RabbitConsumer;

public class ConsumerApp {
	
	public static void main(String[] args) throws Exception {

        RabbitConsumer consumer = new RabbitConsumer();
        consumer.start();

    }

}
