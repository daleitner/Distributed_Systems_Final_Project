package imageSearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	private static final String PATH = "/home/daniel/Distributed_Systems_Final_Project/ImageSearch_Hadoop/input/wantedImage.txt";

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String wantedImage = readFirstLineOfFile(PATH);
		ImageFeatures actImg = new ImageFeatures(line);
		ImageFeatures wantedImg = new ImageFeatures(wantedImage);
		double distance = getEuclidianDistance(wantedImg.getImage_features(), actImg.getImage_features());
		context.write(new Text(actImg.getImage_title()), new IntWritable((int) distance));
	}
	
	public double getEuclidianDistance(double[] a, double[] b) {
		double distance = 0.0;
		if(a.length == b.length) {
			for(int i = 0; i<a.length; i++) {
				distance = distance + ((a[i]-b[i])*(a[i]-b[i]));
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}
	
	public String readFirstLineOfFile(String path) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
	        StringBuilder sb = new StringBuilder();
	        String line;
			line = br.readLine();


	        if (line != null) {
	            return line;
	        }
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return "";
	}
}
