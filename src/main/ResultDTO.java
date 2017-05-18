package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultDTO {
	/** The vector of current PageRank values, changing after each iteration. Length equals idLimit.
	Other classes can read this data, but should not modify it.**/
	private double[] pageranks;
	
	
	/**Array that displays the number of incoming links for each page. Length equals idLimit.**/
	private int[] nbLinksPage;
	
	/**Maximum page ID value plus 1. This sets the length of various arrays.**/
	private int idLimit;
	
	private Map<Integer, String> idToTitle;
	
	private Map<Integer, ArrayList<String>> allLinks;
	
	private Map<Integer, Double> idToDouble;
	
	public ResultDTO(int idLimit, double[] pageranks, int[] nbLinksPage, Map<Integer, String> idToTitle, Map<Integer, ArrayList<String>> allLinks) {
		this.setIdLimit(idLimit);
		this.setPageranks(pageranks);
		this.setNbLinksPage(nbLinksPage);
		this.setIdToTitle(idToTitle);
		this.setAllLinks(allLinks);
		
		/*Map<Integer, Double> idToDoublee = new HashMap<Integer, Double>();
		for(int i=0; i<idLimit; i++) {
			idToDoublee.put(i, pageranks[i]);
		}
		this.idToDouble = idToDoublee;*/
	}

	public double[] getPageranks() {
		return pageranks;
	}

	public void setPageranks(double[] pageranks) {
		this.pageranks = pageranks;
	}

	public int[] getNbLinksPage() {
		return nbLinksPage;
	}

	public void setNbLinksPage(int[] nbLinksPage) {
		this.nbLinksPage = nbLinksPage;
	}

	public int getIdLimit() {
		return idLimit;
	}

	public void setIdLimit(int idLimit) {
		this.idLimit = idLimit;
	}

	public Map<Integer, String> getIdToTitle() {
		return idToTitle;
	}

	public void setIdToTitle(Map<Integer, String> idToTitle) {
		this.idToTitle = idToTitle;
	}

	public Map<Integer, ArrayList<String>> getAllLinks() {
		return allLinks;
	}

	public void setAllLinks(Map<Integer, ArrayList<String>> allLinks) {
		this.allLinks = allLinks;
	}

	public Map<Integer, Double> getIdToDouble() {
		return idToDouble;
	}

	public void setIdToDouble(Map<Integer, Double> idToDouble) {
		this.idToDouble = idToDouble;
	}
}
