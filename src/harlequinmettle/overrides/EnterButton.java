package harlequinmettle.overrides;

import harlequinmettle.calican.Cuker;
import harlequinmettle.someobjects.InViewItem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class EnterButton extends JButton {
	public static final Font BUTTON_FONT = new Font("sans serif", 20, 20);
	public int totalCount = 0;
	public int useCount = 0;

	private InViewItem calabashItem = null;

	public InViewItem getCalabashItem() {
		return calabashItem;
	}

	public void setCalabashItem(InViewItem calabashItem) {
		this.calabashItem = calabashItem;
	}

	public EnterButton(String name,boolean isRevisor) {
		super(name); 
		
		super.registerKeyboardAction(super.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
				JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(super.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
				JComponent.WHEN_FOCUSED);

		super.setFont(BUTTON_FONT);
		addDefaultHistoryRestartListener(isRevisor);
	}

	public EnterButton(String name) {
		super(name); 
		
		super.registerKeyboardAction(super.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
				JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(super.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
				JComponent.WHEN_FOCUSED);

		super.setFont(BUTTON_FONT);
		addDefaultHistoryRestartListener(false);
	}

	private void addDefaultHistoryRestartListener(final boolean isRev) {
	addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SplitPaneTabbedGUI.revising = isRev;
		}
		
	});
	}

	public String getCalabashTitle() {
		String calTitle = "<html>" + useCount + "          (" + totalCount
				+ ")   ";
		calTitle += calabashItem.getFullClassName() + "<br>";
		calTitle += "ID:   " + calabashItem.retrieveId() + "<br>";
		calTitle += "TXT: " + calabashItem.retrieveText();

		return calTitle += "</html>";

	}

}