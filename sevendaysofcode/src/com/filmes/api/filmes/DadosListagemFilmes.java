package com.filmes.api.filmes;

public record DadosListagemFilmes(
        String title,
        String image,
        String imDbRating,
        String year
) {}