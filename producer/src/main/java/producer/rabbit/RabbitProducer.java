package producer.rabbit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import producer.model.Transaction;

public class RabbitProducer {
	
	private Channel channel;
    private ObjectMapper mapper = new ObjectMapper();

    public RabbitProducer() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        channel = connection.createChannel();

    }

    public void enviarTransaccion(Transaction tx){

    	try{

            // AGREGAR IDENTIFICADOR DEL ESTUDIANTE AL ID
            tx.setIdTransaccion(
                    tx.getIdTransaccion() + "-JOSETEO"
            );

            String queue = tx.getBancoDestino();

            // crear cola si no existe
            channel.queueDeclare(queue,true,false,false,null);

            // convertir a JSON
            String json = mapper.writeValueAsString(tx);

            // enviar mensaje
            channel.basicPublish(
                    "",
                    queue,
                    null,
                    json.getBytes()
            );

            System.out.println(
                    "Enviado a cola: " + queue +
                    " | TX: " + tx.getIdTransaccion()
            );

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
