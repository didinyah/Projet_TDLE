package wikipediapckg.ElasticSearch;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hit {
    @JsonProperty(value = "_index")
    private String index;
 
    @JsonProperty(value = "_type")
    private String type;
 
    @JsonProperty(value = "_id")
    private String id;
 
    @JsonProperty(value = "_score")
    private Double score;
 
    @JsonProperty(value = "_source")
    private PageWiki source;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public PageWiki getSource() {
		return source;
	}

	public void setSource(PageWiki source) {
		this.source = source;
	}
}
