package consumer.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import consumer.api.ApiSender;
import consumer.model.Transaction;

public class RabbitConsumer {
	
	private ObjectMapper mapper = new ObjectMapper();
    private ApiSender apiSender = new ApiSender();

    public void start() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        String[] queues = {"BANRURAL","BAC","GYT","BI"};

        for(String queue : queues){

            Channel channel = connection.createChannel();

            channel.basicQos(1);

            channel.queueDeclare(queue,true,false,false,null);

            System.out.println("Escuchando cola: "+queue);

            channel.basicConsume(queue,false,(tag,msg)->{

                long deliveryTag = msg.getEnvelope().getDeliveryTag();

                try{

                    String json = new String(msg.getBody());

                    Transaction tx =
                            mapper.readValue(json, Transaction.class);

                    boolean ok = apiSender.enviar(tx);

                    if(ok){

                        channel.basicAck(deliveryTag,false);

                        System.out.println(
                        "ACK enviado: "+tx.getIdTransaccion()
                        );

                    }else{

                        channel.basicNack(
                                deliveryTag,
                                false,
                                true
                        );

                    }

                }catch(Exception e){

                    channel.basicNack(
                            deliveryTag,
                            false,
                            true
                    );

                    e.printStackTrace();

                }

            },tag->{});

        }

    }
}