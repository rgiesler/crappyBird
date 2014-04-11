package crappybird;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

import crappybird.controls.Mouse;
import crappybird.entity.Bird;
import crappybird.entity.Floor;
import crappybird.entity.Pipe;
import crappybird.entity.Scoreboard;
import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;
import crappybird.graphics.Text;
import crappybird.sound.Sound;

public class Game extends Canvas implements Runnable {

	public enum State {
		PLAY, PAUSE, FALL, SCOREBOARDSCROLL, SCORECOUNT, SCORE;
	}

	public enum Medal {
		NONE, BRONZE, SILVER, GOLD, PLATINUM;
	}

	private static final long serialVersionUID = 1L;
	private Screen game;
	private JFrame frame;
	private boolean running = false;
	private Thread thread;
	private Mouse mouse;
	private int flash = 0;
	public static int score = 0;
	public int scoreTimer = 0;
	public int scoreCounter = 0;
	public int scoreBest = 0;
	boolean newHighScore = true;

	public static final int scale = 2;
	public static final int width = 143 * scale;
	public static final int height = 255 * scale;

	public static State state = State.PAUSE;
	public static Medal medal = Medal.NONE;

	private BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();

	private Preferences prefs;

	public static String title = "CrappyBird";

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		game = new Screen(pixels, width, height);
		frame = new JFrame();
		mouse = new Mouse();
		addMouseListener(mouse);

		prefs = Preferences.userRoot().node(this.getClass().getName());
		scoreBest = prefs.getInt("HighScore", 0);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		double delta = 0;

		int frames = 0;
		int updates = 0;

		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		requestFocus();

		while (running) {
			long now = System.nanoTime();

			// 60 updates per second
			delta += (now - lastTime) * 60.0 / 1000000000.0;
			lastTime = now;

			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}

			render();
			frames++;

			// Update fps in title every second
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates + " u/s , " + frames
						+ " f/s");
				frames = 0;
				updates = 0;
			}
		}

		stop();
	}

	public void update() {
		switch (state) {
		case PLAY:
			// Score Detection
			if (Bird.theBird.x + Sprite.birdMid.w / 2 >= Pipe.pipe1.x
					&& Pipe.pipe1.passed == false) {
				Pipe.pipe1.passed = true;
				score++;
				Sound.playSound("sfx_point.wav");
			}
			if (Bird.theBird.x + Sprite.birdMid.w / 2 >= Pipe.pipe2.x
					&& Pipe.pipe2.passed == false) {
				Pipe.pipe2.passed = true;
				score++;
				Sound.playSound("sfx_point.wav");
			}
			if (Bird.theBird.x + Sprite.birdMid.w / 2 >= Pipe.pipe3.x
					&& Pipe.pipe3.passed == false) {
				Pipe.pipe3.passed = true;
				score++;
				Sound.playSound("sfx_point.wav");
			}

			// Collision Detection
			if (Bird.theBird.collide(Pipe.pipe1)
					|| Bird.theBird.collide(Pipe.pipe2)
					|| Bird.theBird.collide(Pipe.pipe3)) {
				Sound.playSound("sfx_hit.wav");
				Sound.playSound("sfx_die.wav");
				state = State.FALL;
				flash = 50;
			} else if (Bird.theBird.collide(Floor.theFloor)) {
				Sound.playSound("sfx_hit.wav");
				Sound.playSound("sfx_die.wav");
				state = State.SCOREBOARDSCROLL;
				Sound.playSound("sfx_swooshing.wav");
				flash = 50;
				decideMedal(score);
				if (score > scoreBest) {
					scoreBest = score;
					prefs.putInt("HighScore", scoreBest);

					newHighScore = true;
				} else
					newHighScore = false;
			} else {
				Floor.theFloor.update();
				Bird.theBird.update();
				Pipe.pipe1.update();
				Pipe.pipe2.update();
				Pipe.pipe3.update();
			}
			break;
		case FALL:
			if (Bird.theBird.collide(Floor.theFloor)) {
				state = State.SCOREBOARDSCROLL;
				Sound.playSound("sfx_swooshing.wav");
				decideMedal(score);
				if (score > scoreBest) {
					scoreBest = score;
					prefs.putInt("HighScore", scoreBest);
					newHighScore = true;
				} else
					newHighScore = false;
			} else {
				Bird.theBird.update();
			}
			break;
		case PAUSE:
			Bird.theBird.updateFlap();
			Floor.theFloor.update();
			break;
		case SCOREBOARDSCROLL:
			Scoreboard.scoreBoard.update();
			break;
		case SCORECOUNT:
			scoreTimer++;
			if (scoreTimer >= 30 / (score + 1)) {
				scoreTimer = 0;
				scoreCounter++;
			}
			if (scoreCounter >= score) {
				scoreCounter = score;
				state = State.SCORE;
			}
			break;
		default:
			break;
		}
		if (flash > 0)
			flash--;
	}

	public void decideMedal(int score) {
		if (score >= 10)
			medal = Medal.BRONZE;
		if (score >= 20)
			medal = Medal.SILVER;
		if (score >= 30)
			medal = Medal.GOLD;
		if (score >= 40)
			medal = Medal.PLATINUM;
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			// Triple buffering
			createBufferStrategy(3);
			return;
		}

		game.clear();

		Sprite.background.render(game, 0, 0);
		Pipe.pipe1.render(game);
		Pipe.pipe2.render(game);
		Pipe.pipe3.render(game);
		Floor.theFloor.render(game);
		Bird.theBird.render(game);
		Scoreboard.scoreBoard.render(game);
		if (state == State.PAUSE) {
			Sprite.tap.render(game, 60, 103);
			Sprite.title.render(game, 23, 50);
		}
		if (state == State.PLAY)
			Text.displayLargeText(game, score, 70, 10);
		if (state == State.SCOREBOARDSCROLL || state == State.SCORECOUNT
				|| state == State.SCORE)
			Sprite.gameOver.render(game, 24, 65);
		if (state == State.SCORE)
			Sprite.okay.render(game, 51, 165);
		if (state == State.SCORECOUNT || state == State.SCORE) {
			Text.displayLargeText(game, scoreCounter, 109, 116);
			Text.displayLargeText(game, scoreBest, 109, 136);
			if (newHighScore)
				Sprite.newHigh.render(game, 82, 130);
			switch (medal) {
			case BRONZE:
				Sprite.medalBronze.render(game, 28, 122);
				break;
			case SILVER:
				Sprite.medalSilver.render(game, 28, 122);
				break;
			case GOLD:
				Sprite.medalGold.render(game, 28, 122);
				break;
			case PLATINUM:
				Sprite.medalPlatinum.render(game, 28, 122);
				break;
			default:
				break;
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(new Color(0xFF, 0xFF, 0xFF, flash * 255 / 50));
		g.fillRect(0, 0, width, height); // White flash
		g.dispose();
		bs.show(); // Swap frames
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
