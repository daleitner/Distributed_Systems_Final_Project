package imageSearch;

public class ImageFeatures {

	private String image_title = "";
	private double[] image_features;
	public ImageFeatures(String textLine) {
		String[] arr = textLine.split(";");
		if(arr.length > 0) {
			image_title = arr[0];
			image_features = new double[arr.length-1];
			
			for(int i = 1; i<arr.length; i++) {
				image_features[i-1] = Double.parseDouble(arr[i]);
			}
		}
	}
	public String getImage_title() {
		return image_title;
	}
	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}
	public double[] getImage_features() {
		return image_features;
	}
	public void setImage_features(double[] image_features) {
		this.image_features = image_features;
	}
	
}
