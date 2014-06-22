package applications;

import harlequinmettle.interfaces.ICucumberGrammar;
import harlequinmettle.interfaces.IParserKeywords;
import harlequinmettle.interfaces.IPrefiller;
import harlequinmettle.overrides.EnterButton;
import harlequinmettle.someobjects.ColoredText;
import harlequinmettle.someobjects.CukeStepChoicePanel_Id;
import harlequinmettle.someobjects.CukeStepChoicePanel_Text;
import harlequinmettle.someobjects.CukeStepPanel;
import harlequinmettle.someobjects.InViewItem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;


public class CukeSlicerUI extends JFrame implements IParserKeywords,
		ICucumberGrammar, IPrefiller {

	public static JTextArea jta = new JTextArea("");
	static JTextArea dataText = new JTextArea("");
	private static ArrayList<String> visibleCalabashObjects_id = new ArrayList<String>();
	private static ArrayList<String> visibleCalabashObjects_text = new ArrayList<String>();
	private static ArrayList<String> visibleCalabashObjects_contentDescription = new ArrayList<String>();
	private static ArrayList<InViewItem> calabashObjects = new ArrayList<InViewItem>();
	// defining steps in macro
	static TreeMap<String, ArrayList<String>> definitions = new TreeMap<String, ArrayList<String>>();

	public static void main(String[] args) {
		   
		// System.out.println(Arrays.toString(fileList));
		CukeSlicerUI outline = new CukeSlicerUI();
		intitializeUI(outline);
ColoredText ct = new ColoredText();
		// CukeSlicerModel formats = new CukeSlicerModel();
		// /INSERTTEXT INTO TEXT ARE
		// /

	}

	public static void intitializeUI(JFrame outline) {
		outline.setTitle("Scenario Builder");
		outline.setSize(1130, 720);
		outline.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// in general two panes top control buttons/textfield bottom cucumber
		// text
		JSplitPane share = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		outline.add(share);

		// cucumber text
		JScrollPane textScroll = makeTextScroll();

		share.add(textScroll, JSplitPane.BOTTOM);
		share.setDividerLocation(180);
		JTabbedPane tabs = setUpTabs();
		share.add(tabs, JSplitPane.TOP);

		// top panel for controls layout
		initUI();
		// canvasViewer.add(companyOverview);
		outline.setVisible(true);

	}

	private static JScrollPane makeTextScroll() {
		JScrollPane textScroll = new JScrollPane();
		Font mFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		jta.setFont(mFont);
		jta.setLineWrap(false);

		textScroll.setViewportView(jta);
		textScroll.setPreferredSize(new Dimension(600, 300));
		textScroll.getVerticalScrollBar().setUnitIncrement(32);
		return textScroll;
	}

	private static JTabbedPane setUpTabs() {

		JTabbedPane tabbedPane = new JTabbedPane();

		// outline.getContentPane().add(tabbedPane);

		JScrollPane scen = genFileChoicePane();
		JScrollPane stepPane = genStepsPane();
		JScrollPane stepChoicePane_id = genChoiceStepsPane_id();
		JScrollPane stepChoicePane_text = genChoiceStepsPane_text();
		JScrollPane dataPane = genDataPane();
	
		tabbedPane.add("Steps", stepPane);
		tabbedPane.add("ChoiceSteps - id", stepChoicePane_id);
		tabbedPane.add("ChoiceSteps - text", stepChoicePane_text);
		
		tabbedPane.add("File", scen);
		tabbedPane.add("Data", dataPane);

		JScrollPane pathPane = genPathPane();
		tabbedPane.add("Path", pathPane);
		return tabbedPane;
	}

	private static void updateCalabashSteps(final JComboBox<String> macros,
			String path) {
		if(path==null)return;
		macros.removeAllItems();

		String[] stepsList = parseMacros(path);
		for (String def : stepsList) {
			macros.addItem(def);
		}

		macros.repaint();
	}

	private static JScrollPane genPathPane() {
		JScrollPane textScroll = new JScrollPane();
		final JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new GridLayout(0, 1));
		final JTextArea calaPath = new JTextArea(1, 32);
		String savedPath = "enter full path to: calabash_steps.rb";
		// C:\Users\\aparelius\svn_repo_tablet\FreeTablet\features\step_definitions
		try {
			savedPath = restorePath();
		} catch (Exception ex) {

		}
		calaPath.setText(savedPath);
		JComboBox<String> predef = new JComboBox<String>(PREFILLS);

		pathPanel.add(predef);
		predef.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String macroStatement = (String) ((JComboBox) arg0.getSource())
						.getSelectedItem();
				jta.insert("\n\t\t" + macroStatement, jta.getCaretPosition());

			}

		});

		String[] stepsList = parseMacros(savedPath);
		final JComboBox<String> macros = new JComboBox<String>(stepsList);
		pathPanel.add(macros);

		EnterButton savePath = new EnterButton("save path");
		savePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				memorizeObject(calaPath.getText(), "calabash_steps_path");

			}

		});

		EnterButton updateSteps = new EnterButton("update steps");
		updateSteps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				updateCalabashSteps(macros, calaPath.getText());
			}

		});
		// //////////////////////
		// add added ruby steps

		macros.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String macroStatement = (String) ((JComboBox) arg0.getSource())
						.getSelectedItem();
				jta.insert("\n\t\t" + macroStatement, jta.getCaretPosition());
				for (String step : definitions.get(macroStatement)) {
					jta.insert("\n\t\t" + "#" + step, jta.getCaretPosition());

				}
			}

		});

		// for(Entry<String,ArrayList<String>> ent: definitions.entrySet()){
		// System.out.println(ent.getKey());
		// for(String s : ent.getValue()){
		// System.out.println("      "+s);
		// }
		// }

		pathPanel.add(updateSteps);
		pathPanel.add(savePath);
		pathPanel.add(calaPath);

		textScroll.setViewportView(pathPanel);
		textScroll.setPreferredSize(new Dimension(600, 300));
		textScroll.getVerticalScrollBar().setUnitIncrement(32);
		return textScroll;
	}

	// /////////// ////////////////////////////
	@SuppressWarnings("unchecked")
	private static String restorePath() {
		String dataSet = "";
		try {
			FileInputStream filein = new FileInputStream("calabash_steps_path");
			ObjectInputStream objin = new ObjectInputStream(filein);
			try {
				dataSet = (String) objin.readObject();
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			}
			objin.close();
		} catch (Exception ioe) {
			ioe.printStackTrace();
			System.out.println("NO resume: saver");
		}
		return dataSet;
	} //

	public static void memorizeObject(Object ob, String obFileName) {
		System.out.println("memorizing object ... ");
		File nextFile = new File(obFileName);
		try {
			nextFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileOutputStream fileout = new FileOutputStream(obFileName);

			ObjectOutputStream objout = new ObjectOutputStream(fileout);
			objout.writeObject(ob);
			objout.flush();
			objout.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("UNABLE TO SAVE OBJECT TO: " + obFileName);
		}
		System.out.println("done memorizing object to: " + obFileName);
	}

	private static JScrollPane genDataPane() {
		JScrollPane textScroll = new JScrollPane();

		Font mFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		dataText.setFont(mFont);
		dataText.setLineWrap(false);
		textScroll.setViewportView(dataText);
		textScroll.setPreferredSize(new Dimension(600, 300));
		textScroll.getVerticalScrollBar().setUnitIncrement(32);
		return textScroll;
	}

	private static JScrollPane genFileChoicePane() {
		JPanel inputFields = new JPanel();
		inputFields.setLayout(new GridLayout(0, 1));
		JScrollPane controlsOutline = new JScrollPane();
		// add parts to inputFields
		EnterButton makeMacro = new EnterButton("convert to macro");
		makeMacro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String lines[] = jta.getText().split("\n");
				String newLines[] = new String[lines.length - 1];
				int pos = 0;
				for (String line : lines) {

					// Given (/^I log in$/) do
					if (line.contains("Scenario")) {
						line = line.replaceAll("Scenario:", "Given (/^");
						line = line + "$/) do";
						newLines[pos++] = line;
					} else if (line.contains("I")) {
						line = line.replaceAll("Then I", "macro 'I");
						line = line.replaceAll("And I", "macro 'I");
						line = line.replaceAll("When I", "macro 'I");
						line = line.replaceAll("Given I", "macro 'I");
						line = line += "'";
						newLines[pos++] = line;
						// macro 'I wait for "Username" to appear'
					}
				}
				jta.selectAll();
				jta.cut();
				for (String line : newLines) {
					jta.insert("\n\t\t" + line, jta.getCaretPosition());

				}

				jta.insert("\n\t\t" + "end", jta.getCaretPosition());
				jta.selectAll();
				jta.copy();
			}

		});
		inputFields.add(makeMacro);
		// ////////////////////
		String osDependencey = "" ;
				//browse for files
		String[] fileList = getNewTextFiles(new File(
				System.getProperty("user.dir")
						+ osDependencey));
		
		if(fileList== null){
			fileList = new String[1];
			fileList[0] = " no files found ";
		}
		JComboBox files = new JComboBox(fileList);
		inputFields.add(files);
		files.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String currentFileName = (String) ((JComboBox) arg0.getSource())
						.getSelectedItem();
				accessFile(currentFileName, dataText);
				for (JComboBox list : CukeStepChoicePanel_Id.choices_id) {
					list.removeAllItems();
					for (String option : visibleCalabashObjects_id) {
						list.addItem(option);
					}
				}
				for (JComboBox list : CukeStepChoicePanel_Text.choices_text) {
					list.removeAllItems();
					for (String option : visibleCalabashObjects_text) {
						list.addItem(option);
					}
				}
				buildCucumberVisibilityScenarios("Objects in "
						+ currentFileName, calabashObjects);
			}

		});

		// /////////////////////////
		controlsOutline.setViewportView(inputFields);
		controlsOutline.setPreferredSize(new Dimension(300, 300));
		controlsOutline.getVerticalScrollBar().setUnitIncrement(32);
		return controlsOutline;
	}

	private static String[] parseMacros(String pathX) {
		String[] paths = pathX.split(";");
		ArrayList<String> macros = new ArrayList<String>();
		for(String path: paths){
				String[] files = new File(path).list();
				if(files == null){
					String[] empty = {"   "};
					return empty;
				}
				for(String rubyFile : files) {
					File my_macros = new File(path + "\\" + rubyFile);

					BufferedReader br = null;
					InputStream is;
					try {
						is = new FileInputStream(my_macros);
						br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
						int counter = 0;
						String a;
						String def = "";
						while(( a = br.readLine() ) != null) {
							if(a.contains("(/^")||a.contains("/^")) {
								def = a.substring(a.indexOf("/^") + 2, a.indexOf("$/"));
								macros.add(def);
								if(definitions.containsKey(def)){
									System.out.println("\n\n\nDUPLICATE CALABASH DEFINITION\n****\n***\n***\n***"+def);
								}
								definitions.put(def, new ArrayList<String>());
							} else if(isValidStep(a)) {
								definitions.get(def).add("#" + a);
							}
							// / C:\Users\aparelius\svn_repo_tablet\FreeTablet\features\step_definitions
						}
						br.close();

					} catch(IOException e) {
						e.printStackTrace();
					}
				}
		}
				return macros.toArray(new String[macros.size()]);
			}


	private static boolean isValidStep(String x) {

		boolean isValid = true;
		String a = new String(x);
		a = a.replaceAll("end", "").trim();
		if (a.startsWith("#"))
			isValid = false;
		if (a.startsWith("require"))
			isValid = false;
		if (a.length() < 1)
			isValid = false;
		return isValid;
	}

	public static void buildCucumberVisibilityScenarios(String currentFileName,
			ArrayList<InViewItem> visibleCalabashObjects2) {

		jta.insert("\n\tScenario: " + currentFileName, jta.getCaretPosition());
		for (InViewItem vis : visibleCalabashObjects2) {
			jta.insert(
					"\n\n\t\t#" + vis.getFullClassName() + " : "
							+ vis.isEnabled(), jta.getCaretPosition());
			jta.insert("\n\t\t" + vis.getIdAssertion(), jta.getCaretPosition());
			jta.insert("\n\t\t" + vis.getTextAssertion(),
					jta.getCaretPosition());

		}

	}

	private static String[] getNewTextFiles(File dir) {
		return dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
	}

	//
	private static void addScenarioPanel(JPanel inputFields) {

		JPanel tag = new JPanel();
		tag.add(new JLabel("@"));
		final JTextArea tagText = new JTextArea(1, 12);
		CukeStepPanel.patch(tagText);
		tag.add(tagText);
		inputFields.add(tag);

		JPanel scenario = new JPanel();
		scenario.add(new JLabel("Scenario: "));
		final JTextArea description = new JTextArea(1, 40);
		CukeStepPanel.patch(description);
		scenario.add(description);
		EnterButton apply = new EnterButton("add");
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = "";
				if (tagText.getText().trim().length() > 0)
					text = "\n\t@" + tagText.getText();
				text += "\n\tScenario: " + description.getText();
				jta.insert(text, jta.getCaretPosition());

			}

		});

		scenario.add(description);
		scenario.add(apply);
		inputFields.add(scenario);
	}

	private static JScrollPane genStepsPane() {
		JPanel inputFields = new JPanel();
		inputFields.setLayout(new GridLayout(0, 1));
		JScrollPane controlsOutline = new JScrollPane();

		addScenarioPanel(inputFields);

		for (String[] stepOutline : ICucumberGrammar.COMMON_STEPS) {
			inputFields.add(new CukeStepPanel(stepOutline));
		}
		controlsOutline.setViewportView(inputFields);
		controlsOutline.setPreferredSize(new Dimension(300, 300));
		controlsOutline.getVerticalScrollBar().setUnitIncrement(32);
		return controlsOutline;
	}

	private static JScrollPane genChoiceStepsPane_id() {
		JPanel inputFields = new JPanel();
		inputFields.setLayout(new GridLayout(0, 1));
		JScrollPane controlsOutline = new JScrollPane();

		addScenarioPanel(inputFields);
		// add parts to inputFields
		for (String[] stepOutline : ICucumberGrammar.COMMON_STEPS) {
			inputFields.add(new CukeStepChoicePanel_Id(stepOutline));
		}
		controlsOutline.setViewportView(inputFields);
		controlsOutline.setPreferredSize(new Dimension(300, 300));
		controlsOutline.getVerticalScrollBar().setUnitIncrement(32);
		return controlsOutline;
	}

	private static JScrollPane genChoiceStepsPane_text() {
		JPanel inputFields = new JPanel();
		inputFields.setLayout(new GridLayout(0, 1));
		JScrollPane controlsOutline = new JScrollPane();

		addScenarioPanel(inputFields);
		// add parts to inputFields
		for (String[] stepOutline : ICucumberGrammar.COMMON_STEPS) {
			inputFields.add(new CukeStepChoicePanel_Text(stepOutline));
		}
		controlsOutline.setViewportView(inputFields);
		controlsOutline.setPreferredSize(new Dimension(300, 300));
		controlsOutline.getVerticalScrollBar().setUnitIncrement(32);
		return controlsOutline;
	}

	public static void accessFile(String fileName, JTextArea addToView) {
		calabashObjects.clear();
		File f = new File(System.getProperty("user.dir")+File.separator
				 + fileName);
		System.out.println(f.exists());
		System.out.println(f.canRead());

		InputStream is = CukeSlicerUI.class.getResourceAsStream(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		try {
			addToView.setText(line);
			InViewItem calabashItem = new InViewItem();
			while ((line = br.readLine()) != null) {

				if (line.contains("[1;37m["))
					addToView.append("\n");
				for (String key : KEYWORDS) {
					if (line.contains(key)) {
						String[] parts = line.split("\"");
						String divider = "";
						for (int i = 0; i < (30 - key.length()); i++) {
							divider += "_";
						}
						// todo: build InViewItem here
						if (parts[1].equals("enabled")) {
							if (line.contains("true")) {
								calabashItem.setEnabled(true);
							} else if (line.contains("false")) {
								calabashItem.setEnabled(false);
							}
						}
						if (parts.length > 3) {
							if (parts[1].equals("id")) {
								visibleCalabashObjects_id.add(parts[3]);
								calabashItem.setId(parts[3]);
							}
							if (parts[1].equals("text")) {
								visibleCalabashObjects_text.add(parts[3]);
								calabashItem.setText(parts[3]);
							}
							if (parts[1].equals("contentDescription")) {
								calabashItem.setContentDescription(parts[3]);
							}
							if (parts[1].equals("class")) {
								// System.out.println("set classnaem: " +
								// parts[3]);
								calabashItem
										.setAndroidObjectClassName(parts[3]);
							}
							addToView.append(parts[1] + divider + parts[3]
									+ "\n");
						}
						// is enabled
						// is button, edittext, textview, etc -
					}

					if (line.contains("[1;37m[")) {
						if (!(calabashItem.retrieveId() == null && calabashItem
								.retrieveText() == null)) {
							if (isValidObject(calabashItem))
								calabashObjects.add(calabashItem);
						}
						calabashItem = new InViewItem();
					}
				}
			}
			br.close();
			isr.close();
			is.close();

		} catch (Exception e) {

		}

	}

	private static boolean isValidObject(InViewItem calabashItem) {
		boolean validity = true;
		String fullObjectName = calabashItem.getFullClassName();
		if (fullObjectName.contains("Layout")) {
			validity = false;
		}

		return validity;
	}

	static class SuggestionPanel {
		private JList<String> list;
		private JPopupMenu popupMenu;
		private String subWord;
		private final int insertionPosition;

		public SuggestionPanel(JTextArea jta, int position, String subWord,
				Point location) {
			this.insertionPosition = position;
			this.subWord = subWord;
			popupMenu = new JPopupMenu();
			JScrollPane scroller = new JScrollPane();
			popupMenu.removeAll();
			popupMenu.setOpaque(false);
			popupMenu.setBorder(null);
			list = createSuggestionList(position,
					subWord);
			scroller.setViewportView(list);
			list.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == e.VK_ENTER) {

						// insertSelection();
					}
				}

			});
			popupMenu.add(scroller);
			popupMenu.show(jta, location.x, jta.getBaseline(0, 0) + location.y);
		}

		public void hide() {
			popupMenu.setVisible(false);
			if (suggestion == this) {
				suggestion = null;
			}
		}

		private JList<String> createSuggestionList(final int position,
				final String subWord) {
			// String[] data = IPrefiller.PREFILLS;
			ArrayList<String> combo = new ArrayList<String>(
					Arrays.asList(IPrefiller.PREFILLS));
			combo.addAll(definitions.keySet());
			String[] data = combo.toArray(new String[combo.size()]);
			JList list = new JList(data);
			list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					insertSelection();

				}
			});
			list.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == e.VK_ENTER) {

						// insertSelection();
					}
				}

			});
			return list;
		}

		public boolean insertSelection() {
			if (list.getSelectedValue() != null) {

				final String selectedSuggestion = ((String) list
						.getSelectedValue());
				// jta.getDocument().insertString(insertionPosition,
				// selectedSuggestion, null);

				jta.insert(" " + selectedSuggestion, jta.getCaretPosition());

				if (definitions.containsKey(selectedSuggestion)) {
					for (String step : definitions.get(selectedSuggestion)) {
						jta.insert("\n\t\t" + step, jta.getCaretPosition());
					}
				}
				return true;

				// hideSuggestion();
			}
			return false;
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
			final int position = jta.getCaretPosition();
			list.setSelectedIndex(index);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					jta.setCaretPosition(position);
				};
			});
		}
	}

	private static SuggestionPanel suggestion;

	protected static void showSuggestionLater() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showSuggestion();
			}

		});
	}

	protected static void showSuggestion() {
		hideSuggestion();
		final int position = jta.getCaretPosition();
		Point location;
		try {
			location = jta.modelToView(position).getLocation();
		} catch (BadLocationException e2) {
			e2.printStackTrace();
			return;
		}
		String text = jta.getText();
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
		final String subWord = text.substring(start, position);
		if (subWord.length() < 2) {
			// return;
		}
		suggestion = new CukeSlicerUI.SuggestionPanel(jta, position, subWord,
				location);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jta.requestFocusInWindow();
			}
		});
	}

	private static void hideSuggestion() {
		if (suggestion != null) {
			suggestion.hide();
		}
	}

	protected static void initUI() {

		jta.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN && suggestion != null) {
					suggestion.moveDown();
				} else if (e.getKeyCode() == KeyEvent.VK_UP
						&& suggestion != null) {
					suggestion.moveUp();
				} else if (Character.isLetterOrDigit(e.getKeyChar())) {
					showSuggestionLater();
				} else if (Character.isWhitespace(e.getKeyChar())) {
					hideSuggestion();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

	}

}
