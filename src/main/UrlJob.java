package main;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

import com.martinkl.warc.mapreduce.WARCInputFormat;
import com.martinkl.warc.WARCWritable;
//import org.archive.io.ArchiveReader;
//import org.archive.io.ArchiveRecord;


public class UrlJob {

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: UrlJob <in> <out>");
      System.err.println("       where <in> can be a set of files (e.g data/*.warc.gz");
      System.exit(2);
    }
    Job job = new Job(conf, "Url_Job");
    job.setJarByClass(UrlJob.class);

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    //inputPath = "s3n://aws-publicdatasets/common-crawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/wet/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.wet.gz";
    //inputPath = "s3n://aws-publicdatasets/common-crawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/wet/*.warc.wet.gz";
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));


    FileSystem fs = FileSystem.newInstance(conf);
    if (fs.exists(new Path(otherArgs[1]))) {
		fs.delete(new Path(otherArgs[1]), true);
    }
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

    job.setInputFormatClass(WARCInputFormat.class);   // Our custom input split
    job.setOutputFormatClass(TextOutputFormat.class); // The default


    job.setMapOutputKeyClass(Text.class);  
    job.setMapOutputValueClass(LongWritable.class);

    job.setMapperClass(UrlMapper.class);
    job.setReducerClass(LongSumReducer.class);
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
