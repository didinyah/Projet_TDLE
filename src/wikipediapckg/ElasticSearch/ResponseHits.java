package wikipediapckg.ElasticSearch;

import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseHits {
    private Hits hits;

	public Hits getHits() {
		return hits;
	}

	public void setHits(Hits hits) {
		this.hits = hits;
	}
}

