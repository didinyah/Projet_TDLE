package main;

import java.io.IOException;

import wikipediapckg.ParseWiki;

public class Main {
	public static void main(String[] args) {
		// On indique le nombre d'it�rations � faire pour le pagerank
		int nbIterations = 100;
		// choix de la strat�gie (avec ou sans mapreduce)
		
		// Strat�gie simple : pas de map reduce
		try {
			ParseWiki.WikipediaSimpleStrategy(nbIterations);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Strat�gie map reduce
		
	}
}