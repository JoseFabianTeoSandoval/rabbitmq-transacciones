package producer.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import producer.model.Transaction;

public class ApiClient {
	
	public List<Transaction> obtenerTransacciones() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
"https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        String json = response.body();

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(json);

        JsonNode transacciones = root.get("transacciones");

        List<Transaction> lista = new ArrayList<>();

        for(JsonNode node : transacciones){

            Transaction tx =
                    mapper.treeToValue(node, Transaction.class);

            lista.add(tx);

        }

        return lista;
    }

}
