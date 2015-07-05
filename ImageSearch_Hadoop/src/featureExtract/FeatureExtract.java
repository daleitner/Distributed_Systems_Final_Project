package featureExtract;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import net.semanticmetadata.lire.imageanalysis.CEDD;

/**
 * Example from Hadoop book. Serves as base for our own program.
 * 
 * @author julian TODO Adjust parameters and other things to our needs.
 */
public class FeatureExtract {

	public static void main(String[] args) throws Exception {
		String input = "";
		String output = "";
		String imageFeatureFile = "/home/daniel/Distributed_Systems_Final_Project/ImageSearch_Hadoop/input/imageFeatures.txt";
		if (args.length != 2) {
			//System.err.println("Usage: FeatureExtract <input path> <output path>");
			//System.exit(-1);
			input = "/home/daniel/Distributed_Systems_Final_Project/Aloi Images/png4/";
			output = "/home/daniel/Distributed_Systems_Final_Project/ImageSearch_Hadoop/output/";
		} else {
			input = args[0];
			output = args[1];
		}
		File txtfile = new File(imageFeatureFile);
		if(!txtfile.exists()) {
			File f = new File(input);
			Set<String> fileInfos = getFilesInFolder(f);
			writeFile(imageFeatureFile, fileInfos);
		}
		/*Job job = new Job();
		job.setJarByClass(FeatureExtract.class);
		job.setJobName("FeatureExtract");

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.setMapperClass(FeatureExtractMapper.class);
		job.setReducerClass(FeatureExtractReducer.class);

		// TODO Those two have yet to be adjusted for our needs
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);*/
	}
	
	public static Set<String> getFilesInFolder(File folder) {
	    Set<String> ret = new HashSet<String>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            Set<String> childs = getFilesInFolder(fileEntry);
	            ret.addAll(childs);
	        } else {
	            BufferedImage img = null;
	            double[] features = null;
				try {
					img = ImageIO.read(fileEntry);
					features = extract_cedd(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            String featurestring = "";
	            for(int i = 0; i<features.length; i++) {
	            	featurestring += features[i];
	            	if(i < features.length -1)
	            		featurestring += ";";
	            }
	            ret.add(fileEntry.getName() + ";" + featurestring);
	        }
	    }
		return ret;
	}
	
	public static double[] extract_cedd (BufferedImage bimg) {
		CEDD sch = new CEDD ();
		sch.extract(bimg);
		double[] histogram = sch.getDoubleHistogram();
		return histogram;
	}
	
	public static void writeFile(String fileName, Set<String>content) {
	        BufferedWriter output = null;
	        try {
	            File file = new File(fileName);
	            output = new BufferedWriter(new FileWriter(file));
	            Iterator<String> it = content.iterator();
	            while(it.hasNext()) {
	            	output.write(it.next() + "\r\n");
	            }
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        } finally {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	}

}
