package panels;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.StartStopFrame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class VisualPanel extends JPanel {

	private static boolean isFastEnabled = true;
	private static boolean avgColourProcessing = false;
	private static boolean singleFrameAnaysis = false;
	private static boolean singleMonitor = true;
	private static final long serialVersionUID = 1L;

	public VisualPanel(final StartStopFrame frame) {

		GridBagLayout gbl_pnlVisual = new GridBagLayout();
		gbl_pnlVisual.columnWidths = new int[]{0, 0};
		gbl_pnlVisual.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_pnlVisual.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlVisual.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_pnlVisual);

		ButtonGroup bg = new ButtonGroup();

		ButtonGroup bg2 = new ButtonGroup();

		ButtonGroup bg3 = new ButtonGroup();

		ButtonGroup bg4 = new ButtonGroup();

		JPanel pnlProcessing = new JPanel();
		pnlProcessing.setBorder(BorderFactory.createTitledBorder("Processing Speed"));
		GridBagConstraints gbc_pnlProcessing = new GridBagConstraints();
		gbc_pnlProcessing.insets = new Insets(0, 0, 5, 0);
		gbc_pnlProcessing.fill = GridBagConstraints.BOTH;
		gbc_pnlProcessing.gridx = 0;
		gbc_pnlProcessing.gridy = 0;
		add(pnlProcessing, gbc_pnlProcessing);
		GridBagLayout gbl_pnlProcessing = new GridBagLayout();
		gbl_pnlProcessing.columnWidths = new int[]{200, 0, 0};
		gbl_pnlProcessing.rowHeights = new int[]{0, 0};
		gbl_pnlProcessing.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlProcessing.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlProcessing.setLayout(gbl_pnlProcessing);
		final JRadioButton rdbtnAccProc = new JRadioButton("Accurate Processing");
		GridBagConstraints gbc_rdbtnAccProc = new GridBagConstraints();
		gbc_rdbtnAccProc.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAccProc.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnAccProc.gridx = 0;
		gbc_rdbtnAccProc.gridy = 0;
		pnlProcessing.add(rdbtnAccProc, gbc_rdbtnAccProc);
		rdbtnAccProc.addActionListener(paramActionEvent -> {
			if (rdbtnAccProc.isSelected())
				isFastEnabled = false;
		});

		rdbtnAccProc.setSelected(true);
		bg.add(rdbtnAccProc);

		final JRadioButton rdbtnFastProc = new JRadioButton("Fast Processing");
		GridBagConstraints gbc_rdbtnFastProc = new GridBagConstraints();
		gbc_rdbtnFastProc.anchor = GridBagConstraints.WEST;
		gbc_rdbtnFastProc.gridx = 1;
		gbc_rdbtnFastProc.gridy = 0;
		pnlProcessing.add(rdbtnFastProc, gbc_rdbtnFastProc);
		rdbtnFastProc.addActionListener(paramActionEvent -> {
			if (rdbtnFastProc.isSelected())
				isFastEnabled = true;
		});
		bg.add(rdbtnFastProc);

		JPanel pnlColourProc = new JPanel();
		GridBagConstraints gbc_pnlColourProc = new GridBagConstraints();
		gbc_pnlColourProc.insets = new Insets(0, 0, 5, 0);
		gbc_pnlColourProc.fill = GridBagConstraints.BOTH;
		gbc_pnlColourProc.gridx = 0;
		gbc_pnlColourProc.gridy = 1;
		pnlColourProc.setBorder(BorderFactory.createTitledBorder("Colour Processing"));
		add(pnlColourProc, gbc_pnlColourProc);
		GridBagLayout gbl_pnlColourProc = new GridBagLayout();
		gbl_pnlColourProc.columnWidths = new int[]{200, 0, 0};
		gbl_pnlColourProc.rowHeights = new int[]{0, 0};
		gbl_pnlColourProc.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlColourProc.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlColourProc.setLayout(gbl_pnlColourProc);

		final JRadioButton rdbtnAmbientColour = new JRadioButton("Ambient Colour");
		GridBagConstraints gbc_rdbtnAmbientColour = new GridBagConstraints();
		gbc_rdbtnAmbientColour.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAmbientColour.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnAmbientColour.gridx = 0;
		gbc_rdbtnAmbientColour.gridy = 0;
		pnlColourProc.add(rdbtnAmbientColour, gbc_rdbtnAmbientColour);
		rdbtnAmbientColour.addChangeListener(paramChangeEvent -> {
			if (rdbtnAmbientColour.isSelected())
				avgColourProcessing = false;
		});

		rdbtnAmbientColour.setSelected(true);
		bg2.add(rdbtnAmbientColour);
		final JRadioButton rdbtnAverageColour = new JRadioButton("Average Colour");
		GridBagConstraints gbc_rdbtnAverageColour = new GridBagConstraints();
		gbc_rdbtnAverageColour.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAverageColour.gridx = 1;
		gbc_rdbtnAverageColour.gridy = 0;
		pnlColourProc.add(rdbtnAverageColour, gbc_rdbtnAverageColour);
		rdbtnAverageColour.addActionListener(paramActionEvent -> {
			if (rdbtnAverageColour.isSelected())
				avgColourProcessing = true;
		});
		bg2.add(rdbtnAverageColour);

		JPanel pnlFrameAnal = new JPanel();
		pnlFrameAnal.setBorder(BorderFactory.createTitledBorder("Frame Analysis"));
		GridBagConstraints gbc_pnlFrameAnal = new GridBagConstraints();
		gbc_pnlFrameAnal.insets = new Insets(0, 0, 5, 0);
		gbc_pnlFrameAnal.fill = GridBagConstraints.BOTH;
		gbc_pnlFrameAnal.gridx = 0;
		gbc_pnlFrameAnal.gridy = 2;
		add(pnlFrameAnal, gbc_pnlFrameAnal);
		GridBagLayout gbl_pnlFrameAnal = new GridBagLayout();
		gbl_pnlFrameAnal.columnWidths = new int[]{200, 0, 0};
		gbl_pnlFrameAnal.rowHeights = new int[]{0, 0};
		gbl_pnlFrameAnal.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlFrameAnal.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlFrameAnal.setLayout(gbl_pnlFrameAnal);

		final JRadioButton rdbtnSingleFrameAnaysis = new JRadioButton("Single Frame anaysis");
		GridBagConstraints gbc_rdbtnSingleFrameAnaysis = new GridBagConstraints();
		gbc_rdbtnSingleFrameAnaysis.anchor = GridBagConstraints.WEST;
		gbc_rdbtnSingleFrameAnaysis.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnSingleFrameAnaysis.gridx = 0;
		gbc_rdbtnSingleFrameAnaysis.gridy = 0;
		pnlFrameAnal.add(rdbtnSingleFrameAnaysis, gbc_rdbtnSingleFrameAnaysis);
		rdbtnSingleFrameAnaysis.addChangeListener(paramChangeEvent -> {
			if (rdbtnSingleFrameAnaysis.isSelected())
				singleFrameAnaysis = true;
		});
		rdbtnSingleFrameAnaysis.setSelected(true);
		bg3.add(rdbtnSingleFrameAnaysis);

		final JRadioButton rdbtnMultiFrameAnalysis = new JRadioButton("Multi Frame analysis");
		GridBagConstraints gbc_rdbtnMultiFrameAnalysis = new GridBagConstraints();
		gbc_rdbtnMultiFrameAnalysis.anchor = GridBagConstraints.WEST;
		gbc_rdbtnMultiFrameAnalysis.gridx = 1;
		gbc_rdbtnMultiFrameAnalysis.gridy = 0;
		pnlFrameAnal.add(rdbtnMultiFrameAnalysis, gbc_rdbtnMultiFrameAnalysis);
		rdbtnMultiFrameAnalysis.addChangeListener(paramChangeEvent -> {
			if (rdbtnMultiFrameAnalysis.isSelected())
				singleFrameAnaysis = false;
		});
		bg3.add(rdbtnMultiFrameAnalysis);

		JPanel pnlMonitorSelect = new JPanel();
		pnlMonitorSelect.setBorder(BorderFactory.createTitledBorder("Monitor Selector"));
		GridBagConstraints gbc_pnlMonitorSelect = new GridBagConstraints();
		gbc_pnlMonitorSelect.fill = GridBagConstraints.BOTH;
		gbc_pnlMonitorSelect.gridx = 0;
		gbc_pnlMonitorSelect.gridy = 3;
		add(pnlMonitorSelect, gbc_pnlMonitorSelect);
		GridBagLayout gbl_pnlMonitorSelect = new GridBagLayout();
		gbl_pnlMonitorSelect.columnWidths = new int[]{200, 0, 0};
		gbl_pnlMonitorSelect.rowHeights = new int[]{0, 0};
		gbl_pnlMonitorSelect.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlMonitorSelect.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlMonitorSelect.setLayout(gbl_pnlMonitorSelect);

		final JRadioButton rdbtnMainMonitorOnly = new JRadioButton("Main monitor only");
		GridBagConstraints gbc_rdbtnMainMonitorOnly = new GridBagConstraints();
		gbc_rdbtnMainMonitorOnly.anchor = GridBagConstraints.WEST;
		gbc_rdbtnMainMonitorOnly.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnMainMonitorOnly.gridx = 0;
		gbc_rdbtnMainMonitorOnly.gridy = 0;
		pnlMonitorSelect.add(rdbtnMainMonitorOnly, gbc_rdbtnMainMonitorOnly);
		rdbtnMainMonitorOnly.addChangeListener(paramChangeEvent -> {
			if (rdbtnMainMonitorOnly.isSelected())
				singleMonitor = true;
		});
		rdbtnMainMonitorOnly.setSelected(true);
		bg4.add(rdbtnMainMonitorOnly);

		final JRadioButton rdbtnAllMonitors = new JRadioButton("Use Other Monitors");
		GridBagConstraints gbc_rdbtnAllMonitors = new GridBagConstraints();
		gbc_rdbtnAllMonitors.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAllMonitors.gridx = 1;
		gbc_rdbtnAllMonitors.gridy = 0;
		pnlMonitorSelect.add(rdbtnAllMonitors, gbc_rdbtnAllMonitors);
		rdbtnAllMonitors.addChangeListener(paramChangeEvent -> {
			if (rdbtnAllMonitors.isSelected())
				singleMonitor = false;
		});
		bg4.add(rdbtnAllMonitors);

	}

	public static boolean isFastProcessing() {
		return isFastEnabled;
	}

	public static boolean isAverageColourProcessing() {
		return avgColourProcessing;
	}

	public static boolean getSingleFramAnalysis() {
		return singleFrameAnaysis;
	}

	public static boolean useSingleMonitor() {
		return singleMonitor;
	}
}
