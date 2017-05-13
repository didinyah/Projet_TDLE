package wikipediapckg;

/* 
 *
 * Parse 
 * <lang>wiki-<date>-page.sql.gz
 * <lang>wiki-<date>-page-links.sql.gz
 * to produce simplified files with page ids and links
 * 
 * Extracted from the original code :
 *
 *
 * Copyright (c) 2016 Project Nayuki
 * All rights reserved. Contact Nayuki for licensing.
 * https://www.nayuki.io/page/computing-wikipedias-internal-pageranks
 */

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


/* 
 * This program reads the .sql.gz files containing Wikipedia's page metadata and page links
 * (or reads the cache files), writes out cached versions of the parsed data (for faster processing
 * next time), iteratively computes the PageRank of every page, and writes out the raw PageRank vector.
 * 
 * Run the program on the command line with no arguments. You may need to modify the file names below.
 * The program prints a bunch of statistics and progress messages on standard output.
 */

public final class ParseWiki {
	
	/*---- Input/output files configuration ----*/
	
	private static final File PAGE_ID_TITLE_SQL_FILE = new File("wikifolder/frwiki-latest-page.sql.gz");           // Original input file
	private static final File PAGE_ID_TITLE_RAW_FILE = new File("wikifolder/wikipedia-page-id-title.raw");  // Cache after preprocessing
	
	private static final File PAGE_LINKS_SQL_FILE = new File("wikifolder/frwiki-latest-pagelinks.sql.gz");   // Original input file
	private static final File PAGE_LINKS_RAW_FILE = new File("wikifolder/wikipedia-page-links.raw");  // Cache after preprocessing
	
	private static final File INDEX_RAW_FILE = new File("wikifolder/wikipedia-linked.raw");  // Output file
	private static final File PAGERANK_RAW_FILE = new File("wikifolder/wikipedia-pageranks.raw");
	
	/*---- Main program ----*/
	
	public static void WikipediaSimpleStrategy(int nbIterations) throws IOException {
		// Read page-ID-title data
		Map<String,Integer> titleToId;
		if (!PAGE_ID_TITLE_RAW_FILE.isFile()) {  // Read SQL and write cache
			titleToId = PageIdTitleMap.readSqlFile(PAGE_ID_TITLE_SQL_FILE);
			PageIdTitleMap.writeRawFile(titleToId, PAGE_ID_TITLE_RAW_FILE);
		} else  // Read cache
			titleToId = PageIdTitleMap.readRawFile(PAGE_ID_TITLE_RAW_FILE);
		Map<Integer,String> idToTitle = PageIdTitleMap.computeReverseMap(titleToId);
		
		// Read page-links data
		int[] links;
		if (!PAGE_LINKS_RAW_FILE.isFile()) {  // Read SQL and write cache
			links = PageLinksList.readSqlFile(PAGE_LINKS_SQL_FILE, titleToId, idToTitle);
			PageLinksList.writeRawFile(links, PAGE_LINKS_RAW_FILE);
		} else  // Read cache
			links = PageLinksList.readRawFile(PAGE_LINKS_RAW_FILE);
		System.out.println("* Done indexing.");
		
		for(int i =0; i<links.length;i++) {
			// System.out.println(links[i]);
		}
		
		// Iteratively compute PageRank
		final double DAMPING = 0.85;  // Between 0.0 and 1.0; standard value is 0.85
		System.out.println("Computing PageRank...");
		Pagerank pr = new Pagerank(links);
		double[] prevPageranks = pr.pageranks.clone();
		for (int i = 0; i < nbIterations; i++) {
			// Do iteration
			System.out.print("Iteration " + i);
			long startTime = System.currentTimeMillis();
			pr.iterateOnce(DAMPING);
			System.out.printf(" (%.3f s)%n", (System.currentTimeMillis() - startTime) / 1000.0);
			
			// Calculate and print statistics
			double[] pageranks = pr.pageranks;
			printPagerankChangeRatios(prevPageranks, pageranks);
			printTopPages(pageranks, idToTitle);
			prevPageranks = pageranks.clone();
		}
		
		// Write PageRanks to file
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(PAGERANK_RAW_FILE)));
		try {
			for (double x : pr.pageranks)
				out.writeDouble(x);
		} 
		finally {
			out.close();
		}
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
		
}
