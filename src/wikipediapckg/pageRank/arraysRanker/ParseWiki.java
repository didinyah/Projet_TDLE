package wikipediapckg.pageRank.arraysRanker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.get.GetResponse;

import main.Fenetre;
import main.ResultDTO;
import wikipediapckg.ElasticSearch.ElasticSearchImplementation;
import wikipediapckg.ElasticSearch.JsonWriter;
import wikipediapckg.WriterReader.RawFileIO;
import wikipediapckg.pageRank.IPageRanker;


/* 
 * This program reads the .sql.gz files containing Wikipedia's page metadata and page links
 * (or reads the cache files), writes out cached versions of the parsed data (for faster processing
 * next time), iteratively computes the PageRank of every page, and writes out the raw PageRank vector.
 * 
 * Run the program on the command line with no arguments. You may need to modify the file names below.
 * The program prints a bunch of statistics and progress messages on standard output.
 */

public final class ParseWiki implements IPageRanker{
	

	/*---- Main program ----*/
	
	public ResultDTO createPageRank(final int nbIterations,final double damping) throws IOException {
		
		RawFileIO rfr = new RawFileIO();
		
		rfr.searchFile();
		
		// On affiche maintenant les pages et les liens qu'elles rï¿½fï¿½rent
		
		//HashMap<Integer, ArrayList<Integer>> pagesAndLinks = evaluateLinks(links);
		//printSomePages(pagesAndLinks, idToTitle);
		
		
		
		
		// Iteratively compute PageRank
		//final double DAMPING = 0.85;  // Between 0.0 and 1.0; standard value is 0.85
		System.out.println("Computing PageRank...");
		ArrayPageRank pr = new ArrayPageRank(rfr.getLinks());
//		double[] prevPageranks = pr.pageranks.clone();
		for (int i = 0; i < nbIterations; i++) {
			// Do iteration
			System.out.print("Iteration " + i);
			long startTime = System.currentTimeMillis();
			pr.iterateOnce(damping);
			System.out.printf(" (%.3f s)%n", (System.currentTimeMillis() - startTime) / 1000.0);
			
			// Calculate and print statistics
			double[] pageranks = pr.pageranks;
			//printPagerankChangeRatios(prevPageranks, pageranks);
			printTopPages(pageranks, rfr.getIdToTitle());
//			prevPageranks = pageranks.clone();
		}
		
		double[] pageranks = pr.pageranks;
		rfr.writePageRanksToRaw(pageranks);
		
		System.out.println("Fin des itérations pour le pagerank simple");
		
		// On ouvre la fenêtre pour consulter les résultats
		//HashMap<Integer, Integer> nbLinksPages = evaluateNbLinks(links);
		
		//int[][] allLinksSplitted = splitAllLinks(rfr.getLinks(), rfr.getTitleToId().size());
		//printSomePagesLinksSplitted(allLinksSplitted, rfr.getIdToTitle());
				
		//Fenetre fenetre = new Fenetre(pr, rfr.getIdToTitle());
		
		Map<Integer, ArrayList<String>> allLinks = evaluateLinks(rfr.getLinks(),rfr.getIdToTitle());
		
		ResultDTO rdto = new ResultDTO(pr.idLimit, pr.pageranks, pr.nbLinksPage, rfr.getIdToTitle(), allLinks);
		
		//JsonWriter.createJson(pr);
		
		//ElasticSearchImplementation esi = new ElasticSearchImplementation();
		//GetResponse resp = esi.getResponseRequest(1);
		//System.out.println(resp.toString());
		
		return rdto;
	}




	/*---- Miscellaneous functions ----*/
	
	private static void printPagerankChangeRatios(double[] prevPr, double[] pr) {
		double min = Double.POSITIVE_INFINITY;
		double max = 0;
		for (int i = 0; i < pr.length; i++) {
			if (pr[i] != 0 && prevPr[i] != 0) {
				double ratio = pr[i] / prevPr[i];
				min = Math.min(ratio, min);
				max = Math.max(ratio, max);
			}
		}
		System.out.println("Range of ratio of changes: " + min + " to " + max);
	}
	
	
	private static void printTopPages(double[] pageranks, Map<Integer,String> titleById) {
		final int NUM_PAGES = 30;
		double[] sorted = pageranks.clone();
		Arrays.sort(sorted);
		for (int i = 0; i < NUM_PAGES; i++) {
			for (int j = 0; j < sorted.length; j++) {
				if (pageranks[j] == sorted[sorted.length - 1 - i]) {
					System.out.printf("  %.3f  %s%n", Math.log10(pageranks[j]), titleById.get(j));
					break;
				}
			}
		}
	}
	
