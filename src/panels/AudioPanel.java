package panels;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import main.StartStopFrame;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AudioPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public AudioPanel(final StartStopFrame frame) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblAuidoNotYet = new JLabel("Auido not yet supported");
		GridBagConstraints gbc_lblAuidoNotYet = new GridBagConstraints();
		gbc_lblAuidoNotYet.insets = new Insets(0, 0, 5, 5);
		gbc_lblAuidoNotYet.gridx = 1;
		gbc_lblAuidoNotYet.gridy = 1;
		add(lblAuidoNotYet, gbc_lblAuidoNotYet);
	}

}
