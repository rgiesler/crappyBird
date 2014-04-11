package crappybird.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	public static synchronized Thread playSound(final String path) {
		Thread soundThread = new Thread() {
			@Override
			public void run() {
				try {
					Clip clip = null;
					AudioInputStream inputStream = null;
					clip = AudioSystem.getClip();
					InputStream bufferedIn = new BufferedInputStream(
							Sound.class.getResourceAsStream("/" + path));
					inputStream = AudioSystem.getAudioInputStream(bufferedIn);

					clip.open(inputStream);
					long durationInMiliSeconds = clip.getMicrosecondLength() / 1000;
					clip.start();
					Thread.sleep(durationInMiliSeconds);
					while (true) {
						if (!clip.isActive()) {
							break;
						}
						long fPos = (long) (clip.getMicrosecondPosition() / 1000);
						long left = durationInMiliSeconds - fPos;
						if (left > 0)
							Thread.sleep(left);
					}
					clip.stop();
					clip.close();
					inputStream.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		};
		soundThread.setDaemon(true);
		soundThread.start();
		return soundThread;
	}
}
