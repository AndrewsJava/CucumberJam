package applications;

import harlequinmettle.overrides.EnterButton;
import harlequinmettle.overrides.JScrollPanelledPane;
import harlequinmettle.overrides.SplitPaneTabbedGUI;
import harlequinmettle.utilities.Maker;

import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class MirrorGUI {
	public MirrorGUI() {
		SplitPaneTabbedGUI backupGUI = new SplitPaneTabbedGUI();

		EnterButton eb = new EnterButton("Backup Tab"); 

		JScrollPanelledPane jspx = Maker.makeScrollPane();
		jspx.add(Maker.generatePanel(eb));
		backupGUI.PANES.put("first tab", jspx);
		backupGUI.updateTabs();
	}

	public static void main(String arg[]) {
		SplitPaneTabbedGUI backupGUI = new SplitPaneTabbedGUI();

		EnterButton eb = new EnterButton("Backup Tab"); 

		JScrollPanelledPane jspx = Maker.makeScrollPane();
		jspx.add(Maker.generatePanel(eb));
		backupGUI.PANES.put("first tab", jspx);
		backupGUI.updateTabs();

	}
}
