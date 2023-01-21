package com.filmes.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.filmes.api.controller.ApiClient;
import com.filmes.api.filmes.DadosListagemFilmes;
import com.filmes.api.filmes.FilmeJsonParser;
import com.filmes.api.view.HtmlGenerator;

public class TopFilmes {
	
	private static String apiKey = "k_l24l9cm1";

	public static void main(String[] args) throws IOException, InterruptedException {
		
        System.out.println("Chamada da API");
		String json = new ApiClient(apiKey).getBody();

        System.out.println("Parsing JSON");
		List<DadosListagemFilmes> filmes = new FilmeJsonParser(json).parse();

		System.out.println("Gerando HTML");
		PrintWriter writer = new PrintWriter("content.html");
		new HtmlGenerator(writer).generate(filmes);
		writer.close();
	}

}
