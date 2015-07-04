package featureExtract;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Example from Hadoop book. Serves as base for our own program.
 * 
 * @author julian TODO Adjust parameters and other things to our needs.
 */
public class FeatureExtract {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: FeatureExtract <input path> <output path>");
			System.exit(-1);
		}

		Job job = new Job();
		job.setJarByClass(FeatureExtract.class);
		job.setJobName("FeatureExtract");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(FeatureExtractMapper.class);
		job.setReducerClass(FeatureExtractReducer.class);

		// TODO Those two have yet to be adjusted for our needs
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
