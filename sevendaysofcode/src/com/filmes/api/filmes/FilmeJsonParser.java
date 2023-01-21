package com.filmes.api.filmes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilmeJsonParser {

    private String json;

    public FilmeJsonParser(String json) {
        this.json = json;
    }
    
    public List<DadosListagemFilmes> parse() {

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
