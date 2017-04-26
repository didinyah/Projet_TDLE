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
	
	private static final File PAGE_ID_TITLE_SQL_FILE = new File("frwiki-latest-page.sql.gz");           // Original input file
	private static final File PAGE_ID_TITLE_RAW_FILE = new File("wikipedia-page-id-title.raw");  // Cache after preprocessing
	
	private static final File PAGE_LINKS_SQL_FILE = new File("frwiki-latest-pagelinks.sql.gz");   // Original input file
	private static final File PAGE_LINKS_RAW_FILE = new File("wikipedia-page-links.raw");  // Cache after preprocessing
	
	private static final File INDEX_RAW_FILE = new File("wikipedia-linked.raw");  // Output file
	
	
	/*---- Main program ----*/
	
	public static void main(String[] args) throws IOException {
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
	}
		
}
