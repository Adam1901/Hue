package hue;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import main.HueProperties;
import main.StartStopFrame;
import main.StartStopFrame.MODE;
import processor.AudioProcessor;
import processor.ImageProcessor;
import processor.MicrophoneProcessor;
import processor.ProcessorIF;
import processor.ProfileProcessor;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;

public class HueControllor implements Runnable, HueInterface {

	public static boolean loop = false;
	public static boolean end = false;
	@SuppressWarnings("unused")
	private boolean connected = false;

	public void startHue() throws InterruptedException {
		if (!StartStopFrame.fakeIt) {
			PHHueSDK.create();

			HueProperties.loadProperties();

			BridgeControllor c = new BridgeControllor();
			c.findBridges();

			PHHueSDK.getInstance().getNotificationManager().registerSDKListener(c.getListener());

			try {
				c.connectToLastKnownAccessPoint();
			} catch (Exception e) {
				System.out.println(e);
			}
			int timeoutCounter = 0;
			while (!PHHueSDK.getInstance().isAccessPointConnected(BridgeControllor.getAccessPoint())) {
				// THis should be the wait for the button press
				System.out.println("Please press the button on the the bridge1");
				Thread.sleep(1000);
				timeoutCounter++;
				if (timeoutCounter == 60) {// Should be 60 seconds (give or
											// take)
					System.out.println("Button not pressed. Quitting");
					System.exit(0);
				}
			}
		}
		connected = true;

		System.out.println("Ready to start");
		ProcessorIF proc;
		ProcessorIF micproc = new MicrophoneProcessor();
		while (!end) {
			while (!loop && !end) {
				Thread.sleep(100);
			}

			System.out.println("No longer waiting");
			try {
				if (!end) {
					int i = 0;
					while (loop && !end) {
						i++;
						long start = new Date().getTime();
						switch (StartStopFrame.getActiveMode()) {
							case VISUAL :
								proc = new ImageProcessor();
								proc.startProcessing();
								break;
							case SYSTEMAUDIO :
								proc = new AudioProcessor();
								proc.startProcessing();
								break;
							case MICROPHONE :
								micproc.startProcessing();
								Thread.sleep(1000);
								break;
							case PROFILE :
								proc = new ProfileProcessor();
								proc.startProcessing();
								break;
							default :
								Thread.sleep(10000);
								break;
						}
						long end = new Date().getTime();
						System.out.println(i + "" + (start - end));
						// Set the microphone off. 
						MicrophoneProcessor.setConnected(false);
						MicrophoneProcessor.setPlaying(false);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				// In case something bad happens
			}
		}

		LightControllor.setToReading();
		destory();
	}

	public static void destory() {
		PHHueSDK.getInstance().destroySDK();
		System.out.println("Ended safely");
		System.exit(0);
	}

	public void run() {
		System.out.println("run");
		try {
			startHue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PHLight> getAllLights() {
		if (StartStopFrame.fakeIt) {
			return Arrays.asList(new PHLight("Test", "1", "1", "test"));
		} else {
			return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getAllLights();
		}
	}

}