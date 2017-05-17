package wikipediapckg.pageRank.hadoop.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WikiMapReduceIterator 
{
	private static String OUTPUTFOLDER = "hadoop" ;

	private static String OUTPUTFILENAME = "job2n";

	private int  iteration = 10;
	private static double damping = 0.85;

	/**
	 * Classe utilisee pour le Mapper de texte il le prepare pour le {@link RankerReducer}
	 * @author dinar
	 *
	 */
	public static class RankerMapper 
	extends Mapper<LongWritable, Text, Text, Text>{

		public void map(LongWritable key, Text value,
				Context context) 
						throws IOException, InterruptedException {

			StringTokenizer itrFile = new StringTokenizer(value.toString(),"\n");

			while (itrFile.hasMoreTokens()) {

				String ligne = itrFile.nextToken();

				String[] elems =  ligne.split("\t");
				// Page_Or valeur liensExterne

				if(elems.length == 2)
				{
					context.write(new Text(elems[0]), new Text("!"));
				}
				if(elems.length == 3)
				{
					String[] liens = elems[2].split(",");
					if(liens.length != 0)
					{
						for( String lien : liens)
						{
							context.write(new Text(lien),
									new Text(elems[0]+"\t"+elems[1]+"\t"+ liens.length ));

						}
					}

					//Ecriture de lien original
					context.write(new Text(elems[0]), new Text('|'+elems[2]));
				}

			}


		}
	}

	/**
	 * Permet de reduire le mapiing du {@link RankerMapper} sous forme de 
	 * @author dinar
	 *
	 */
	public static class RankerReducer
	extends Reducer<Text,Text,Text,Text>{
		public void reduce(Text key, Iterable<Text> values, 
				Context context
				) throws IOException, InterruptedException {

			//boolean pageEstExistante = false;
			String link = "";

			double sommePageAvecLien = 0;

			for(Text val :values)
			{
				String mot = val.toString();

				/*if(mot.equals("!"))
				{
					pageEstExistante = true;
				}*/
				if(mot.startsWith("|"))
				{
					link = "\t" +mot.substring(1);
				}
				else
				{
					String[] chiffres = mot.split("\t");
					if(chiffres.length == 3)
					{
						double  rank = Double.valueOf(chiffres[1]);
						int nombreLiens = Integer.valueOf(chiffres[2]);
						sommePageAvecLien += rank/nombreLiens; 

					}

					
				}

			}
			//if(pageEstExistante)
			//{
				double newRank =  (damping * sommePageAvecLien + (1-damping));
				if(!link.isEmpty())
					context.write(key,new Text( String.format(Locale.ENGLISH,"%.10f",newRank)+link ));
				else
					context.write(key,new Text( String.format(Locale.ENGLISH,"%.10f",newRank)));

			
			//}

		}
	}

	/**
	 * Constructeur de base 
	 * permet de definir la valeur de base de damping et le nomre d'iteration a faire
	 * @param damping la valeur constante utilise a chaque iteration du MapReducer
	 * @param iteration le nombre de mapreduce a effectue 
	 */
	public WikiMapReduceIterator(double damping,int iteration)
	{
		this.iteration = iteration;
		//this.iteration =1; 
		WikiMapReduceIterator.damping = damping;
	}

	/**
	 * permet de llancer loperation de Map Reduce
	 * @param input
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public String launch(String input) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException
	{
		File inputFile = new File(input); 

		if(!inputFile.exists())
		{
			throw new FileNotFoundException("le fichier n'existe pas");
		}



		String oldPath = input;

		for(int i = 0 ; i < iteration ;i++ )
		{
			if(i!= 0)
			{
				oldPath = oldPath+"/part-r-00000";
			}
			
			String newPath = OUTPUTFOLDER + "/" + OUTPUTFILENAME + i;
			File outputfile = new File(newPath);
			suppressionRecursive(outputfile);
			
			
			Configuration conf = new Configuration();
			Job job = new Job(conf,"iterationPageRank");
			job.setJarByClass(WikiMapReduceIterator.class);
			job.setMapperClass(RankerMapper.class);
			//job.setCombinerClass(RankerReducer.class);
			job.setReducerClass(RankerReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.setInputPaths(job, new Path(oldPath));
			FileOutputFormat.setOutputPath(job, new Path(newPath));

			//Lancement du job
			job.waitForCompletion(true);


			oldPath = newPath;

		}
		
		if(iteration!= 0)
		{
			oldPath = oldPath+"/part-r-00000";
		}
		
		
		return oldPath;

	}

	public void suppressionRecursive(File outputfile) {
		if(outputfile.isDirectory())
		{
			for (File file : outputfile.listFiles())
			{
			
				 suppressionRecursive(file);
			}
		}
		outputfile.delete();
	}
	



}