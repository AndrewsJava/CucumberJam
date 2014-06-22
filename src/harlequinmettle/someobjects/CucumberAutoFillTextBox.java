package harlequinmettle.someobjects;

import harlequinmettle.interfaces.IPrefiller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

public class CucumberAutoFillTextBox  {

	private SuggestionPanel suggestion;
	private JTextArea textarea;

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
		final int position = textarea.getCaretPosition();
		Point location;
		try {
			location = textarea.modelToView(position).getLocation();
		} catch(BadLocationException e2) {
			e2.printStackTrace();
			return;
		}
		String text = textarea.getText();
		int start = Math.max(0, position - 1);
		while(start > 0) {
			if(true || !Character.isWhitespace(text.charAt(start))) {
				start--;
			} else {
				start++;
				break;
			}
		}
		if(start > position) {
			return;
		}
		final String subWord = text.substring(start, position);
		if(subWord.length() < 2) {
		//	return;
		}
		suggestion = new SuggestionPanel(textarea,   location);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textarea.requestFocusInWindow();
			}
		});
	}

	private void hideSuggestion() {
		if(suggestion != null) {
			suggestion.popupMenu.setVisible(false);
		}
		suggestion = null;
	}

	protected void initUI() {
		final JFrame frame = new JFrame();
		frame.setTitle("Test frame on two screens");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());
		textarea = new JTextArea(24, 80);
		textarea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		textarea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
		 
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DOWN && suggestion != null) {
					suggestion.moveDown();
				} else if(e.getKeyCode() == KeyEvent.VK_UP && suggestion != null) {
					suggestion.moveUp();
				} else if(Character.isLetterOrDigit(e.getKeyChar())) {
					showSuggestionLater();
				} else if(Character.isWhitespace(e.getKeyChar())) {
					//hideSuggestion();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		panel.add(textarea, BorderLayout.CENTER);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
public CucumberAutoFillTextBox(){
	//textarea = this;
	
}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(InstantiationException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new CucumberAutoFillTextBox().initUI();
			}
		});
	}

}