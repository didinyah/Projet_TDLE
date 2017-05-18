package main;

import java.io.IOException;

import wikipediapckg.pageRank.IPageRanker;
import wikipediapckg.pageRank.arraysRanker.ParseWiki;
import wikipediapckg.pageRank.hadoop.MapReduceRanker;

public class Main {
	public static void main(String[] args) {
		// On indique le nombre d'iterations a faire pour le pagerank, le damping et le nbmax de résultats affiches
		int nbIterations = 20;
		double damping = 0.85;
		int nbMaxResultsAffiches = 40;
		
		// Choix de la stratï¿½gie (avec ou sans mapreduce)
		
		// Strategie simple : pas de map reduce
		IPageRanker ranker = new ParseWiki();
		
		// Strategie map reduce
		//IPageRanker ranker = new MapReduceRanker();
		
		try {
			ResultDTO rdto = ranker.createPageRank(nbIterations,damping);
			Fenetre fen = new Fenetre(rdto, nbMaxResultsAffiches);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
}