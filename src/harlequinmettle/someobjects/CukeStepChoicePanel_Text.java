package harlequinmettle.someobjects;
 
import harlequinmettle.overrides.EnterButton;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import applications.CukeSlicerUI;


public class CukeStepChoicePanel_Text extends JPanel {

 	final ArrayList<Component> stepBuilder = new ArrayList<Component>();
 	public static  ArrayList<JComboBox> choices_text = new ArrayList<JComboBox	>(); 
	public CukeStepChoicePanel_Text(String[] labels ) {
		//JTextArea input = new JTextArea(1, 5);
		JComboBox input = new JComboBox();
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (String text : labels) {
			JLabel pre = new JLabel(text);
			stepBuilder.add(pre);
			 // input = new JTextArea(1, 5);
			input = new JComboBox();
			input.setEditable(true);
		//	 patch(input);
			stepBuilder.add(input);
			this.add(pre);
			this.add(input);
			choices_text.add(input);
		}  
		 
		//remove last textarea
		this.remove(input);
		choices_text.remove(input);
		// CLEAR ALL JTEXTAREAS
		// SELECT ALL AND COPY TO CLIPBOARD
		EnterButton addStep = new EnterButton("addStep");
		addStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder stb = new StringBuilder("\n\t\t");
				for (int i = 0; i < stepBuilder.size(); i++) {
					if (i % 2 == 0) {
						stb.append(((JLabel) stepBuilder.get(i)).getText());
					} else {
						JComboBox recreate = (JComboBox) stepBuilder.get(i);
						if(recreate!=null && recreate.getSelectedItem()!=null )
						stb.append(recreate.getSelectedItem());
					}
				}
				CukeSlicerUI.jta.insert(stb.toString(),
						CukeSlicerUI.jta.getCaretPosition());
			}

		});
		this.add(addStep);
	}
 
	
    /**
     * Patch the behaviour of a component. 
     * TAB transfers focus to the next focusable component,
     * SHIFT+TAB transfers focus to the previous focusable component.
     * 
     * @param c	The component to be patched.
     */
    public static void patch(Component c) {
    	Set<KeyStroke> 
    	strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
    	c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
    	strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
    	c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }
}
