package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;

public class ColourIndicatorPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JPanel panel = new JPanel();

	public ColourIndicatorPanel() {
		setTitle("Fake Hue Bulb");
		jbInit();
	}

	private void jbInit() {
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.WHITE);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNoteColoursAre = new JLabel("Note colours are not accurate to what the bulbs would be");
		panel.add(lblNoteColoursAre);
		setSize(300, 400);

		setVisible(true);
	}
}
