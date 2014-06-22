package harlequinmettle.overrides;

import java.awt.Color;

import javax.swing.JLabel;

public class CustomJLabel extends JLabel {

	public CustomJLabel(String title) {
		super(title); 
		setOpaque(true);
		setBackground(new Color(100, 200, 250));
	}
	public CustomJLabel(String title, Color c) {
		super(title); 
		setOpaque(true);
		setBackground(c);
	}
}
