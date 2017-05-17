package wikipediapckg.ElasticSearch;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import static org.elasticsearch.common.xcontent.XContentFactory.*;


public class ElasticSearchImplementation {
	
	private RestClient restClient;
	private static String index = "wikipedia", type="page"; 
	public RestClient getRestClient() 
	{
		if(restClient == null)
		{
			 restClient = RestClient.builder(
				        new HttpHost("localhost", 9200, "http")).build();
		}
		return restClient;
	}
	
	public void CloseClient()
	{
		try 
		{
			restClient.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void AddJsonToElasticSearch(String json, String requete, String url)
	{
		HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

		try 
		{
			Response indexResponse = restClient.performRequest(
			        requete /*PUT*/,
			        url/*twitter/tweet/1*/,
			        Collections.<String, String>emptyMap(),
			        entity);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
//	public void JeSaisPasEncore()
//	{
//		int numRequests = 10;
//		final CountDownLatch latch = new CountDownLatch(numRequests);
//
//		for (int i = 0; i < numRequests; i++) {
//		    restClient.performRequestAsync(
//		        "PUT",
//		        "/twitter/tweet/" + i,
//		        Collections.<String, String>emptyMap(),
//		        //assume that the documents are stored in an entities array
//		        entities[i],
//		        new ResponseListener() {
//		            @Override
//		            public void onSuccess(Response response) {
//		                System.out.println(response);
//		                latch.countDown();
//		            }
//
//		            @Override
//		            public void onFailure(Exception exception) {
//		                latch.countDown();
//		            }
//		        }
//		    );
//		}
//
//		//wait for all requests to be completed
//		latch.await();
//	}
	
	public void BulkAdd(String id, String[] json)
	{
		Client client = (Client)restClient;
		for(int i = 0; i < json.length ; i++)
		{
			BulkRequestBuilder bulkRequest = ((Client) restClient).prepareBulk();
			bulkRequest.add(client.prepareIndex(index, type, id)
		        .setSource(json[i])
		        );
		}
	}
	
	public GetResponse getResponseRequest(int nb) {
		Client client = (Client)restClient;
		System.out.println(client.toString());
		GetResponse response = client.prepareGet("wikipedia", "page", String.valueOf(nb))
		        .setOperationThreaded(false)
		        .get();
		return response;
	}
}
