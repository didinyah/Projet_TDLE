package wikipediapckg.pageRank.hadoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import main.ResultDTO;
import wikipediapckg.pageRank.IPageRanker;
import wikipediapckg.pageRank.hadoop.job.WikiMapReduceIterator;


/**
 * methode qui permet de calcul du OageRank a partir du Map Reduce de Hadoop
 * @author dinar
 * @deprecated la methode est moins efficiante que la methode array
 */
public  class  MapReduceRanker implements IPageRanker {



	public static String OUTPUTLINEJOB1 = "hadoop/TestMapReduce.txt"; 
	//public static String OUTPUTLINEJOB1 = "hadoop/Job1OutPut.txt"; 

	@Override
	public ResultDTO createPageRank(int nbIterations, double damping) 
			throws IOException {



		//JOB 1 ecriture dans le fichier		
		
//		{
//		RawFileIO rfr = new RawFileIO();
//		rfr.searchFile();
//		initMapReduce(rfr.getLinks());
//		}
//		System.gc();
		
		//JOB 2 Iteration et lecture
		HashMap<Integer,Double> pagerank = new HashMap<Integer,Double>();
		double[] resPagerank;
		HashMap<Integer,Integer>  nbLinksPage = new HashMap<Integer,Integer>() ;
		HashMap<Integer,String>  idtotitleErzatz = new HashMap<Integer,String>() ;
		HashMap<String,Integer>  titletoidErzatz = new HashMap<String,Integer>() ;
		
		HashMap<Integer,String[]> listdetablien = new HashMap<Integer,String[]>();
		HashMap<Integer,ArrayList<String>> listedeliens = new HashMap<Integer,ArrayList<String>>();


		int[] resLinksPage;

		ResultDTO res = null;

		int nbMax = 0;
		
		WikiMapReduceIterator process = new WikiMapReduceIterator(damping, nbIterations);
		try {
			Scanner result = new Scanner(new FileReader(process.launch(OUTPUTLINEJOB1,35)));
			//process.launch(OUTPUTLINEJOB1,rfr.getIdToTitle().size() );
			
			while(result.hasNext())
			{
				String ligne = result.nextLine();
				String[] split = ligne.split("\t");
				
				int id = Integer.valueOf(split[0]);
				double pagerankValue = Double.valueOf(split[1]);
				
				int linksNumber = split[2].split(",").length;
				
				pagerank.put(id, pagerankValue);
				nbLinksPage.put(id, linksNumber);
				listdetablien.put(id,  split[2].split(","));
				
				if(nbMax < id)
				{
					nbMax = id;
				}
				
			}
			
			resPagerank = new double[pagerank.size()];
			resLinksPage = new int[nbLinksPage.size()];
			
			for( Entry<Integer,Double> k : pagerank.entrySet())
			{
				resPagerank[k.getKey()-1] = k.getValue();
			}
			for( Entry<Integer,Integer> k : nbLinksPage.entrySet())
			{
				resLinksPage[k.getKey()-1] = k.getValue();
				
				idtotitleErzatz.put(k.getKey(), "Test"+k.getKey() );
				titletoidErzatz.put("Test"+k.getKey(), k.getKey());
			}
			
			for( Entry<Integer,String[]> k :  listdetablien.entrySet())
			{
				ArrayList<String> values = new ArrayList<String>();
				for( String q : k.getValue())
				{
					values.add(idtotitleErzatz.get(Integer.valueOf(q)));
				}
				listedeliens.put(k.getKey(), values);
			}

		
			//res = new ResultDTO(nbMax, resPagerank, resLinksPage, rfr.getIdToTitle());
			res = new ResultDTO(nbMax, resPagerank, resLinksPage, idtotitleErzatz,listedeliens);
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return res;
		
		
	}
	public void initMapReduce(int[] links) throws IOException, FileNotFoundException {

		File outputj = new File(OUTPUTLINEJOB1);
		if(outputj.exists())
		{
			outputj.delete();
		}
		if(!outputj.createNewFile())
		{
			throw new IOException();
		}
		PrintWriter writer = new PrintWriter(outputj);
		//int[] links = rfr.getLinks();
		int i=0;
		while(i<links.length ) {
			i++;
			int nbLiensPageActu = links[i];
			//writer.print(titrePage);
			writer.print(links[i]);
			writer.print("\t");
			writer.print(0.0);
			writer.print("\t");
			// si la page a des liens, on les ajoute tous � l'entier
			if(nbLiensPageActu>0) {
				i++;					
				writer.print(links[i]);
				i++;
				for(int j=1; j<nbLiensPageActu; j++) {
                    writer.print(",");
					writer.print(links[i]);
					i++;

				}
			}
		}
		writer.close();
	}
}

