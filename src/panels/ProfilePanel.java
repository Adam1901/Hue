package panels;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JButton;

import main.StartStopFrame;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

public class ProfilePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static enum PROFILE {
		XMAS, SOUNDOFDAPOLICE, INASUB, CUSTOMCOLOUR, FIREPLACE, TBC, LAVALAMP
	};

	private static PROFILE activeProfile = PROFILE.XMAS;

	private static List<Color> customColours = new ArrayList<Color>();

	public ProfilePanel(final StartStopFrame frame) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 5, 0};
		gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 0, 0, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JButton btnXmas = new JButton("X-Mas");
		btnXmas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setActiveProfile(PROFILE.XMAS);
			}
		});
		GridBagConstraints gbc_btnXmas = new GridBagConstraints();
		gbc_btnXmas.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnXmas.insets = new Insets(0, 0, 5, 5);
		gbc_btnXmas.gridx = 1;
		gbc_btnXmas.gridy = 1;
		add(btnXmas, gbc_btnXmas);

		final JButton btnSoundOfDa = new JButton("Sound Of Da Police");
		btnSoundOfDa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setActiveProfile(PROFILE.SOUNDOFDAPOLICE);
			}
		});
		GridBagConstraints gbc_btnSoundOfDa = new GridBagConstraints();
		gbc_btnSoundOfDa.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSoundOfDa.insets = new Insets(0, 0, 5, 5);
		gbc_btnSoundOfDa.gridx = 2;
		gbc_btnSoundOfDa.gridy = 1;
		add(btnSoundOfDa, gbc_btnSoundOfDa);

		final JButton btnWeAllLive = new JButton("We all live in a");
		btnWeAllLive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setActiveProfile(PROFILE.INASUB);
			}
		});
		GridBagConstraints gbc_btnWeAllLive = new GridBagConstraints();
		gbc_btnWeAllLive.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnWeAllLive.insets = new Insets(0, 0, 5, 5);
		gbc_btnWeAllLive.gridx = 1;
		gbc_btnWeAllLive.gridy = 2;
		add(btnWeAllLive, gbc_btnWeAllLive);

		JButton btnCustomColour = new JButton("Custom Colours");
		btnCustomColour.setToolTipText("All wait timers are multipled by 2");
		btnCustomColour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setAlwaysOnTop(false);
				String noOfColours = JOptionPane.showInputDialog("Enter a number of colours ");
				int numberOfCols = Integer.parseInt(noOfColours);

				for (int i = 0; i < numberOfCols; i++) {
					Color color = JColorChooser.showDialog(null, "Chose a Colour for colour: " + (i + 1), Color.WHITE);
					if (color == null)
						color = Color.WHITE;
					customColours.add(color);
				}
				setActiveProfile(PROFILE.CUSTOMCOLOUR);
				frame.setAlwaysOnTop(true);
			}
		});
		GridBagConstraints gbc_btnTbc = new GridBagConstraints();
		gbc_btnTbc.insets = new Insets(0, 0, 5, 5);
		gbc_btnTbc.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTbc.gridx = 2;
		gbc_btnTbc.gridy = 2;
		add(btnCustomColour, gbc_btnTbc);

		JButton button = new JButton("Fire place");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setActiveProfile(PROFILE.FIREPLACE);
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 3;
		add(button, gbc_button);

		JButton btnLavaLamp = new JButton("Lava Lamp");
		btnLavaLamp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setActiveProfile(PROFILE.LAVALAMP);
			}
		});
		GridBagConstraints gbc_btnLavaLamp = new GridBagConstraints();
		gbc_btnLavaLamp.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLavaLamp.insets = new Insets(0, 0, 5, 5);
		gbc_btnLavaLamp.gridx = 2;
		gbc_btnLavaLamp.gridy = 3;
		add(btnLavaLamp, gbc_btnLavaLamp);

		JButton button_3 = new JButton("TBC");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_3.insets = new Insets(0, 0, 5, 5);
		gbc_button_3.gridx = 1;
		gbc_button_3.gridy = 4;
		add(button_3, gbc_button_3);

		JButton button_5 = new JButton("TBC");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
			}
		});
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_5.insets = new Insets(0, 0, 5, 5);
		gbc_button_5.gridx = 2;
		gbc_button_5.gridy = 4;
		add(button_5, gbc_button_5);

		JButton button_4 = new JButton("TBC");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_4.insets = new Insets(0, 0, 5, 5);
		gbc_button_4.gridx = 1;
		gbc_button_4.gridy = 5;
		add(button_4, gbc_button_4);

		JButton button_6 = new JButton("TBC");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button_6 = new GridBagConstraints();
		gbc_button_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_6.insets = new Insets(0, 0, 5, 5);
		gbc_button_6.gridx = 2;
		gbc_button_6.gridy = 5;
		add(button_6, gbc_button_6);

		JButton button_7 = new JButton("TBC");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button_7 = new GridBagConstraints();
		gbc_button_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_7.insets = new Insets(0, 0, 5, 5);
		gbc_button_7.gridx = 1;
		gbc_button_7.gridy = 6;
		add(button_7, gbc_button_7);

		JButton button_2 = new JButton("TBC");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 6;
		add(button_2, gbc_button_2);
	}
	public static PROFILE getActiveProfile() {
		return activeProfile;
	}

	public static void setActiveProfile(PROFILE activeProfile) {
		ProfilePanel.activeProfile = activeProfile;
	}

	public static List<Color> getCustomColours() {
		return customColours;
	}

	public static void setCustomColours(List<Color> customColours) {
		ProfilePanel.customColours = customColours;
	}
}
