package imageSearch;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class ImageSearchReducer extends Reducer<Text, Text, Text, DoubleWritable> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		int count = 10;
		double[] minValues = new double[count];
		String[] minValueImgNames = new String[count];
		Set<String> mapValues = new HashSet<String>();
		for(Text value : values) {
			mapValues.add(value.toString());
		}
		
		for(int i = 0; i<count; i++) {
			minValues[i] = Double.MAX_VALUE;
			minValueImgNames[i] = "";
			Iterator it = mapValues.iterator();
		while (it.hasNext()) {
			String value = it.next().toString();
			String[]arr = value.split(";");
			double actValue = Double.parseDouble(arr[1]);
			if(checkValues(minValueImgNames, i, arr[0]) && actValue < minValues[i]) {
				minValues[i] = actValue;
				minValueImgNames[i] = arr[0];
			}
		}
		context.write(new Text(minValueImgNames[i]), new DoubleWritable(minValues[i]));
		System.out.println(i + ". kleinste distanz: " + minValueImgNames[i] + "[" + minValues[i] + "]");
		}
	}
	
	//check if image is already used
	public boolean checkValues(String[] names, int actPos, String actName) {
		for(int i = 0; i<actPos; i++) {
			if(names[i].equals(actName))
				return false;
		}
		return true;
	}
}
