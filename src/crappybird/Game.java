package crappybird;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import crappybird.controls.Mouse;
import crappybird.entity.Bird;
import crappybird.entity.Floor;
import crappybird.entity.Pipe;
import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;
import crappybird.sound.Sound;

public class Game extends Canvas implements Runnable {

	public enum State {
		PLAY, PAUSE, FALL;
	}

	private static final long serialVersionUID = 1L;
	private Screen game;
	private JFrame frame;
	private boolean running = false;
	private Thread thread;
	private Mouse mouse;
	private int flash = 0;
	public static int score = 0;

	public static final int scale = 2;
	public static final int width = 143 * scale;
	public static final int height = 255 * scale;

	public static State state = State.PLAY;

	private BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();

	public static String title = "CrappyBird";

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		game = new Screen(pixels, width, height);
		frame = new JFrame();
		mouse = new Mouse();
		addMouseListener(mouse);

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
			//Score Detection
			if(Bird.theBird.x + Sprite.birdMid.w/2>=Pipe.pipe1.x&&Pipe.pipe1.passed==false){
				Pipe.pipe1.passed=true;
				score++;
				Sound.playSound("res\\sfx_point.wav");
			}
			if(Bird.theBird.x + Sprite.birdMid.w/2>=Pipe.pipe2.x&&Pipe.pipe2.passed==false){
				Pipe.pipe2.passed=true;
				score++;
				Sound.playSound("res\\sfx_point.wav");
			}
			if(Bird.theBird.x + Sprite.birdMid.w/2>=Pipe.pipe3.x&&Pipe.pipe3.passed==false){
				Pipe.pipe3.passed=true;
				score++;
				Sound.playSound("res\\sfx_point.wav");
			}
			//Collision Detection
			if (Bird.theBird.collide(Pipe.pipe1)
					|| Bird.theBird.collide(Pipe.pipe2)
					|| Bird.theBird.collide(Pipe.pipe3)) {
				Sound.playSound("res\\sfx_hit.wav");
				Sound.playSound("res\\sfx_die.wav");
				state = State.FALL;
				flash = 50;
				System.out.println("Game Over! You scored: " + score);
			}
			else if(Bird.theBird.collide(Floor.theFloor)){
				Sound.playSound("res\\sfx_hit.wav");
				Sound.playSound("res\\sfx_die.wav");
				state = State.PAUSE;
				flash = 50;
				System.out.println("Game Over! You scored: " + score);
			}
			else {
				Floor.theFloor.update();
				Bird.theBird.update();
				Pipe.pipe1.update();
				Pipe.pipe2.update();
				Pipe.pipe3.update();
			}
			break;
		case FALL:
			if(Bird.theBird.collide(Floor.theFloor)){
				state = State.PAUSE;
			}
			else {
				Bird.theBird.update();
			}
			break;
		case PAUSE:
			break;
		default:
			break;		
		}
		if (flash > 0) flash--;
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

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(new Color(0xFF, 0xFF, 0xFF, flash*255/50));
		g.fillRect(0, 0, width, height);
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
