package crappybird.controls;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import crappybird.Game;
import crappybird.Game.State;
import crappybird.entity.Bird;
import crappybird.entity.Floor;
import crappybird.entity.Pipe;
import crappybird.entity.Scoreboard;
import crappybird.graphics.Sprite;
import crappybird.sound.Sound;

public class Mouse implements MouseListener {

	public Mouse() {

	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		if (Game.state == State.PAUSE) {
			Game.state = State.PLAY;
		}
		if (Game.state == State.PLAY) {
			Bird.theBird.yVel = -3;
			Sound.playSound("res\\sfx_wing.wav");
		} else if (Game.state == State.SCORE) {
			Sound.playSound("res\\sfx_swooshing.wav");
			reset();
		}
	}

	private static void reset() {
		Bird.theBird = new Bird(30, 120, Bird.birdAnimation);
		Pipe.startingPoint = Pipe.random.nextInt(255);
		Pipe.pipe1 = new Pipe(190 + Pipe.startingPoint, -1, Sprite.upperTube,
				Sprite.lowerTube, 50);
		Pipe.pipe2 = new Pipe(260 + Pipe.startingPoint, -1, Sprite.upperTube,
				Sprite.lowerTube, 50);
		Pipe.pipe3 = new Pipe(330 + Pipe.startingPoint, -1, Sprite.upperTube,
				Sprite.lowerTube, 50);
		Floor.theFloor = new Floor(0, 220, Sprite.floor, -1);
		Game.score = 0;
		Game.state = State.PAUSE;
		Scoreboard.scoreBoard.y = 260;
	}

	public void mouseReleased(MouseEvent arg0) {

	}
}
