package consumer.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import consumer.model.Transaction;

public class ApiSender {
	
	private ObjectMapper mapper = new ObjectMapper();

    public boolean enviar(Transaction tx){

        try{

            HttpClient client = HttpClient.newHttpClient();

            String json = mapper.writeValueAsString(tx);

            System.out.println("JSON enviado:");
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()

            .uri(URI.create(
"https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones"
            ))

            .header("Content-Type","application/json")

            .POST(HttpRequest.BodyPublishers.ofString(json))

            .build();

            HttpResponse<String> response =
                client.send(request,
                HttpResponse.BodyHandlers.ofString());

            System.out.println("POST status: "+response.statusCode());
            System.out.println("Respuesta API: "+response.body());

            return response.statusCode() == 200 || response.statusCode() == 201;

        }catch(Exception e){

            System.out.println("Error enviando al POST");
            e.printStackTrace();

            return false;

        }
    }

}
