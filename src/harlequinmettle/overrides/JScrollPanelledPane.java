package harlequinmettle.overrides;

import harlequinmettle.calican.Cuker;
import harlequinmettle.utilities.Maker;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JScrollPanelledPane extends JScrollPane {
	public ArrayList<JComponent> parts = new ArrayList<JComponent>();
	public JPanel entireView = Maker.makePanel(Maker.VERTICAL);

	public JScrollPanelledPane() {
 
		setViewportView(entireView);
		// setPreferredSize(new Dimension(300, 300));
		getVerticalScrollBar().setUnitIncrement(32);
	}

	public void addComp(EnterButton b) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(b);
		parts.add(shell);
		updateParts();
	}

	public void addComp(JTextArea txt) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(txt);
		parts.add(shell);
		updateParts();
	}

	@Override
	public Component add(Component comp) {

		parts.add((JComponent) comp);
		updateParts();
		return super.add(comp);
	}

	public void addComp(JCheckBox box) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(box);
		parts.add(shell);
		updateParts();
	}

	public void addComp(JCheckBox box, JLabel brev) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(box);
		shell.add(brev);
		parts.add(shell);
		updateParts();
	}

	public void addComp(JComponent a, JComponent b) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(a);
		shell.add(b);
		parts.add(shell);
		updateParts();
	}

	public void addComp(JTextArea txt, EnterButton b) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(txt);
		shell.add(b);
		parts.add(shell);
		updateParts();
	}

	public void addComp(JLabel description, JTextArea txt, EnterButton b) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(description);
		shell.add(txt);
		shell.add(b);
		parts.add(shell);
		updateParts();
	}

	public void addComp(EnterButton a, EnterButton b) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(a);
		shell.add(b);
		parts.add(shell);
		updateParts();
	}

	public void addComp(EnterButton a, EnterButton b, EnterButton c) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		shell.add(a);
		shell.add(b);
		shell.add(c);
		parts.add(shell);
		updateParts();
	}

	private void updateParts() {
		entireView.removeAll();
		for (JComponent jp : parts) {
			entireView.add(jp);
		}
	}
}
