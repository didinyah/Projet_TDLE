package wikipediapckg.pageRank.hadoop;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.omg.CORBA.IDLTypeOperations;

import wikipediapckg.WriterReader.RawFileIO;
import wikipediapckg.pageRank.IPageRanker;

public class MapReduceRanker implements IPageRanker {



	public static String OUTPUTLINEJOB1 = "hadoop/Job1OutPut.txt"; 
	@Override
	public void createPageRank(int nbIterations, double damping) 
			throws IOException {


		RawFileIO rfr = new RawFileIO();

		rfr.searchFile();

		// On affiche maintenant les pages et les liens qu'elles r�f�rent

		//HashMap<Integer, ArrayList<Integer>> pagesAndLinks = evaluateLinks(links);
		//printSomePages(pagesAndLinks, idToTitle);

		//HashMap<Integer, Integer> nbLinksPages = evaluateNbLinks(links);

		//JOB 1 ecriture dans le fichier

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



		int[] links = rfr.getLinks();
		Map<Integer, String> idToTitle = rfr.getIdToTitle();
		int i=0;
		int ligne = 0;
		while(i<links.length ) {
			//int idPageActu = rfr.getLinks()[i];			
			String titrePage = idToTitle.get(links[i]);

			i++;
			int nbLiensPageActu = links[i];
			//writer.print(titrePage);
			writer.print(links[i]);
			writer.print(" ");
			writer.print(1.0);
			writer.print(" ");

			// si la page a des liens, on les ajoute tous � l'entier
			if(nbLiensPageActu>0) {
				i++;
				for(int j=0; j<nbLiensPageActu; j++) {
					//writer.print(idToTitle.get(links[i]));
					writer.print(links[i]);

					writer.print(" ");

					i++;
				}
			}
			ligne++;
			//System.out.println("page actu : " + pageActu + "; i : " + i);
		}
		writer.close();


		/*
		try{
			int[][] allLinksSplitted = splitAllLinks(rfr.getLinks(), rfr.getTitleToId().size());
			printPagesLinksSplitted(allLinksSplitted, rfr.getIdToTitle());
		}
		catch(IOException e)
		{
			throw new IOException("[Job 1] impossible de creer la liste des pages initial");
		}*/
	}
}
	/*
	private  int[][] splitAllLinks(int[] links, int nbPages) {
		int[][] res = new int[nbPages][];
		int[] liensPageActu;
		int i=0;
		int pageActu=0;
		while(i<links.length) {
			int idPageActu = links[i];
			i++;
			int nbLiensPageActu = links[i];
			liensPageActu = new int[nbLiensPageActu+2];
			liensPageActu[0] = idPageActu;
			liensPageActu[1] = nbLiensPageActu;
			// si la page a des liens, on les ajoute tous � l'entier
			if(nbLiensPageActu>0) {
				i++;
				for(int j=0; j<nbLiensPageActu; j++) {
					liensPageActu[j+2] = links[i];
					i++;
				}
			}
			res[pageActu] = liensPageActu;
			//System.out.println("page actu : " + pageActu + "; i : " + i);
			pageActu++;
		}
		return res;
	}

	private  void printPagesLinksSplitted(int[][] allLinksSplitted, Map<Integer, String> idToTitle) throws IOException {


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


		for (int i=0; i<idToTitle.size(); i++) {
	    	String titrePage = idToTitle.get(allLinksSplitted[i][0]);
	    	int nbLiensPage = allLinksSplitted[i][1];
	    	writer.print(titrePage);
	    	writer.print(" ");
	    	writer.print(1.0);
	    	writer.print(" ");
			//System.out.println(titrePage + " : " + nbLiensPage);
			for(int j =0; j< nbLiensPage; j++) {
				writer.print(idToTitle.get(allLinksSplitted[i][j+2]) + ",");
			}
			writer.println("\n");
	    }

		writer.close();
	}

}*/
