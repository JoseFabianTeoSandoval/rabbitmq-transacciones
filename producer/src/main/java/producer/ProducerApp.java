package producer;

import java.util.List;

import producer.api.ApiClient;
import producer.model.Transaction;
import producer.rabbit.RabbitProducer;

public class ProducerApp {
	
	public static void main(String[] args) throws Exception {

		try{

            ApiClient apiClient = new ApiClient();
            RabbitProducer producer = new RabbitProducer();

            // obtener transacciones desde la API
            List<Transaction> lista = apiClient.obtenerTransacciones();

            System.out.println("Total transacciones recibidas: " + lista.size());

            // enviar cada transaccion a RabbitMQ
            for(Transaction tx : lista){

                producer.enviarTransaccion(tx);

            }

            System.out.println("Transacciones enviadas a RabbitMQ");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