	private static void writeTopPages(double[] pageranks, Map<Integer,String> titleById) {
		final int NUM_PAGES = 50;
		double[] sorted = pageranks.clone();
		Arrays.sort(sorted);
		File output = new File("wikifolder/top50.txt");
		if(output.exists())
		{
			output.delete();
		}
		try {
			if(!output.createNewFile())
			{
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(output);
			for (int i = 0; i < NUM_PAGES; i++) {
				for (int j = 0; j < sorted.length; j++) {
					if (pageranks[j] == sorted[sorted.length - 1 - i]) {
						System.out.printf("  %.3f  %s%n", Math.log10(pageranks[j]), titleById.get(j));
						writer.printf("  %f  %.3f  %s%n", pageranks[j], Math.log10(pageranks[j]), titleById.get(j));
						break;
					}
				}
			}
			writer.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//int[] links = rfr.getLinks();

		
	}
	
	private static Map<Integer, ArrayList<String>> evaluateLinks(int[] links, Map<Integer, String> idToTitle) {
		// links[0] = id de la page
		// links[1] = nb de liens de la page d'id links[0]
		// links[2..2+links[1]-1] = tous les ids des liens que rï¿½fï¿½rent la page d'id links[0]
		Map<Integer, ArrayList<String>> res = new HashMap<Integer, ArrayList<String>>();
		int i = 0;
		ArrayList<String> allLiensPageActu;
		while(i<links.length) {
			int idPageActu = links[i];
			int nbLiensPageActu = links[i+1];
			allLiensPageActu = new ArrayList<String>();
			// si la page n'a pas de liens, on envoie une arraylist vide
			for(int j=0; j<nbLiensPageActu; j++) {
				allLiensPageActu.add(idToTitle.get(links[i+2+j]));
			}
			res.put(idPageActu, allLiensPageActu);
			
			System.out.println("mise des liens dans le hashmap : " +i);
			
			i+= nbLiensPageActu+2;
		}
		
		return res;
	}
	
	private static HashMap<Integer, Integer> evaluateNbLinks(int[] links) {
		// links[0] = id de la page
		// links[1] = nb de liens de la page d'id links[0]
		// links[2..2+links[1]-1] = tous les ids des liens que rï¿½fï¿½rent la page d'id links[0]
		HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
		int i = 0;
		while(i<links.length) {
			int idPageActu = links[i];
			i++;
			int nbLiensPageActu = links[i];
			i+=nbLiensPageActu+1;
			System.out.println(i);
			res.put(idPageActu, nbLiensPageActu);
		}
		
		return res;
	}
	
	private static void printSomePages(HashMap<Integer, ArrayList<Integer>> pagesAndLinks, Map<Integer, String> idToTitle) {
		final int NUM_PAGES = 10;
		Iterator<Entry<Integer, ArrayList<Integer>>> it = pagesAndLinks.entrySet().iterator();
		int i =0;
	    while (it.hasNext() && i<NUM_PAGES) {
	        Entry<Integer, ArrayList<Integer>> pair = it.next();
	        int nbLiensPageActu = pair.getValue().size();
			System.out.println(idToTitle.get(pair.getKey()) + " : " + nbLiensPageActu);
			for(int j =0; j< nbLiensPageActu; j++) {
				System.out.print(idToTitle.get(pair.getValue().get(j)) + ", ");
			}
			System.out.println("");
	        it.remove(); // avoids a ConcurrentModificationException
	        i++;
	    }
	}
	
	// va renvoyer les ids des liens sï¿½parï¿½s avec le 1er ï¿½lï¿½ment de chaque tableau l'id de la page de base
	private static int[][] splitAllLinks(int[] links, int nbPages) {
		int[][] res = new int[nbPages][];
		int[] liensPageActu;
		int i=0;
		int pageActu=0;
		System.out.println("Séparation des liens");
		while(i<links.length) {
			int idPageActu = links[i];
			int nbLiensPageActu = links[i+1];
			liensPageActu = new int[nbLiensPageActu+2];
			liensPageActu[0] = idPageActu;
			liensPageActu[1] = nbLiensPageActu;
			// si la page a des liens, on les ajoute tous ï¿½ l'entier
			if(nbLiensPageActu>0) {
				for(int j=0; j<nbLiensPageActu; j++) {
					liensPageActu[j+2] = links[i+2+j];
				}
			}
			res[pageActu] = liensPageActu;
			
			i += nbLiensPageActu + 2;
			pageActu++;
		}
		System.out.println("Fin de séparation des liens");
		return res;
	}
	
	private static void printSomePagesLinksSplitted(int[][] allLinksSplitted, Map<Integer, String> idToTitle) {
		final int NUM_PAGES = 15;
	    for (int i=0; i<NUM_PAGES; i++) {
	    	String titrePage = idToTitle.get(allLinksSplitted[i][0]);
	    	int nbLiensPage = allLinksSplitted[i][1];
			System.out.println(titrePage + " : " + nbLiensPage);
			for(int j =0; j< nbLiensPage; j++) {
				System.out.print(idToTitle.get(allLinksSplitted[i][j+2]) + ", ");
			}
			System.out.println("");
	    }
	}
		
}
