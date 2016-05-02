package processor;

import hue.CustomLight;
import hue.LightControllor;

import java.awt.AWTException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import main.StartStopFrame;
import panels.ProfilePanel;

public class ProfileProcessor implements ProcessorIF {

	@Override
	public void startProcessing() throws AWTException, InterruptedException {
		switch (ProfilePanel.getActiveProfile()) {
			case XMAS :
				xmas();
				break;
			case SOUNDOFDAPOLICE :
				soundOfDaPolice();
				break;
			case INASUB :
				inASub();
				break;
			case CUSTOMCOLOUR :
				customColours();
				break;
			case FIREPLACE :
				fireplace();
				break;
			case LAVALAMP :
				lavaLamp();
				break;
			default :
				Thread.sleep(1000);
				break;
		}
		Thread.sleep(10); // Safety
	}

	private void lavaLamp() {
		PHLightState lightState = new PHLightState();
		int transitionTime = 100;
		lightState.setTransitionTime(transitionTime);

		LightControllor.setLightStateColour(lightState, Color.RED, new CustomLight(new PHLight(null, null, null, null)));
		LightControllor.changeLights(lightState, Color.RED);
		sleep(transitionTime * 100);

		LightControllor.setLightStateColour(lightState, Color.BLUE, new CustomLight(new PHLight(null, null, null, null)));
		LightControllor.changeLights(lightState, Color.BLUE);
		sleep(transitionTime * 100);
	}

	private void fireplace() {
		PHLightState lightState = new PHLightState();
		for (CustomLight customLight : StartStopFrame.getSelectedLights()) {
			int bright = randomeInt(100, LightControllor.MAX_BRIGHTNESS);
			lightState.setBrightness(bright);
			LightControllor.setLightStateColour(lightState, Color.RED, customLight);
			LightControllor.changeLights(lightState, Color.RED);
			sleep(randomeInt(10, 500));
			bright = randomeInt(100, LightControllor.MAX_BRIGHTNESS);
			lightState.setBrightness(bright);
			LightControllor.setLightStateColour(lightState, Color.ORANGE, customLight);
			LightControllor.changeLights(lightState, Color.ORANGE);
		}
	}

	private int randomeInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	private void sleep(int bright) {
		try {
			Thread.sleep(bright);
		} catch (InterruptedException e) {
		}
	}

	private void customColours() {
		List<Color> customColours = new ArrayList<Color>(ProfilePanel.getCustomColours());
		for (Color color : customColours) {
			LightControllor.changeColour(color, StartStopFrame.getSleepValue() * 2.0);
		}
	}

	private void xmas() {
		LightControllor.changeColour(Color.RED, StartStopFrame.getSleepValue());
		LightControllor.changeColour(Color.GREEN, StartStopFrame.getSleepValue());
	}

	private void soundOfDaPolice() {
		LightControllor.changeColour(Color.RED, StartStopFrame.getSleepValue());
		LightControllor.changeColour(Color.BLUE, StartStopFrame.getSleepValue());
	}

	private void inASub() {
		LightControllor.changeColour(Color.RED, StartStopFrame.getSleepValue());
		LightControllor.changeColour(Color.BLACK, StartStopFrame.getSleepValue());
	}
}
