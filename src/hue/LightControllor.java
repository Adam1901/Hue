package hue;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.model.PHLight.PHLightEffectMode;
import com.philips.lighting.model.PHLight.PHLightType;

import main.ColourIndicatorPanel;
import main.StartStopFrame;

public class LightControllor {
	public static final int MAX_BRIGHTNESS = 254;
	public static final int HALF_BRIGHTNESS = 254 / 2;
	public static final int MAX_HUE = 65535;

	public static PHLightState changeLights(PHLightState lightState, Color colour) {
		for (CustomLight light : StartStopFrame.getSelectedLights()) {
			changeBulbState(light, lightState, null, colour);
		}
		return lightState;
	}

	public static PHLightState changeOneLight(PHLight light, PHLightState lightState) {
		changeBulbState(light, lightState, null, null);
		return lightState;
	}
	// Colour light LCT007
	// Strips LST001
	// WHite LWB006
	/**
	 * @param colour
	 * @param sleep
	 *            in seconds
	 * @return
	 */
	public static PHLightState changeColour(Color colour, double sleep) {
		PHLightState lightState = new PHLightState();
		try {
			for (CustomLight light : StartStopFrame.getSelectedLights()) {
				boolean isFullColour = PHLightType.CT_COLOR_LIGHT.equals(light.getLightType());
				setLightStateColour(lightState, colour, light);

				float[] hsv = new float[3];
				Color.RGBtoHSB(colour.getRed(), colour.getGreen(), colour.getBlue(), hsv);

				float rawBrightnessMod = (float) StartStopFrame.getBrightnessModifier();
				float brightnessStripMod = (float) StartStopFrame.getBrightnessStripMod();
				float effBrightMod = isFullColour ? rawBrightnessMod : brightnessStripMod;
				float brightnessModifier = effBrightMod / 100.0f;

				int brightness;
				if (!isFullColour) {
					brightness = (int) (hsv[2] * MAX_BRIGHTNESS);
					brightness = (int) (brightness * brightnessModifier);
				} else {
					brightness = (int) (MAX_BRIGHTNESS * brightnessModifier);
				}

				if (brightness <= 0) {
					if (light.getLastKnownLightState().isOn()) {
						lightState.setOn(false);
					}
					lightState.setBrightness(0);
				} else {
					lightState.setOn(true);
					lightState.setBrightness(brightness - 1);
				}

				changeBulbState(light, lightState, null, colour);

				// Seems to be a bug with the SDK, if you send more than 2
				// updates at the same time.
				// It will ignore the diff in the seprate brightness mods/
				Thread.sleep(5);
			}
			sleep(sleep);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lightState;
	}

	public static void setLightStateColour(PHLightState lightState, Color colour, CustomLight light) {
		float[] xy;
		if (StartStopFrame.isInvertColour()) {
			Color invertImage = invertImage(colour);
			xy = PHUtilities.calculateXYFromRGB(invertImage.getRed(), invertImage.getGreen(), invertImage.getBlue(), light.getModelNumber());
		} else {
			xy = PHUtilities.calculateXYFromRGB(colour.getRed(), colour.getGreen(), colour.getBlue(), light.getModelNumber());
		}
		lightState.setX(xy[0]);
		lightState.setY(xy[1]);
		lightState.setEffectMode(PHLightEffectMode.EFFECT_NONE);
	}

	private static void sleep(double sleep) {
		if (sleep > 0.0) {
			int slp = (int) (sleep * 1000.0);
			try {
				Thread.sleep(slp);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setToReading() {
		PHLightState ph = new PHLightState();
		ph.setX(0.4449F);
		ph.setY(0.4066F);
		ph.setOn(true);
		ph.setEffectMode(PHLightEffectMode.EFFECT_NONE);
		float brightnessModifier = (((float) (StartStopFrame.getBrightnessModifier())) / 100);
		float effBrightness = (float) ((float) MAX_BRIGHTNESS * brightnessModifier);
		if (effBrightness <= 0.0)
			effBrightness = 1;
		ph.setBrightness((int) effBrightness);
		changeLights(ph, Color.WHITE);
	}

	private static Color invertImage(Color input) {
		return new Color(255 - input.getRed(), 255 - input.getGreen(), 255 - input.getBlue());
	}

	private static void changeBulbState(PHLight light, PHLightState lightState, PHLightListener listener, Color colour) {
		if (StartStopFrame.fakeIt && colour != null) {
			ColourIndicatorPanel.panel.setBackground(colour);
		} else {
			PHHueSDK.getInstance().getSelectedBridge().updateLightState(light, lightState, listener);
		}
	}

	/**
	 * Flash the lights to indicate something
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	private static void sendNotification() throws InterruptedException {
		List<CustomLight> lights = StartStopFrame.getSelectedLights();
		Map<CustomLight, PHLightState> ll = new HashMap<CustomLight, PHLightState>();
		for (CustomLight light : lights) {
			ll.put(light, light.getLastKnownLightState());
		}

		for (int i = 0; i < 2; i++) {
			for (Entry<CustomLight, PHLightState> light : ll.entrySet()) {
				PHLightState lastKnownLightState = new PHLightState(light.getValue());
				lastKnownLightState.setBrightness(MAX_BRIGHTNESS / 2);
				changeOneLight(light.getKey(), lastKnownLightState);
			}
			Thread.sleep(500);
			for (Entry<CustomLight, PHLightState> light : ll.entrySet()) {
				PHLightState lastKnownLightState = new PHLightState(light.getValue());
				lastKnownLightState.setBrightness(MAX_BRIGHTNESS / 2);
				changeOneLight(light.getKey(), lastKnownLightState);
			}
			Thread.sleep(500);
		}
		for (Entry<CustomLight, PHLightState> light : ll.entrySet()) {
			PHLightState lastKnownLightState = new PHLightState(light.getValue());
			changeOneLight(light.getKey(), lastKnownLightState);
		}
	}
}
