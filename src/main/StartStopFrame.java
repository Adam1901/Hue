package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import com.philips.lighting.model.PHLight;

import panels.AudioPanel;
import panels.MicPanel;
import panels.ProfilePanel;
import panels.VisualPanel;
import hue.BridgeControllor;
import hue.CustomLight;
import hue.HueControllor;
import hue.HueInterface;
import hue.LightControllor;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartStopFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private SpinnerNumberModel snm = new SpinnerNumberModel(0.3, 0.0, 100.0, 0.1);
	private JSpinner spinner = new JSpinner(snm);

	private DefaultListModel<CustomLight> model = new DefaultListModel<CustomLight>();
	private JList<CustomLight> list = new JList<CustomLight>(model);
	private static double sleepValue = 0.3;
	private static List<CustomLight> selectedLights = new ArrayList<CustomLight>();
	private static int brightnessMod = 100;
	private static int brightnessStripMod = 100;
	private static MODE activeMode = MODE.VISUAL;
	private static boolean invertColour = false;

	/**
	 * TODO turn into a launch arg or something
	 * 
	 * However it controls weather you want it to control the on screen window
	 * (in a hacked in sort of way) or control the actual lights
	 */
	public static boolean fakeIt = true;

	public static enum MODE {
		SYSTEMAUDIO, VISUAL, MICROPHONE, PROFILE
	};

	public static void main(String[] args) throws InterruptedException {
		new StartStopFrame();
	}

	public StartStopFrame() throws InterruptedException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent paramWindowEvent) {
				HueControllor.destory();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final HueControllor hue = new HueControllor();
		new Thread(hue).start();
		HueInterface hueiface = hue;

		if (fakeIt)
			new ColourIndicatorPanel();

		setAlwaysOnTop(true);
		setTitle("Hue Colour Temp");
		setSize(450, 721);
		setLocationRelativeTo(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 5, 0};
		gridBagLayout.rowHeights = new int[]{5, 0, 0, 50, 18, 0, 0, 5, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		JButton btnCustomColour = new JButton("Custom Colour");
		btnCustomColour.addActionListener(arg0 -> {
			setAlwaysOnTop(false);
			Color color = JColorChooser.showDialog(null, "Select a colour", Color.WHITE);
			if (color != null) {
				LightControllor.changeColour(color, 0);
			}
			setAlwaysOnTop(true);
		});

		JButton btnReadingScene = new JButton("Set to Reading");
		btnReadingScene.addActionListener(paramActionEvent -> LightControllor.setToReading());
		GridBagConstraints gbc_btnReadingScene = new GridBagConstraints();
		gbc_btnReadingScene.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnReadingScene.insets = new Insets(0, 0, 5, 5);
		gbc_btnReadingScene.gridx = 1;
		gbc_btnReadingScene.gridy = 1;
		getContentPane().add(btnReadingScene, gbc_btnReadingScene);
		GridBagConstraints gbc_btnCustomColour = new GridBagConstraints();
		gbc_btnCustomColour.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCustomColour.insets = new Insets(0, 0, 5, 5);
		gbc_btnCustomColour.gridx = 2;
		gbc_btnCustomColour.gridy = 1;
		getContentPane().add(btnCustomColour, gbc_btnCustomColour);

		final JLabel lblDynamic = new JLabel("Off");
		GridBagConstraints gbc_lblDynamic = new GridBagConstraints();
		gbc_lblDynamic.anchor = GridBagConstraints.WEST;
		gbc_lblDynamic.insets = new Insets(0, 0, 5, 5);
		gbc_lblDynamic.gridx = 2;
		gbc_lblDynamic.gridy = 8;
		getContentPane().add(lblDynamic, gbc_lblDynamic);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(paramActionEvent -> {
			setSelectedLights(getSelectedLightsFromCombo());
			HueControllor.loop = false;
			lblDynamic.setText("Stopped");
		});

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(paramActionEvent -> {
			setSelectedLights(getSelectedLightsFromCombo());
			HueControllor.loop = true;
			lblDynamic.setText("Started");
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.gridwidth = 2;
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 2;
		getContentPane().add(btnStart, gbc_btnStart);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(scrollPane, gbc_scrollPane);
		list.addListSelectionListener(arg0 -> setSelectedLights(getSelectedLightsFromCombo()));
		scrollPane.setViewportView(list);

		JPanel pnlSelect = new JPanel();
		GridBagConstraints gbc_pnlSelect = new GridBagConstraints();
		gbc_pnlSelect.gridwidth = 2;
		gbc_pnlSelect.insets = new Insets(0, 0, 5, 5);
		gbc_pnlSelect.fill = GridBagConstraints.BOTH;
		gbc_pnlSelect.gridx = 1;
		gbc_pnlSelect.gridy = 4;
		getContentPane().add(pnlSelect, gbc_pnlSelect);
		pnlSelect.setBorder(BorderFactory.createTitledBorder("Global Settings"));

		ButtonGroup bg5 = new ButtonGroup();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 5;
		getContentPane().add(tabbedPane, gbc_tabbedPane);

		tabbedPane.addTab("Visual", null, new VisualPanel(this), null);
		tabbedPane.addTab("Profiles", null, new ProfilePanel(this), null);
		tabbedPane.addTab("Microphone", null, new MicPanel(this), null);
		tabbedPane.addTab("Auido", null, new AudioPanel(this), null);

		if (BridgeControllor.isConnectedToAccessPoint()) {
			fillCombo(hueiface);
		} else {
			System.out.println("Not connected yet");
			Thread.sleep(1000);
			if (BridgeControllor.isConnectedToAccessPoint()) {
				fillCombo(hueiface);
			} else {
				Thread.sleep(10000);
				fillCombo(hueiface);
			}
		}

		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.gridwidth = 2;
		gbc_btnStop.fill = GridBagConstraints.BOTH;
		gbc_btnStop.insets = new Insets(0, 0, 5, 5);
		gbc_btnStop.gridx = 1;
		gbc_btnStop.gridy = 6;
		getContentPane().add(btnStop, gbc_btnStop);
		GridBagLayout gbl_pnlSelect = new GridBagLayout();
		gbl_pnlSelect.columnWidths = new int[]{20, 95, 0, 0};
		gbl_pnlSelect.rowHeights = new int[]{23, 20, 2, 14, 31, 14, 31, 0};
		gbl_pnlSelect.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_pnlSelect.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlSelect.setLayout(gbl_pnlSelect);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 3;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		pnlSelect.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		final JRadioButton rdbtnVisual = new JRadioButton("Visual");
		GridBagConstraints gbc_rdbtnVisual = new GridBagConstraints();
		gbc_rdbtnVisual.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnVisual.gridx = 0;
		gbc_rdbtnVisual.gridy = 0;
		panel.add(rdbtnVisual, gbc_rdbtnVisual);
		rdbtnVisual.addChangeListener(arg0 -> {
			if (rdbtnVisual.isSelected())
				activeMode = MODE.VISUAL;
		});
		rdbtnVisual.setSelected(true);
		bg5.add(rdbtnVisual);

		final JRadioButton rdbtnProfiles = new JRadioButton("Profiles");
		GridBagConstraints gbc_rdbtnProfiles = new GridBagConstraints();
		gbc_rdbtnProfiles.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnProfiles.gridx = 1;
		gbc_rdbtnProfiles.gridy = 0;
		panel.add(rdbtnProfiles, gbc_rdbtnProfiles);
		rdbtnProfiles.addChangeListener(arg0 -> {
			if (rdbtnProfiles.isSelected())
				activeMode = MODE.PROFILE;
		});
		bg5.add(rdbtnProfiles);

		final JRadioButton rdbtnMicrophone = new JRadioButton("Microphone");
		GridBagConstraints gbc_rdbtnMicrophone = new GridBagConstraints();
		gbc_rdbtnMicrophone.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnMicrophone.gridx = 2;
		gbc_rdbtnMicrophone.gridy = 0;
		panel.add(rdbtnMicrophone, gbc_rdbtnMicrophone);
		rdbtnMicrophone.addChangeListener(e -> {
			if (rdbtnMicrophone.isSelected())
				activeMode = MODE.MICROPHONE;
		});
		bg5.add(rdbtnMicrophone);

		final JRadioButton rdbtnAuido = new JRadioButton("Auido");
		GridBagConstraints gbc_rdbtnAuido = new GridBagConstraints();
		gbc_rdbtnAuido.gridx = 3;
		gbc_rdbtnAuido.gridy = 0;
		panel.add(rdbtnAuido, gbc_rdbtnAuido);
		rdbtnAuido.addChangeListener(e -> {
			if (rdbtnAuido.isSelected())
				activeMode = MODE.SYSTEMAUDIO;
		});

		rdbtnAuido.setEnabled(false);
		bg5.add(rdbtnAuido);

		JLabel lblDelay = new JLabel("Delay:");
		GridBagConstraints gbc_lblDelay = new GridBagConstraints();
		gbc_lblDelay.anchor = GridBagConstraints.EAST;
		gbc_lblDelay.insets = new Insets(0, 0, 5, 5);
		gbc_lblDelay.gridx = 0;
		gbc_lblDelay.gridy = 1;
		pnlSelect.add(lblDelay, gbc_lblDelay);
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 1;
		pnlSelect.add(spinner, gbc_spinner);
		spinner.addChangeListener(paramChangeEvent -> sleepValue = (double) spinner.getValue());

		JCheckBox chckbxInvertColour = new JCheckBox("Invert Colour");
		GridBagConstraints gbc_chckbxInvertColour = new GridBagConstraints();
		gbc_chckbxInvertColour.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxInvertColour.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxInvertColour.gridx = 2;
		gbc_chckbxInvertColour.gridy = 1;
		pnlSelect.add(chckbxInvertColour, gbc_chckbxInvertColour);
		chckbxInvertColour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxInvertColour.isSelected())
					invertColour = true;
				else
					invertColour = false;
			}
		});

		JSeparator sep = new JSeparator();
		sep.setSize(100, 100);
		GridBagConstraints gbc_sep = new GridBagConstraints();
		gbc_sep.fill = GridBagConstraints.BOTH;
		gbc_sep.insets = new Insets(0, 0, 5, 5);
		gbc_sep.gridwidth = 3;
		gbc_sep.gridx = 0;
		gbc_sep.gridy = 2;
		pnlSelect.add(sep, gbc_sep);

		JLabel lblBrightness = new JLabel("Bulb Brightness");
		GridBagConstraints gbc_lblBrightness = new GridBagConstraints();
		gbc_lblBrightness.anchor = GridBagConstraints.WEST;
		gbc_lblBrightness.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrightness.gridx = 0;
		gbc_lblBrightness.gridy = 3;
		pnlSelect.add(lblBrightness, gbc_lblBrightness);

		final JSlider stripSlider = new JSlider();
		stripSlider.addChangeListener(arg0 -> brightnessStripMod = stripSlider.getValue());

		final JSlider mainSlider = new JSlider();
		mainSlider.addChangeListener(arg0 -> brightnessMod = mainSlider.getValue());
		mainSlider.setValue(100);
		mainSlider.setSnapToTicks(true);
		mainSlider.setPaintTicks(true);
		mainSlider.setMinorTickSpacing(5);
		mainSlider.setMinimum(1);
		mainSlider.setMajorTickSpacing(10);
		GridBagConstraints gbc_mainSlider = new GridBagConstraints();
		gbc_mainSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_mainSlider.insets = new Insets(0, 0, 5, 0);
		gbc_mainSlider.gridwidth = 3;
		gbc_mainSlider.gridx = 0;
		gbc_mainSlider.gridy = 4;
		pnlSelect.add(mainSlider, gbc_mainSlider);

		JLabel lblStripBrightness = new JLabel("Other Hue Brightness");
		GridBagConstraints gbc_lblStripBrightness = new GridBagConstraints();
		gbc_lblStripBrightness.anchor = GridBagConstraints.WEST;
		gbc_lblStripBrightness.insets = new Insets(0, 0, 5, 5);
		gbc_lblStripBrightness.gridx = 0;
		gbc_lblStripBrightness.gridy = 5;
		pnlSelect.add(lblStripBrightness, gbc_lblStripBrightness);
		stripSlider.setMinorTickSpacing(5);
		stripSlider.setMajorTickSpacing(10);
		stripSlider.setValue(100);
		stripSlider.setMinimum(1);
		stripSlider.setSnapToTicks(true);
		stripSlider.setPaintTicks(true);
		GridBagConstraints gbc_stripSlider = new GridBagConstraints();
		gbc_stripSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_stripSlider.gridwidth = 3;
		gbc_stripSlider.gridx = 0;
		gbc_stripSlider.gridy = 6;
		pnlSelect.add(stripSlider, gbc_stripSlider);

		JButton btnCloseSafely = new JButton("Close Safely");
		btnCloseSafely.addActionListener(arg0 -> {
			setSelectedLights(getSelectedLightsFromCombo());
			HueControllor.end = true;
		});

		GridBagConstraints gbc_btnCloseSafely = new GridBagConstraints();
		gbc_btnCloseSafely.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseSafely.gridwidth = 2;
		gbc_btnCloseSafely.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCloseSafely.gridx = 1;
		gbc_btnCloseSafely.gridy = 7;
		getContentPane().add(btnCloseSafely, gbc_btnCloseSafely);

		JLabel lblStatus = new JLabel("Status:");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.EAST;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 8;
		getContentPane().add(lblStatus, gbc_lblStatus);

		setVisible(true);
	}

	private void fillCombo(HueInterface hueiface) {
		List<PHLight> allLights = hueiface.getAllLights();

		for (PHLight rawLight : allLights) {
			CustomLight light = new CustomLight(rawLight);
			model.addElement(light);
		}
		if (allLights.size() >= 1) {
			int[] i = new int[3];
			i[0] = 0;
			i[1] = 3;
			list.setSelectedIndices(i);
		}
	}

	private List<CustomLight> getSelectedLightsFromCombo() {
		List<CustomLight> lights = new ArrayList<CustomLight>();
		int[] selectedIndices = list.getSelectedIndices();
		for (int i = 0; i < selectedIndices.length; i++) {
			lights.add(list.getModel().getElementAt(selectedIndices[i]));
		}
		setSelectedLights(lights);
		return lights;
	}

	public static int getBrightnessModifier() {
		return brightnessMod == 1 ? 0 : brightnessMod;
	}

	public static int getBrightnessStripMod() {
		return brightnessStripMod == 1 ? 0 : brightnessStripMod;
	}

	public static List<CustomLight> getSelectedLights() {
		return selectedLights;
	}

	public static void setSelectedLights(List<CustomLight> lightIds) {
		StartStopFrame.selectedLights = lightIds;
	}

	public static double getSleepValue() {
		return sleepValue;
	}

	public static MODE getActiveMode() {
		return activeMode;
	}

	public static boolean isInvertColour() {
		return invertColour;
	}
}