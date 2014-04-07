package crappybird.entity;

import java.util.Random;

import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;

public class Pipe extends Entity {

	public Sprite upperSprite;
	private Sprite lowerSprite;
	public int height;
	public int gap;
	public boolean passed = false;
	private int xVel;
	public static final Random random = new Random();

	public static int startingPoint = random.nextInt(255);
	public static Pipe pipe1 = new Pipe(190 + startingPoint, -1,
			Sprite.upperTube, Sprite.lowerTube, 50);
	public static Pipe pipe2 = new Pipe(260 + startingPoint, -1,
			Sprite.upperTube, Sprite.lowerTube, 50);
	public static Pipe pipe3 = new Pipe(330 + startingPoint, -1,
			Sprite.upperTube, Sprite.lowerTube, 50);

	public Pipe(int x, int xVel, Sprite upperSprite, Sprite lowerSprite, int gap) {
		this.x = x;
		this.xVel = xVel;
		this.upperSprite = upperSprite;
		this.lowerSprite = lowerSprite;
		this.height = random.nextInt(85) + 50;
		this.gap = gap;
	}

	public void update() {
		x += xVel;
		if (x < -upperSprite.w) {
			x = 179;
			height = random.nextInt(85) + 50;
			passed = false;
		}
	}

	public void render(Screen screen) {
		// Upper
		upperSprite.render(screen, x, height - upperSprite.h);
		// Lower
		lowerSprite.render(screen, x, height + gap);
	}
}
