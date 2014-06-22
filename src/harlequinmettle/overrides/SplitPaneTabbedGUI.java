package harlequinmettle.overrides;

import harlequinmettle.calican.Cuker;
import harlequinmettle.someobjects.SuggestionPanel;
import harlequinmettle.utilities.Maker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class SplitPaneTabbedGUI extends JPanel {

	// in general two panes
	final JSplitPane share = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	public final JTabbedPane tabbedPane = new JTabbedPane();
	public final LinkedHashMap<String, JScrollPane> PANES = new LinkedHashMap<String, JScrollPane>();

	public final JTextArea textLower = new JTextArea("\n\n\n\n\n\n\n\n\n\n");
	public final ConcurrentLinkedDeque<String> TEXT_HISTORY = new ConcurrentLinkedDeque<String>();
	public final ConcurrentLinkedDeque<String> TEXT_FUTURE = new ConcurrentLinkedDeque<String>();
	public Thread saveHistory;
	final JScrollPane textScroll = Maker.makeTextScroll(textLower);
	final JPanel southPanel = new JPanel();
	SuggestionPanel suggestion;
	private JPanel controllsOption;
	public static boolean revising = false;
	public static long saveInterval = 200;

	public SplitPaneTabbedGUI() {
		this.setLayout(new GridLayout(1, 0));
		this.setUpGUI();
		textLower.addKeyListener(revisionMonitor);
		defineStartSaveHistory();
	}

	// would like to add to all subcomponents to for focus independent undo/redo
	// capabilities
	public KeyListener revisionMonitor = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_Z)
					&& ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
				undo();
			} else if ((e.getKeyCode() == KeyEvent.VK_Y)
					&& ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
				redo();
			} else {
				revising = false;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	};

	public void redo() {
		revising = true;
		if (!TEXT_FUTURE.isEmpty()) {
			String restoreText = TEXT_FUTURE.pop();
			TEXT_HISTORY.push(restoreText);
			textLower.setText(restoreText);
			// gui.TEXT_HISTORY.tailMap(fromKey)
		} 
	}

	public void undo() {

	 revising = true;
		if (!TEXT_HISTORY.isEmpty()) {
			String lastText = TEXT_HISTORY.pop();
			TEXT_FUTURE.push(lastText);
			textLower.setText(lastText);
			// gui.TEXT_HISTORY.tailMap(fromKey)
		} 

	}

	private void defineStartSaveHistory() {
		saveHistory = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					while (revising) {
						sleep(10);
					}
					String text = textLower.getText();
					if (!TEXT_HISTORY.contains(text)) {
						TEXT_HISTORY.push(textLower.getText());
						System.out.println(textLower.getText().replaceAll("\\s"," "));
					}

					sleep(saveInterval);
				}
			}

			// private long getInverseTime() {
			// return Long.MAX_VALUE-System.currentTimeMillis();
			// }

			private void sleep(long time) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
				}
			}

		});
		saveHistory.start();
	}

	public void addControlPanel(JPanel controlls) {
		controllsOption = controlls;
		southPanel.add(controllsOption, BorderLayout.PAGE_START);
		// this.repaint();
	}

	private void setUpGUI() {
		// add split pane to application frame
		this.add(share);
		southPanel.setPreferredSize(Cuker.APP_SIZE);
		southPanel.setLayout(new BorderLayout());
		southPanel.add(textScroll, BorderLayout.CENTER);
		if (controllsOption != null)
			southPanel.add(controllsOption, BorderLayout.PAGE_START);
		// add scroll text to bottom pane
		share.add(southPanel, JSplitPane.BOTTOM);
		share.setDividerLocation(180);
		// add tabbed pane to upper pane
		share.add(tabbedPane, JSplitPane.TOP);
		updateTabs();
		initUI();

	}

	// control tabs by adding/removing jsrollpanes from arraylist
	public void updateTabs() {
		tabbedPane.removeAll();
		for (Entry<String, JScrollPane> ent : PANES.entrySet()) {
			tabbedPane.add(ent.getKey(), ent.getValue());
		}
	}

	// /////////////////////////////////////0000000000000000
	// ////////////////////////////////////-------------------------logic for
	// suggestion panel
	protected void showSuggestionLater() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showSuggestion();
			}

		});
	}

	protected void showSuggestion() {
		hideSuggestion();
		final int position = textLower.getCaretPosition();
		Point location;
		try {
			location = textLower.modelToView(position).getLocation();
		} catch (BadLocationException e2) {
			e2.printStackTrace();
			return;
		}
		// ///////////----------------text
		String text = getTextForFiltering(textLower.getText());

		int start = Math.max(0, position - 1);
		while (start > 0) {
			if (true || !Character.isWhitespace(text.charAt(start))) {
				start--;
			} else {
				start++;
				break;
			}
		}
		if (start > position) {
			return;
		}
		final String subWord = "huh";// text.substring(start, position);
		if (subWord.length() < 2) {
			// return;
		}
		suggestion = new SuggestionPanel(textLower, location);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textLower.requestFocusInWindow();
			}
		});
	}

	// NEEDS CONFIRMATION OF FUNCTION
	private String getTextForFiltering(String text) {
		String[] textLine = text.split("\n");
		text = textLine[textLine.length - 1].trim().toLowerCase();
		if (text.startsWith("and"))
			text = text.replaceFirst("and", "");
		if (text.startsWith("given"))
			text = text.replaceFirst("given", "");
		if (text.startsWith("when"))
			text = text.replaceFirst("when", "");
		if (text.startsWith("then"))
			text = text.replaceFirst("then", "");
		return text.trim();
	}

	private void hideSuggestion() {
		if (suggestion != null) {
			suggestion.popupMenu.setVisible(false);
		}
		suggestion = null;
	}

	protected void initUI() {

		textLower.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		textLower.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (true) {
					if (e.getKeyCode() == KeyEvent.VK_DOWN
							&& suggestion != null) {
						suggestion.moveDown();
					} else if (e.getKeyCode() == KeyEvent.VK_UP
							&& suggestion != null) {
						suggestion.moveUp();
					} else if (Character.isLetterOrDigit(e.getKeyChar())) {
						showSuggestionLater();
					} else if (Character.isWhitespace(e.getKeyChar())) {
						// hideSuggestion();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

	}

}
