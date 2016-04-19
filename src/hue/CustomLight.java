package hue;

import com.philips.lighting.model.PHLight;

public class CustomLight extends PHLight {

	public CustomLight(PHLight light) {
		super(light);
	}

	/**
	 * Do not change unless you want to change how the light selector in the main
	 * screen works.
	 */
	@Override
	public String toString() {
		return this.getIdentifier() + " - " + this.getName();
	}

}
