package crappybird.graphics;

public class Text {
	public static void displayLargeText(Screen screen, int num, int x, int y) {
		if (num == 0) {
			Sprite.bigNum[0].render(screen, x, y);
		}
		int div = 10;
		while (num > 0) {
			int display = num % div;
			num /= div;
			Sprite.bigNum[display].render(screen, x, y);
			x -= Sprite.bigNum[display].w - 1;
		}
	}
}
