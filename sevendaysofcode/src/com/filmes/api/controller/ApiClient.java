package com.filmes.api.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private String uri = "https://imdb-api.com/en/API/Top250Movies/";

    private String apiKey;

    public ApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBody(){

        URI apiIMDB = URI.create(uri + apiKey);

        HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(apiIMDB).build();

		try {
            HttpResponse<String> response = client
            		.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
