package com.explorer.filme.api.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/topfilmes")
public class TopFilmesController {

    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    @GetMapping
    public void listar() throws IOException {

        Request request = new Request.Builder()
                .url("https://imdb-api.com/en/API/Top250Movies/k_l24l9cm1")
                .method("GET", null)
                .build();

        Response response;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.body().string());
    }
}
