package main;

import java.io.IOException;

import wikipediapckg.pageRank.IPageRanker;
import wikipediapckg.pageRank.arraysRanker.ParseWiki;

public class Main {
	public static void main(String[] args) {
		// On indique le nombre d'it�rations � faire pour le pagerank
		int nbIterations = 20;
		// choix de la strat�gie (avec ou sans mapreduce)
		
		// Strat�gie simple : pas de map reduce
		
		IPageRanker ranker = new ParseWiki();
		
		try {
			ranker.createPageRank(nbIterations);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Strat�gie map reduce
		
	}
}