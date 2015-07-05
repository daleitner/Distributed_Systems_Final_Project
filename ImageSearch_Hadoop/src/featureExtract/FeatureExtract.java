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
	           
	            ret.add(getLine(fileEntry.getName(), features));
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
	
	public static String getLine(String fileName, double[] features) {
		 String featurestring = "";
         for(int i = 0; i<features.length; i++) {
         	featurestring += features[i];
         	if(i < features.length -1)
         		featurestring += ";";
         }
         return fileName + ";" + featurestring;
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
	
	public static void reduce(Set<String> values, String input) {
		
	}

}
