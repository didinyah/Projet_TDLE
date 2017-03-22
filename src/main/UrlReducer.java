package main;


import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;





public class UrlReducer extends Reducer<Text, LongWritable, Text, LongWritable> {


   @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException,InterruptedException {

	long sum=0;
	for (LongWritable v : values)
		sum += v.get();
	
	context.write(key, new LongWritable(sum));	

    }
}

