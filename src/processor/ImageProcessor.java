package processor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import hue.LightControllor;
import main.StartStopFrame;
import panels.VisualPanel;

public class ImageProcessor implements ProcessorIF {

	private BufferedImage caputreScreenShot() throws AWTException {
		if (VisualPanel.useSingleMonitor()) {
			return captureRawImage();
		} else {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] screens = ge.getScreenDevices();
			Rectangle allScreenBounds = new Rectangle();

			// As a back up in-case a NPE, therefore revert to raw image.
			try {
				// Ignore monitor 0
				for (int i = 1; i < screens.length; i++) {
					Rectangle screenBounds = screens[i].getDefaultConfiguration().getBounds();

					allScreenBounds.width += screenBounds.width;
					allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
				}

				Robot robot = new Robot();
				return robot.createScreenCapture(allScreenBounds);
			} catch (Exception ex) {
				return captureRawImage();
			}
		}
	}

	private BufferedImage captureRawImage() throws AWTException {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		return new Robot().createScreenCapture(screenRect);
	}

	private Color getRepresentativeColor() throws AWTException {

		BufferedImage img = calculateImg(caputreScreenShot());
		// scale down
		int width = img.getWidth() / 2;
		int height = img.getHeight() / 2;

		double totalBrightness = 0.0;
		int totalPixels = width * height;

		Map<Integer, SaturationCounter> hueToSat = new HashMap<>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				// get pixel colour
				Color col = new Color(img.getRGB(i, j));
				float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), new float[3]);
				float h = hsb[0];
				float s = hsb[1];
				float b = hsb[2];

				// update brightness tally
				totalBrightness += b;

				// Filter out grays and add to map
				if (isColourful(s, b)) {
					int numBuckets = 51;
					int hueBucket = ((int) (h * numBuckets + 128 / numBuckets)) * (255 / numBuckets);
					SaturationCounter sat = hueToSat.get(hueBucket);
					if (sat == null)
						sat = new SaturationCounter();
					sat.num++;
					sat.total += s;
					hueToSat.put(hueBucket, sat);
				}
			}
		}

		// find most common colour
		int winningHue = 0;
		SaturationCounter winningCounter = new SaturationCounter();
		for (Map.Entry<Integer, SaturationCounter> e : hueToSat.entrySet()) {
			int hue = e.getKey();
			SaturationCounter sc = e.getValue();
			if (sc.total > winningCounter.total) {
				winningCounter = sc;
				winningHue = hue;
			}
		}

		float hf = (float) winningHue / 255;
		float sf = (float) winningCounter.total / winningCounter.num;
		float avBrightness = (float) totalBrightness / totalPixels;
		sf = correctSaturationForExtremeBrightness(sf, avBrightness);

		return Color.getHSBColor(hf, sf, avBrightness);
	}

	private Color getAvgColourOfScreen() throws AWTException {
		BufferedImage image = calculateImg(caputreScreenShot());
		int height = image.getHeight();
		int width = image.getWidth();

		int totalPix = height * width;
		int totalRed = 0, totalBlue = 0, totalGreen = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				int[] rgbArr = getRGBArr(rgb);
				totalRed += rgbArr[0];
				totalGreen += rgbArr[1];
				totalBlue += rgbArr[2];
			}
		}
		int avgRed = (int) (totalRed / totalPix);
		int avgBlue = (int) (totalBlue / totalPix);
		int avgGreen = (int) (totalGreen / totalPix);

		return new Color(avgRed, avgGreen, avgBlue);
	}

	private BufferedImage calculateImg(BufferedImage capturedScreenShot) {
		BufferedImage image;
		if (VisualPanel.isFastProcessing()) {
			image = resizeImage(capturedScreenShot, BufferedImage.TYPE_INT_RGB);
		} else {
			image = capturedScreenShot;
		}
		return image;
	}

	private int[] getRGBArr(int pixel) {
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return new int[]{red, green, blue};
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type) {
		int width = originalImage.getWidth() / 2;
		int height = originalImage.getHeight() / 2;
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}

	private boolean isColourful(float saturation, float brightness) {
		return saturation > 0.1 && brightness > 0.1 && brightness < 0.95;
	}

	private float correctSaturationForExtremeBrightness(float s, float b) {
		float lowerThreshold = 0.2f;
		float upperThreshold = 0.8f;

		if (b < (lowerThreshold)) {
			s *= b / lowerThreshold;
		}

		if (b > upperThreshold) {
			s *= (1 - b) / (1 - upperThreshold);
		}
		return s;
	}

	private static class SaturationCounter {
		double total = 0.0;
		long num = 0;
	}

	private Color calculateColour(int sleep) throws AWTException, InterruptedException {
		Color colourToUse;
		if (VisualPanel.isAverageColourProcessing()) {
			colourToUse = getAvgColourOfScreen();
		} else {
			colourToUse = getRepresentativeColor();
		}
		// This is here for the multi-frame wait
		Thread.sleep(sleep);
		return colourToUse;
	}

	@Override
	public void startProcessing() throws AWTException, InterruptedException {

		Color colourToUse;
		if (VisualPanel.getSingleFramAnalysis()) {
			colourToUse = calculateColour(0);
		} else {
			Color temp1 = calculateColour(100);
			Color temp2 = calculateColour(100);
			Color temp3 = calculateColour(100);

			int avgRed = (temp1.getRed() + temp2.getRed() + temp3.getRed()) / 3;
			int avgGreen = (temp1.getGreen() + temp2.getGreen() + temp3.getGreen()) / 3;
			int avgBlue = (temp1.getBlue() + temp2.getBlue() + temp3.getBlue()) / 3;

			colourToUse = new Color(avgRed, avgGreen, avgBlue);
		}

		LightControllor.changeColour(colourToUse, StartStopFrame.getSleepValue());
	}
}
