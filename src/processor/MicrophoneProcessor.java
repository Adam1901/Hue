package processor;

import hue.HueControllor;
import hue.LightControllor;

import java.awt.AWTException;

import javax.sound.sampled.*;

import main.StartStopFrame;

import com.philips.lighting.model.PHLightState;
import com.philips.lighting.model.PHLight.PHLightEffectMode;

/**
 * VERY beta
 * 
 * @author Adam
 *
 */
public class MicrophoneProcessor implements Processor {
	public static boolean threadStarted = false;
	private static boolean updateLight = true;

	// Sorry
	private static boolean onlyEverDoOnce = false;

	private TargetDataLine inputLine;
	private AudioFormat format;
	private float sampleRate;
	private int sampleSizeBits;
	private int channels;
	private int waitTime;
	private int bufferSize;
	private int windowSize = 512;
	private static PlayThread pThread;
	private static boolean playing = false;
	private static boolean connected = false;

	// Class that reads a signal from Line-in source and sends that signal
	// to either a recorder module or the signal-viewing pipeline
	public class PlayThread extends Thread {

		byte[] buffer = new byte[bufferSize];

		public void run() {
			threadStarted = true;
			while (true) {
				while (!HueControllor.end && HueControllor.loop) {
					try {
						sleep(waitTime);
						inputLine.read(buffer, 0, bufferSize);
						if (isConnected()) {
							while (!isPlaying() && !HueControllor.end && HueControllor.loop && isConnected())
								sleep(100);
							int max = 0;
							for (int i = 0; i < buffer.length; i++) {
								if (Math.abs(buffer[i]) > max)
									max = Math.abs(buffer[i]);
							}
							System.out.println("Max: " + max);

							updateLightColour(max);
							double sleep = StartStopFrame.getSleepValue();
							if (sleep > 0.0) {
								int slp = (int) (sleep * 1000.0);
								try {
									Thread.sleep(slp);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void updateLightColour(int max) throws InterruptedException {
			if (updateLight) {
				PHLightState lightState = new PHLightState();
				lightState.setEffectMode(PHLightEffectMode.EFFECT_COLORLOOP);
				lightState.setBrightness(max * 2);
				LightControllor.changeLights(lightState, null);
			}
		}
	}

	public MicrophoneProcessor() {
		sampleRate = 44100;
		sampleSizeBits = 16;
		channels = 2;
		bufferSize = (sampleSizeBits / 8) * channels * windowSize;
		waitTime = (int) ((((1000f / sampleRate) / (float) sampleSizeBits) / 2f) * 8f * (float) bufferSize);
	}

	private TargetDataLine getTargetDataLine() {
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			for (Mixer.Info mi : AudioSystem.getMixerInfo()) {
				TargetDataLine dataline = null;
				try {
					Mixer mixer = AudioSystem.getMixer(mi);
					dataline = (TargetDataLine) mixer.getLine(info);
					dataline.open(format);
					dataline.start();
					return dataline;
				} catch (Exception e) {
				}
				if (dataline != null)
					try {
						dataline.close();
					} catch (Exception e) {
					}
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void startProcessing() throws AWTException, InterruptedException {

		format = new AudioFormat(sampleRate, sampleSizeBits, channels, true, true);

		// Obtain and open the lines.
		if (!onlyEverDoOnce) {
			pThread = new PlayThread();
			inputLine = getTargetDataLine();
			onlyEverDoOnce = true;
		}
		setConnected(true);
		setPlaying(true);
		if (!threadStarted) {
			pThread.start();
		}
	}

	public static boolean isPlaying() {
		return playing;
	}

	public static void setPlaying(boolean playing) {
		MicrophoneProcessor.playing = playing;
	}

	public static boolean isConnected() {
		return connected;
	}

	public static void setConnected(boolean connected) {
		MicrophoneProcessor.connected = connected;
	}
}