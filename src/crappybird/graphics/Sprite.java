package crappybird.graphics;

import crappybird.Game;
import crappybird.entity.Floor;
import crappybird.entity.Pipe;

public class Sprite {

	private SpriteSheet spriteSheet;
	public int xOrg, yOrg, w, h, alphaColor;

	// -----------SPRITES--------------
	public static Sprite background = new Sprite(SpriteSheet.tiles, 0, 0, 143,
			255, 0xE4005F);
	public static Sprite birdMid = new Sprite(SpriteSheet.tiles, 260, 86, 24,
			24, 0xE4005F);
	public static Sprite birdUp = new Sprite(SpriteSheet.tiles, 260, 60, 24,
			24, 0xE4005F);
	public static Sprite birdDown = new Sprite(SpriteSheet.tiles, 219, 120, 24,
			24, 0xE4005F);
	public static Sprite floor = new Sprite(SpriteSheet.tiles, 146, 0, 153, 55,
			0xE4005F);
	public static Sprite upperTube = new Sprite(SpriteSheet.tiles, 302, 0, 26,
			135, 0xE4005F);
	public static Sprite lowerTube = new Sprite(SpriteSheet.tiles, 330, 0, 26,
			121, 0xE4005F);

	// -----------SPRITES--------------

	public Sprite(SpriteSheet spriteSheet, int xOrg, int yOrg, int width,
			int height, int alphaColor) {
		this.spriteSheet = spriteSheet;
		this.xOrg = xOrg;
		this.yOrg = yOrg;
		this.w = width;
		this.h = height;
		this.alphaColor = alphaColor;
	}

	public void render(Screen screen, int xPos, int yPos) {
		xPos = xPos * Game.scale;
		yPos = yPos * Game.scale;
		for (int x = 0; x < w * Game.scale; x++) {
			for (int y = 0; y < h * Game.scale; y++) {
				if (x + xPos >= 0 && x + xPos < screen.width && y + yPos >= 0
						&& y + yPos < screen.height) {
					int color = spriteSheet.pixels[xOrg + x / Game.scale
							+ (yOrg + y / Game.scale) * spriteSheet.width];
					if (color != alphaColor) {
						screen.pixels[x + xPos + (y + yPos) * screen.width] = color;
					}
				}
			}
		}
	}

	public void render(Screen screen, int xPos, int yPos, double angle) {
		xPos *= Game.scale;
		yPos *= Game.scale;
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		for (int x = 0; x < w * Game.scale; x++) {
			for (int y = 0; y < h * Game.scale; y++) {
				int u = (int) (((cosAngle * (x - w * Game.scale / 2) + sinAngle
						* (y - h * Game.scale / 2)) + w * Game.scale / 2));
				int v = (int) (((-sinAngle * (x - w * Game.scale / 2) + cosAngle
						* (y - h * Game.scale / 2)) + h * Game.scale / 2));
				u = u / Game.scale;
				v = v / Game.scale;
				if (x + xPos >= 0 && x + xPos < screen.width && y + yPos >= 0
						&& y + yPos < screen.height & u >= 0 && u < w && v >= 0
						& v < h) {
					int color = spriteSheet.pixels[xOrg + u + (yOrg + v)
							* spriteSheet.width];
					if (color != alphaColor) {
						screen.pixels[x + xPos + (y + yPos) * screen.width] = color;
					}
				}
			}
		}
	}

	public boolean collide(int xPos, int yPos, double angle, Pipe pipe) {
		xPos *= Game.scale;
		yPos *= Game.scale;
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		for (int x = 0; x < w * Game.scale; x++) {
			for (int y = 0; y < h * Game.scale; y++) {
				int u = (int) (((cosAngle * (x - w * Game.scale / 2) + sinAngle
						* (y - h * Game.scale / 2)) + w * Game.scale / 2));
				int v = (int) (((-sinAngle * (x - w * Game.scale / 2) + cosAngle
						* (y - h * Game.scale / 2)) + h * Game.scale / 2));
				u = u / Game.scale;
				v = v / Game.scale;
				if (x + xPos >= 0 && y + yPos >= 0 && u >= 0 && u < w && v >= 0
						& v < h) {
					int color = spriteSheet.pixels[xOrg + u + (yOrg + v)
							* spriteSheet.width];
					if (color != alphaColor) {
						int X = x + xPos;
						int Y = y + yPos;
						double scale = Game.scale;
						if ((X >= pipe.x * scale)
								&& (X <= pipe.x * scale + pipe.upperSprite.w
										* scale)
								&& ((Y <= pipe.height * scale) || (Y >= (pipe.height + pipe.gap)
										* scale))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean collide(int xPos, int yPos, double angle, Floor floor) {
		xPos *= Game.scale;
		yPos *= Game.scale;
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		for (int x = 0; x < w * Game.scale; x++) {
			for (int y = 0; y < h * Game.scale; y++) {
				int u = (int) (((cosAngle * (x - w * Game.scale / 2) + sinAngle
						* (y - h * Game.scale / 2)) + w * Game.scale / 2));
				int v = (int) (((-sinAngle * (x - w * Game.scale / 2) + cosAngle
						* (y - h * Game.scale / 2)) + h * Game.scale / 2));
				u = u / Game.scale;
				v = v / Game.scale;
				if (x + xPos >= 0 && y + yPos >= 0 && u >= 0 && u < w && v >= 0
						& v < h) {
					int color = spriteSheet.pixels[xOrg + u + (yOrg + v)
							* spriteSheet.width];
					if (color != alphaColor) {
						int X = x + xPos;
						int Y = y + yPos;
						double scale = Game.scale;
						if (Y >= floor.y*scale) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
