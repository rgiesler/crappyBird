package crappybird.entity;

import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;

public class Bird extends Entity {

	private Sprite[] animation;
	private int animationCycle, animationCycleCounter;
	public double yVel, yAccel;

	public static Sprite[] birdAnimation = { Sprite.birdDown, Sprite.birdMid,
			Sprite.birdUp, Sprite.birdMid };
	public static Bird theBird = new Bird(30, 120, birdAnimation);

	public Bird(int x, int y, Sprite[] animation) {
		this.x = x;
		this.y = y;
		this.animation = animation;
		animationCycle = 2;
		animationCycleCounter = 0;
		yVel = 3;
		yAccel = .2;
	}

	public void render(Screen screen) {
		double rotationAngle = 0;
		rotationAngle = yVel * 0.2 - 0.6;

		if (rotationAngle > Math.PI / 2)
			rotationAngle = Math.PI / 2;
		if (rotationAngle < -0.4)
			rotationAngle = -0.4;
		animation[animationCycle].render(screen, x, y, rotationAngle);
	}

	public void update() {
		// Switch Animation
		animationCycleCounter++;
		if (animationCycleCounter > 10) {
			animationCycle++;
			animationCycleCounter = 0;
			if (animationCycle >= animation.length)
				animationCycle = 0;
		}

		yVel += yAccel;
		y += yVel;
	}

	public void updateFlap() {
		// Switch Animation
		animationCycleCounter++;
		if (animationCycleCounter > 10) {
			animationCycle++;
			animationCycleCounter = 0;
			if (animationCycle >= animation.length)
				animationCycle = 0;
		}
	}

	public boolean collide(Pipe pipe) {
		double rotationAngle = 0;
		rotationAngle = yVel * 0.2 - 0.6;

		if (rotationAngle > Math.PI / 2)
			rotationAngle = Math.PI / 2;
		if (rotationAngle < -0.4)
			rotationAngle = -0.4;
		return animation[animationCycle].collide(x, y, rotationAngle, pipe);
	}

	public boolean collide(Floor floor) {
		double rotationAngle = 0;
		rotationAngle = yVel * 0.2;

		if (rotationAngle > Math.PI / 2)
			rotationAngle = Math.PI / 2;
		if (rotationAngle < -Math.PI / 2)
			rotationAngle = -Math.PI / 2;
		return animation[animationCycle].collide(x, y, rotationAngle, floor);
	}
}
