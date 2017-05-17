package wikipediapckg.pageRank.arraysRanker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import main.Fenetre;
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
	
	public void createPageRank(final int nbIterations,final double damping) throws IOException {
		
		RawFileIO rfr = new RawFileIO();
		
		rfr.searchFile();
		
		// On affiche maintenant les pages et les liens qu'elles r�f�rent
		
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
		
		System.out.println("Fin des it�rations pour le pagerank simple");
		
		// On ouvre la fen�tre pour consulter les r�sultats
		//HashMap<Integer, Integer> nbLinksPages = evaluateNbLinks(links);
		
		int[][] allLinksSplitted = splitAllLinks(rfr.getLinks(), rfr.getTitleToId().size());
		//printSomePagesLinksSplitted(allLinksSplitted, rfr.getIdToTitle());
				
		Fenetre fenetre = new Fenetre(allLinksSplitted, pageranks, rfr.getIdToTitle());
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
	
	private static HashMap<Integer, ArrayList<Integer>> evaluateLinks(int[] links) {
		// links[0] = id de la page
		// links[1] = nb de liens de la page d'id links[0]
		// links[2..2+links[1]-1] = tous les ids des liens que r�f�rent la page d'id links[0]
		HashMap<Integer, ArrayList<Integer>> res = new HashMap<Integer, ArrayList<Integer>>();
		int i = 0;
		ArrayList<Integer> allLiensPageActu;
		while(i<links.length) {
			int idPageActu = links[i];
			i++;
			int nbLiensPageActu = links[i];
			allLiensPageActu = new ArrayList<Integer>();
			// si la page n'a pas de liens, on envoie une arraylist vide
			if(nbLiensPageActu>0) {
				i++;
				for(int j=0; j<nbLiensPageActu; j++) {
					allLiensPageActu.add(links[i]);
					i++;
				}
			}
			res.put(idPageActu, allLiensPageActu);
			System.out.println(i);
		}
		
		return res;
	}
	
	private static HashMap<Integer, Integer> evaluateNbLinks(int[] links) {
		// links[0] = id de la page
		// links[1] = nb de liens de la page d'id links[0]
		// links[2..2+links[1]-1] = tous les ids des liens que r�f�rent la page d'id links[0]
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
	
	// va renvoyer les ids des liens s�par�s avec le 1er �l�ment de chaque tableau l'id de la page de base
	private static int[][] splitAllLinks(int[] links, int nbPages) {
		int[][] res = new int[nbPages][];
		int[] liensPageActu;
		int i=0;
		int pageActu=0;
		System.out.println("S�paration des liens");
		while(i<links.length) {
			int idPageActu = links[i];
			i++;
			int nbLiensPageActu = links[i];
			liensPageActu = new int[nbLiensPageActu+2];
			liensPageActu[0] = idPageActu;
			liensPageActu[1] = nbLiensPageActu;
			// si la page a des liens, on les ajoute tous � l'entier
			if(nbLiensPageActu>0) {
				i++;
				for(int j=0; j<nbLiensPageActu; j++) {
					liensPageActu[j+2] = links[i];
					i++;
				}
			}
			res[pageActu] = liensPageActu;
			pageActu++;
		}
		System.out.println("Fin de s�paration des liens");
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