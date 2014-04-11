package crappybird.entity;

import crappybird.Game;
import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;

public class Scoreboard extends Entity {
	private Sprite sprite;

	public static Scoreboard scoreBoard = new Scoreboard(Sprite.scoreBoard, 15,
			260);

	public Scoreboard(Sprite sprite, int x, int y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}

	public void render(Screen screen) {
		this.sprite.render(screen, this.x, this.y);
	}

	public void update() {
		y -= 5;
		if (y < 100) {
			y = 100;
			Game.state = Game.State.SCORECOUNT;
		}
	}

}
