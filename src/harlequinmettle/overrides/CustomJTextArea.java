package harlequinmettle.overrides;

import java.awt.Color;

import javax.swing.JTextArea;

public class CustomJTextArea extends JTextArea {
	public CustomJTextArea(){
		super();
		setBackground(new Color(165,225,210));
	}
	public CustomJTextArea(Color c){
		super();
		setBackground(c);
	}
}
