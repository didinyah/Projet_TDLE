package wikipediapckg.WriterReader;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class RawFileIO {
	
	/*---- Input/output files configuration ----*/
	
	private static final File PAGE_ID_TITLE_SQL_FILE = new File("wikifolder/frwiki-latest-page.sql.gz");           // Original input file
	private static final File PAGE_ID_TITLE_RAW_FILE = new File("wikifolder/wikipedia-page-id-title.raw");  // Cache after preprocessing
	
	private static final File PAGE_LINKS_SQL_FILE = new File("wikifolder/frwiki-latest-pagelinks.sql.gz");   // Original input file
	private static final File PAGE_LINKS_RAW_FILE = new File("wikifolder/wikipedia-page-links.raw");  // Cache after preprocessing
	
	//private static final File INDEX_RAW_FILE = new File("wikifolder/wikipedia-linked.raw");  // Output file
	private static final File PAGERANK_RAW_FILE = new File("wikifolder/wikipedia-pageranks.raw");
	
	private static Map<String,Integer> titleToId;
	private static int[] links;
	private static Map<Integer,String> idToTitle;
	
	
	
	public  void searchFile() throws IOException {
		if (!PAGE_ID_TITLE_RAW_FILE.isFile()) {  // Read SQL and write cache
			titleToId = PageIdTitleMap.readSqlFile(PAGE_ID_TITLE_SQL_FILE);
			PageIdTitleMap.writeRawFile(titleToId, PAGE_ID_TITLE_RAW_FILE);
		} else  // Read cache
			titleToId = PageIdTitleMap.readRawFile(PAGE_ID_TITLE_RAW_FILE);
		idToTitle = PageIdTitleMap.computeReverseMap(titleToId);
		
		if (!PAGE_LINKS_RAW_FILE.isFile()) {  // Read SQL and write cache
			links = PageLinksList.readSqlFile(PAGE_LINKS_SQL_FILE, titleToId, idToTitle);
			PageLinksList.writeRawFile(links, PAGE_LINKS_RAW_FILE);
		} else  // Read cache
			links = PageLinksList.readRawFile(PAGE_LINKS_RAW_FILE);
		System.out.println("* Done indexing.");
		
		System.out.println(links.length);
	}
	
	public  void writePageRanksToRaw(double[] pageranks) throws FileNotFoundException, IOException {
		// Write PageRanks to file
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(PAGERANK_RAW_FILE)));
		try {
			for (double x : pageranks)
				out.writeDouble(x);
		} 
		finally {
			out.close();
		}
	}
	



	public  Map<String, Integer> getTitleToId() {
		return titleToId;
	}



	public  int[] getLinks() {
		return links;
	}



	public  Map<Integer, String> getIdToTitle() {
		return idToTitle;
	}
	
	

}
