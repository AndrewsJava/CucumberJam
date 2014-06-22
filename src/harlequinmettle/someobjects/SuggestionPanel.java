package harlequinmettle.someobjects;

import harlequinmettle.calican.Cuker;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class SuggestionPanel {
	private JList<String> list;
	public JPopupMenu popupMenu;
	JTextArea forWriting;

	public SuggestionPanel(JTextArea writeTo, Point location) {
		forWriting = writeTo;
		popupMenu = new JPopupMenu();
		JScrollPane scroller = new JScrollPane();
		popupMenu.removeAll();
		popupMenu.setOpaque(false);
		popupMenu.setBorder(null);

		scroller.setViewportView(list = createSuggestionList());

		popupMenu.add(scroller);
		popupMenu.show(writeTo, location.x, writeTo.getBaseline(0, 0)
				+ location.y);
	}

	public void hide() {
		popupMenu.setVisible(false);
		// if(suggestion == this) {
		// suggestion = null;
		// }
	}

	private JList<String> createSuggestionList() {
		// String[] data = IPrefiller.PREFILLS;
		String filterBy = getMostRecentTextTyped(6);
		Vector<String> filtered = getFilteredList(filterBy);
		JList<String> list = new JList<String>(filtered);
		list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				insertSelection();
				hide();
			}
		});
		list.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == e.VK_ENTER) {

					insertSelection();
				}
			}

		});
		return list;
	}

	private Vector<String> getFilteredList(String filterBy) {
		Vector<String> filteredList = new Vector<String>();
		for (String s : Cuker.programSettings.STEPS.keySet()) {
			if (s.toLowerCase().contains(filterBy.toLowerCase()))
				filteredList.add(s);
		}
		return filteredList;
	}

	private String getMostRecentTextTyped(int lookBack) {

		String text = Cuker.gui.textLower.getText();
		int minCaret = Cuker.gui.textLower.getCaretPosition() - lookBack < 0 ? 0
				: Cuker.gui.textLower.getCaretPosition() - lookBack;
		text = text.substring(minCaret, Cuker.gui.textLower.getCaretPosition());
		text = text.replaceAll("And", "").replaceAll("Given", "")
				.replaceAll("When", "").replaceAll("Then", "").trim();
		return text;
	}

	private JList<String> createSuggestionList(String[] data) {
		// String[] data = IPrefiller.PREFILLS;

		JList<String> list = new JList<String>(data);
		list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				insertSelection();
				hide();
			}
		});
		list.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == e.VK_ENTER) {

					insertSelection();
				}
			}

		});
		return list;
	}

	public boolean insertSelection() {
		if (list.getSelectedValue() != null) {
			forWriting.select(getLineStart(forWriting),
					forWriting.getCaretPosition());
			forWriting.cut();
			final String selectedSuggestion = ((String) list.getSelectedValue());
			// textarea.getDocument().insertString(insertionPosition,
			// selectedSuggestion, null);

			forWriting.insert("\n   " + selectedSuggestion + "   #"+Cuker.programSettings.LINE_NUMBERS.get(selectedSuggestion)+"\n\t",
					forWriting.getCaretPosition());
			if(Cuker.programSettings.generateStepsWithSuggestionBox){
				 
						for (String step : Cuker.programSettings.STEPS.get(selectedSuggestion)) {
							if (Cuker.programSettings.commentOutSteps) {
								Cuker.gui.textLower.insert("\n\t\t " + step,
										Cuker.gui.textLower.getCaretPosition());
							} else {
								Cuker.gui.textLower.insert(
										"\n\t\t" + step.replaceAll("#", ""),
										Cuker.gui.textLower.getCaretPosition());
							}
						}
			}
			return true;

			// hideSuggestion();
		}
		return false;
	}

	private int getLineStart(JTextArea forWrite) {
		return forWrite.getText()
				.indexOf("\n", forWrite.getCaretPosition() - 7);
	}

	public void moveUp() {
		int index = Math.min(list.getSelectedIndex() - 1, 0);
		selectIndex(index);
	}

	public void moveDown() {
		int index = Math.min(list.getSelectedIndex() + 1, list.getModel()
				.getSize() - 1);
		selectIndex(index);
	}

	private void selectIndex(int index) {
		final int position = forWriting.getCaretPosition();
		list.setSelectedIndex(index);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				forWriting.setCaretPosition(position);
			};
		});
	}
	// ////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////
}
