package com.filmes.api;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.filmes.api.filmes.DadosListagemFilmes;
import com.filmes.api.view.HtmlGenerator;

public class TopFilmes {
	
	static HttpClient client = HttpClient.newHttpClient();
	
	static String apiKey = "k_l24l9cm1"; 

	public static void main(String[] args) throws IOException, InterruptedException {
		
		URI apiIMDB = URI.create("https://imdb-api.com/en/API/Top250Movies/" + apiKey);

		HttpRequest request = HttpRequest.newBuilder().uri(apiIMDB).build();

		HttpResponse<String> response = client
				.send(request, HttpResponse.BodyHandlers.ofString());

		String json = response.body();

		List<DadosListagemFilmes> filmes = parse(json);

		//System.out.println(filmes);

		PrintWriter writer = new PrintWriter("content.html");
		new HtmlGenerator(writer).generate(filmes);
		writer.close();
	}

	private static List<DadosListagemFilmes> parse(String json) {

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

    private static List<String> parseYears(String[] filmesLista) {
        return parseAtributo(filmesLista, 4);
    }

    private static List<String> parseRatings(String[] filmesLista) {
        return parseAtributo(filmesLista, 7);
    }

    private static List<String> parseUrlImages(String[] filmesLista) {
        return parseAtributo(filmesLista, 5);
    }

    private static List<String> parseTitles(String[] filmesLista) {
        return parseAtributo(filmesLista, 2);
    }

    private static List<String> parseAtributo(String[] filmesLista, int posicao) {

        return Stream
                .of(filmesLista)
                .map(e -> e.split("\",\"")[posicao])
                .map(e -> e.split(":\"")[1])
                .map(e -> e.replaceAll("\"",""))
                .collect(Collectors.toList());
    }

    private static String[] parseJsonFilmes(String json) {

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
