package imageSearch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import featureExtract.FeatureExtract;

/**
 * Example from Hadoop book. Serves as base for our own program.
 * 
 * @author julian TODO Adjust parameters and other things to our needs.
 */
public class ImageSearch {

	public static void main(String[] args) throws Exception {
		String projectPath = "/home/daniel/Distributed_Systems_Final_Project/";
		String inputPath = projectPath + "ImageSearch_Hadoop/input/";
		String imageFolder = projectPath + "Aloi Images/png4/";
		String input = "";
		String output = "";
		String imageFeatureFile = inputPath + "imageFeatures.txt";
		if (args.length != 2) {
			//System.err.println("Usage: FeatureExtract <input path> <output path>");
			//System.exit(-1);
			input = imageFolder + "251/251_c.png";
			output = projectPath + "ImageSearch_Hadoop/output/";
		} else {
			input = args[0];
			output = args[1];
		}
		
		//create imageFeatures.txt if not already exists
		File txtfile = new File(imageFeatureFile);
		if(!txtfile.exists()) {
			File f = new File(imageFolder);
			Set<String> fileInfos = FeatureExtract.getFilesInFolder(f);
			FeatureExtract.writeFile(imageFeatureFile, fileInfos);
		}
		
		//create file for input image
		String inputtxtFile = inputPath + "wantedImage.txt";
		File inputFile = new File(input);
		 BufferedImage img = null;
         double[] features = null;
			try {
				img = ImageIO.read(inputFile);
				features = FeatureExtract.extract_cedd(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		Set<String> inputFiles = new HashSet<String>();
		inputFiles.add(FeatureExtract.getLine(inputFile.getName(), features));
		FeatureExtract.writeFile(inputtxtFile, inputFiles);

		//get similar images
		Job job = new Job();
		job.setJarByClass(ImageSearch.class);
		job.setJobName("ImageSearch");

		FileInputFormat.addInputPath(job, new Path(imageFeatureFile));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.setMapperClass(ImageSearchMapper.class);
		job.setReducerClass(ImageSearchReducer.class);
		
		// TODO Those two have yet to be adjusted for our needs
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
