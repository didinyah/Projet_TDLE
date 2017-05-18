package wikipediapckg.pageRank.hadoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import wikipediapckg.WriterReader.RawFileIO;
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
	public void createPageRank(int nbIterations, double damping) 
			throws IOException {



		//JOB 1 ecriture dans le fichier		
		
//		{
//		RawFileIO rfr = new RawFileIO();
//		rfr.searchFile();
//		initMapReduce(rfr.getLinks());
//		}
//		System.gc();
		
		//JOB 2 Interation
		WikiMapReduceIterator process = new WikiMapReduceIterator(damping, nbIterations);
		try {
			process.launch(OUTPUTLINEJOB1,35);
			//process.launch(OUTPUTLINEJOB1,rfr.getIdToTitle().size() );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
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

