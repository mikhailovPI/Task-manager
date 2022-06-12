package manager.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String url;
    String api_token;
    private HttpClient client;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        this.api_token = registerNewKey();
    }

    public void put(String key, String json) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + api_token))
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения put запроса возникла ошибка.");
        }
    }

    public String load(String key) {
        String tasks = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + api_token))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                tasks = jsonElement.toString();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения load запроса возникла ошибка.");
        }
        return tasks;
    }

    public String registerNewKey() {
        String key = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                key = jsonElement.getAsString();
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("KVServer не получил api_token");
        }
        return key;
    }
}