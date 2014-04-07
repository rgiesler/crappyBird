package crappybird.graphics;

public class Screen {

	public int pixels[], width, height;

	public Screen(int pixels[], int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
}
