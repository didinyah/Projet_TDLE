package main;

import java.io.IOException;

import wikipediapckg.ParseWiki;

public class Main {
	public static void main(String[] args) {
		// On indique le nombre d'itérations à faire pour le pagerank
		int nbIterations = 100;
		// choix de la stratégie (avec ou sans mapreduce)
		
		// Stratégie simple : pas de map reduce
		try {
			ParseWiki.WikipediaSimpleStrategy(nbIterations);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Stratégie map reduce
		
	}
}