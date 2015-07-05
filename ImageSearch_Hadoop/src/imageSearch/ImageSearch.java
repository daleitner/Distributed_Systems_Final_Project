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

public class ImageSearch {

	public static void main(String[] args) throws Exception {
		String imageFolder = "/home/daniel/Distributed_Systems_Final_Project/Aloi Images/png4/";
		String wantedImage = "/home/daniel/Distributed_Systems_Final_Project/Aloi Images/png4/251/251_c.png";
		String input = "";
		String output = "";
		if (args.length != 2) {
			input = "/home/daniel/Distributed_Systems_Final_Project/ImageSearch_Hadoop/input/";
			output = "/home/daniel/Distributed_Systems_Final_Project/ImageSearch_Hadoop/output/";
		} else {
			input = args[0];
			output = args[1];
		}
		
		//create imageFeatures.txt if not already exists
		File txtfile = new File(input + "imageFeatures.txt");
		if(!txtfile.exists()) {
			File f = new File(imageFolder);
			Set<String> fileInfos = FeatureExtract.getFilesInFolder(f);
			FeatureExtract.writeFile(input + "imageFeatures.txt", fileInfos);
		}
		
		//create file for input image
		String inputtxtFile = input + "wantedImage.txt";
		File inputFile = new File(wantedImage);
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

		FileInputFormat.addInputPath(job, new Path(input + "imageFeatures.txt"));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.setMapperClass(ImageSearchMapper.class);
		job.setReducerClass(ImageSearchReducer.class);
		
		// TODO Those two have yet to be adjusted for our needs
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
