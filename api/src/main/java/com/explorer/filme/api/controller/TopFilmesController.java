package com.explorer.filme.api.controller;

import com.explorer.filme.api.filmes.DadosListagemFilmes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/topfilmes")
public class TopFilmesController {

    //OkHttpClient client = new OkHttpClient()
    //        .newBuilder()
    //        .build();

    HttpClient client = HttpClient.newHttpClient();

    String apiKey = "k_l24l9cm1";

    @GetMapping
    public List<DadosListagemFilmes> listar() throws IOException, InterruptedException {

        //Request request = new Request.Builder()
        //        .url("https://imdb-api.com/en/API/Top250Movies/" + apiKey)
        //        .method("GET", null)
        //        .build();
        URI apiIMDB = URI.create("https://imdb-api.com/en/API/Top250Movies/" + apiKey);

        HttpRequest request = HttpRequest.newBuilder().uri(apiIMDB).build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        List<DadosListagemFilmes> filmes = parse(json);

        return filmes;
    }

    private List<DadosListagemFilmes> parse(String json) {

        String[] filmesLista = parseJsonFilmes(json);

        List<String> titles = parseTitles(filmesLista);
        List<String> urlImages = parseUrlImages(filmesLista);
        List<String> ratings = parseRatings(filmesLista);
        List<String> years = parseYears(filmesLista);

        List<DadosListagemFilmes> filmes = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++) {
            filmes.add(
                    new DadosListagemFilmes(
                            titles.get(i),
                            urlImages.get(i),
                            ratings.get(i),
                            years.get(i)
                    )
            );
        }

        return filmes;
    }

    private List<String> parseYears(String[] filmesLista) {
        return parseAtributo(filmesLista, 4);
    }

    private List<String> parseRatings(String[] filmesLista) {
        return parseAtributo(filmesLista, 7);
    }

    private List<String> parseUrlImages(String[] filmesLista) {
        return parseAtributo(filmesLista, 5);
    }

    private List<String> parseTitles(String[] filmesLista) {
        return parseAtributo(filmesLista, 2);
    }

    private List<String> parseAtributo(String[] filmesLista, int posicao) {

        return Stream
                .of(filmesLista)
                .map(e -> e.split("\",\"")[posicao])
                .map(e -> e.split(":\"")[1])
                .map(e -> e.replaceAll("\"",""))
                .collect(Collectors.toList());
    }

    private String[] parseJsonFilmes(String json) {

        Matcher matcher = Pattern
                .compile(".*\\[(.*)\\].*")
                .matcher(json);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nenhum dado encontrado em " + json);
        }

        String[] filmesLista = matcher
                .group(1)
                .split("\\},\\{");

        filmesLista[0] = filmesLista[0].substring(1);
        int ultimo = filmesLista.length - 1;
        String ultimaString = filmesLista[ultimo];
        filmesLista[ultimo] = ultimaString.substring(0, ultimaString.length() - 1);

        return filmesLista;
    }
}
