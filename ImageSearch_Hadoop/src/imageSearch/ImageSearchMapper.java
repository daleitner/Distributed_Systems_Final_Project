package imageSearch;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Example from Hadoop book. Serves as base for our own program.
 * 
 * @author julian
 *
 */
public class ImageSearchMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private static final int MISSING = 9999;

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();

		String year = line.substring(15, 19);
		/*int airTemperature;
		if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
			airTemperature = Integer.parseInt(line.substring(88, 92));
		} else {
			airTemperature = Integer.parseInt(line.substring(87, 92));
		}
		String quality = line.substring(92, 93);
		if (airTemperature != MISSING && quality.matches("[01459]")) {
			context.write(new Text(year), new IntWritable(airTemperature));
		}*/
	}
	
	public double getManhattanDistance(double[] a, double[] b) {
		double distance = 0.0;
		if(a.length == b.length) {
			for(int i = 0; i<a.length; i++) {
				if(a[i] >= b[i])
					distance = distance + (a[i]-b[i]);
				else
					distance = distance + (b[i]-a[i]);
			}
		}
		return distance;
	}
}
