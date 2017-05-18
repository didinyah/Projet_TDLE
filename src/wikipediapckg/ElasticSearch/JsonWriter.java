package wikipediapckg.ElasticSearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import wikipediapckg.WriterReader.RawFileIO;
import wikipediapckg.pageRank.IPageRanker;
import wikipediapckg.pageRank.arraysRanker.ArrayPageRank;
import wikipediapckg.pageRank.arraysRanker.ParseWiki;

public class JsonWriter {
	
	public static String OUTPUTJSONFILE = "json/data"; 
	
	public static XContentBuilder createJson(ArrayPageRank apr) {
		
		int idLimit = apr.idLimit;
		int[] linksPage = apr.nbLinksPage;
		double[] pageranks = apr.pageranks;
		XContentBuilder builder = null;
		
		try {
			// écriture dans le fichier json
			File outputj = new File(OUTPUTJSONFILE);
			if(outputj.exists())
			{
				outputj.delete();
			}
			if(!outputj.createNewFile())
			{
				throw new IOException();
			}
			PrintWriter writer = new PrintWriter(outputj);
			
			builder = jsonBuilder();
			for(int i=0; i<idLimit; i++) {
				/*builder = builder.startObject()
				        .field("index").startObject()
					        .field("_index", "wikipedia")
					        .field("_type", "page")
					        .field("_id", i+1)
					    .endObject()
				    .endObject().startObject()
				        .field("id", i+1)
				        .field("nbL", linksPage[i])
				        .field("pr", pageRankPage)
				    .endObject();*/
				writer.print("{\"index\":{\"_index\":\"wikipedia\",\"_type\":\"page\",\"_id\":\"" + String.valueOf(i+1) + "\"}}\n{\"id\":\"" + String.valueOf(i+1) + "\",\"nbL\":\"" + String.valueOf(linksPage[i]) + "\",\"pr\":\"" + String.valueOf(pageranks[i]) + "\"}\n"); 
				System.out.println("Writing to json file : " + i);
			}
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	public static void indexerJson(XContentBuilder builder) {
		RestClient restClient = RestClient.builder(
		        new HttpHost("localhost", 9200, "http"),
		        new HttpHost("localhost", 9201, "http")).build();
		Client client = (Client)restClient;
		
		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
		        .setSource(builder)
		        .get();
	}
	
	public static void main(String[] args) {
		//createJson();
		
	}
}
