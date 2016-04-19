
package hue;

import java.util.List;

import main.HueProperties;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

public class BridgeControllor {

	private PHHueSDK phHueSDK;

	public BridgeControllor() {
		this.phHueSDK = PHHueSDK.getInstance();
	}

	public void findBridges() {
		phHueSDK = PHHueSDK.getInstance();
		PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		sm.search(true, true);
	}

	private PHSDKListener listener = new PHSDKListener() {

		@Override
		public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
			System.out.println("onAccessPointsFound");
			PHHueSDK phHueSDK = PHHueSDK.getInstance();
			if (accessPointsList.size() >= 1) {
				if (!phHueSDK.isAccessPointConnected(accessPointsList.get(0))) {
					phHueSDK.connect(accessPointsList.get(0));
				}
			} else {
				System.out.println("No Bridges Found");
			}
		}

		@Override
		public void onAuthenticationRequired(PHAccessPoint accessPoint) {
			// Start the Pushlink Authentication.

			System.out.println("onAuthenticationRequired");
			phHueSDK.startPushlinkAuthentication(accessPoint);
			int timeoutCounter = 0;
			while (!PHHueSDK.getInstance().isAccessPointConnected(BridgeControllor.getAccessPoint())) {
				// THis should be the wait for the button press
				System.out.println("Please press the button on the the bridge");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timeoutCounter++;
				if (timeoutCounter == 60) {// Should be 60 seconds (give or
											// take)
					System.out.println("Button not pressed. Quitting");
					System.exit(0);
				}
			}
		}

		@Override
		public void onBridgeConnected(PHBridge bridge, String username) {
			phHueSDK.setSelectedBridge(bridge);
			phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
			String lastIpAddress = bridge.getResourceCache().getBridgeConfiguration().getIpAddress();
			HueProperties.storeUsername(username);
			HueProperties.storeLastIPAddress(lastIpAddress);
			HueProperties.saveProperties();
			// Update the GUI.
			System.out.println("last ip: " + lastIpAddress);
			System.out.println("last user: " + username);
			// Close the PushLink dialog (if it is showing).

		}

		@Override
		public void onCacheUpdated(List<Integer> arg0, PHBridge arg1) {
		}

		@Override
		public void onConnectionLost(PHAccessPoint arg0) {
		}

		@Override
		public void onConnectionResumed(PHBridge arg0) {
		}

		@Override
		public void onError(int code, final String message) {

			if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
			} else if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
			} else if (code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
			} else if (code == PHMessageType.BRIDGE_NOT_FOUND) {
			}
			System.out.println(code + ":" + message);
		}

		@Override
		public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
			for (PHHueParsingError parsingError : parsingErrorsList) {
				System.out.println(" hue.BridgeControllor.listener.new PHSDKListener() {...}.onParsingErrors(List<PHHueParsingError>)  ParsingError : " + parsingError.getMessage());
			}
		}
	};

	public PHSDKListener getListener() {
		return listener;
	}

	/**
	 * Connect to the last known access point. This method is triggered by the
	 * Connect to Bridge button but it can equally be used to automatically
	 * connect to a bridge.
	 * 
	 */
	public boolean connectToLastKnownAccessPoint() {
		String username = HueProperties.getUsername();
		String lastIpAddress = HueProperties.getLastConnectedIP();

		if (username == null || lastIpAddress == null) {
			return false;
		}
		PHAccessPoint accessPoint = getAccessPoint(username, lastIpAddress);
		phHueSDK.connect(accessPoint);
		return true;
	}

	public static PHAccessPoint getAccessPoint() {
		String username = HueProperties.getUsername();
		String lastIpAddress = HueProperties.getLastConnectedIP();
		return getAccessPoint(username, lastIpAddress);
	}

	public static PHAccessPoint getAccessPoint(String username, String lastIpAddress) {
		PHAccessPoint accessPoint = new PHAccessPoint();
		accessPoint.setIpAddress(lastIpAddress);
		accessPoint.setUsername(username);
		return accessPoint;
	}

	public static boolean isConnectedToAccessPoint() {
		try {
			return PHHueSDK.getInstance().isAccessPointConnected(BridgeControllor.getAccessPoint());
		} catch (Exception e) {
			return false;
		}
	}
}
