package main;

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
	
	public ResultDTO(int idLimit, double[] pageranks, int[] nbLinksPage, Map<Integer, String> idToTitle) {
		this.setIdLimit(idLimit);
		this.setPageranks(pageranks);
		this.setNbLinksPage(nbLinksPage);
		this.setIdToTitle(idToTitle);
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
}
