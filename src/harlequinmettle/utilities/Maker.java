package harlequinmettle.utilities;

import harlequinmettle.calican.Cuker;
import harlequinmettle.calican.JavaRuntimeHost;
import harlequinmettle.interfaces.IGitStepLocations;
import harlequinmettle.interfaces.IParserKeywords;
import harlequinmettle.overrides.EnterButton;
import harlequinmettle.overrides.JScrollPanelledPane;
import harlequinmettle.overrides.SplitPaneTabbedGUI;
import harlequinmettle.someobjects.InViewItem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Maker {

	public static final Border BLACKBORDER = BorderFactory
			.createLineBorder(Color.black);
	public static final int HORIZONTAL = 8000000;
	public static final int VERTICAL = 1111111;
	public static final int DEFAULT = 0;
	public static final int DATA_PATH = 44440;
	public static final int STEPS_PATH = 550;

	// generates a
	public static JScrollPanelledPane makeScrollPane() {

		JScrollPanelledPane mScrollPane = new JScrollPanelledPane();

		return mScrollPane;
	}

	public static JPanel makePanel(int orientation) {
		JPanel p = new JPanel();

		switch (orientation) {
		case HORIZONTAL:
			p.setLayout(new GridLayout(1, 0));
			break;
		case VERTICAL:
			p.setLayout(new GridLayout(0, 1));
			break;
		default:
			break;
		}
		return p;
	}

	public static JScrollPane makeTextScroll(JTextArea jta) {
		JScrollPane textScroll = new JScrollPane();
		Font mFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		jta.setFont(mFont);
		jta.setLineWrap(false);
		textScroll.setViewportView(jta);
		textScroll.setPreferredSize(new Dimension(600, 300));
		textScroll.getVerticalScrollBar().setUnitIncrement(32);
		return textScroll;
	}

	public static EnterButton makeFilePathButton(final JTextArea textToUpdate,
			final int TYPE) {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final EnterButton a = new EnterButton("browse");
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				// In response to a button click:
				int returnVal = fc.showOpenDialog(a);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String path = file.getAbsolutePath();

					String previousPath = "";

					if (TYPE == DATA_PATH) {
						previousPath += Cuker.programSettings.pathToTextOutputFiles;
					} else if (TYPE == STEPS_PATH) {
						previousPath += Cuker.programSettings.pathsToCalabashSteps;
					}

					if (previousPath.length() > 0)
						previousPath += (";\n");

					previousPath += path;

					if (TYPE == DATA_PATH) {
						Cuker.programSettings.pathToTextOutputFiles = previousPath;
					} else if (TYPE == STEPS_PATH) {
						Cuker.programSettings.pathsToCalabashSteps = previousPath;
					}

					textToUpdate.append(previousPath);

					Maker.resetGuiFromSettings();
				}

			}

		});
		return a;
	}

	// public static EnterButton makeFilePathButton(final JTextArea
	// textToUpdate) {
	// final JFileChooser fc = new JFileChooser();
	// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	// final EnterButton a = new EnterButton("browse");
	// a.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	//
	// // In response to a button click:
	// int returnVal = fc.showOpenDialog(a);
	// if (returnVal == JFileChooser.APPROVE_OPTION) {
	// File file = fc.getSelectedFile();
	// a.setResult(file.getAbsolutePath());
	// if (textToUpdate.getText().length() > 0)
	// textToUpdate.append(";\n");
	//
	// textToUpdate.append(file.getAbsolutePath());
	// // Maker.resetGuiFromSettings();
	// }
	//
	// }
	//
	// });
	// return a;
	// }

	public static void resetGuiFromSettings() {
		FileMemoryManager.saveSettings();
		Cuker.refreshGuiLayout();
		Cuker.tabbedPane.setSelectedComponent(Cuker.settingsViewPanel);
	}

	public static EnterButton makeRefreshButton() {
		final EnterButton a = new EnterButton("refresh");
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetGuiFromSettings();
				// update settings from gui data
				// update gui from new settings

			}

		});
		return a;
	}

	public static EnterButton makeSaveButton() {
		final EnterButton a = new EnterButton("save settings");
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Cuker.programSettings.pathToTextOutputFiles = Cuker.textDataPath
						.getText();

				Cuker.programSettings.pathsToCalabashSteps = Cuker.textStepsPath
						.getText();

				FileMemoryManager.saveSettings();

			}

		});
		return a;
	}

	// this button will generate all steps to lowerTextArea workspace
	public static EnterButton makeCalabashObjectButton(
			final InViewItem inViewItem, final JTextArea writeTo, int totCount,
			int usedCount) {

		final EnterButton a = new EnterButton("default");
		a.useCount = usedCount;
		a.totalCount = totCount;
		a.setHorizontalAlignment(SwingConstants.LEFT);
		a.setCalabashItem(inViewItem);
		a.setText(a.getCalabashTitle());
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				writeTo.insert("\n ", writeTo.getCaretPosition());
				for (String s : InViewItem.TITLES) {
					if (Cuker.programSettings.autoGenerateStepsPreferences
							.get(s))
						writeTo.insert("\t" + inViewItem.genericGetText(s)
								+ "\n", writeTo.getCaretPosition());
				}
				writeTo.insert("\n\n", writeTo.getCaretPosition());
				// ID PRESS
			}

		});
		return a;
	}

	public static EnterButton makeScriptGeneratorButton(
			final String buttonTitle, final InViewItem inViewItem,
			final JFrame chooser) {
		final String statement = inViewItem.genericGetText(buttonTitle);
		final EnterButton a = new EnterButton(statement);

		a.setHorizontalAlignment(SwingConstants.LEFT);
		a.setCalabashItem(inViewItem);
		// a.setText(a.getCalabashTitle());

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Cuker.gui.textLower.insert("\t" + statement,
						Cuker.gui.textLower.getCaretPosition());
				// chooser.setVisible(false);
				chooser.dispose();

			}

		});
		return a;
	}

	public static EnterButton makeCalabashObjectWindowedOptionsButton(
			final InViewItem inViewItem, final JTextArea writeTo, int totCount,
			int usedCount) {

		final EnterButton a = new EnterButton("default");
		a.useCount = usedCount;
		a.totalCount = totCount;
		a.setHorizontalAlignment(SwingConstants.LEFT);
		a.setCalabashItem(inViewItem);
		a.setText(a.getCalabashTitle());

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				final JFrame chooser = new JFrame("Choose Calabash Step");
				final JScrollPanelledPane scroll = Maker.makeScrollPane();
				chooser.setSize(500, 700);
				chooser.setLocation(450, 30);
				chooser.add(scroll);
				chooser.setVisible(true);

				writeTo.insert("\n ", writeTo.getCaretPosition());
				for (String s : InViewItem.TITLES) {
					if (Cuker.programSettings.autoGenerateStepsPreferences
							.get(s)) {
						scroll.addComp(Maker.makeScriptGeneratorButton(s,
								inViewItem, chooser));
					}
				}

			}

		});
		return a;
	}

	public static JPanel generatePanel(JComponent... comps) {
		JPanel shell = Maker.makePanel(Maker.HORIZONTAL);
		if (!Cuker.programSettings.useSystemLookAndFeel)
			shell.setBorder(BLACKBORDER);
		for (JComponent a : comps)
			shell.add(a);

		return shell;
	}

	public static void setupFilesButtonsTab() {
		File[] fileList = new File(Cuker.programSettings.pathToTextOutputFiles)
				.listFiles();
		File f = new File(Cuker.programSettings.pathToTextOutputFiles);

		if (fileList != null) {
			JScrollPanelledPane fileScroll = Maker.makeScrollPane();

			for (File s : fileList) {
				EnterButton eb = null;
				if (Cuker.programSettings.useWindows) {
					eb = Maker.makeFileLoadButtonWindowLaunch(s
							.getAbsolutePath());
				} else {
					eb = Maker.makeFileLoadButton(s.getAbsolutePath(),
							Cuker.gui);
				}

				eb.setHorizontalAlignment(SwingConstants.LEFT);
				fileScroll.addComp(eb);
			}
			Cuker.gui.PANES.put("FILES", fileScroll);
		}
	}

	public static void setupScenarioExtractorTab() {

		String pathToFeatures = getFeaturePathFromStepsPath();
		System.out.println(pathToFeatures);

		
		if (pathToFeatures.equals(""))
			return;
		Collection<File> fileList = new ArrayList<File>();
		try {
			File	features = new File(pathToFeatures);

			 extractAllFeatureFiles(features,fileList);

			if (fileList == null) return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.out.println("*******"+fileList);

		if (fileList != null) {

			JScrollPanelledPane fileScroll = Maker.makeScrollPane();

			for (File s : fileList) { 
				EnterButton eb = null;

				eb = Maker.makeFeatureFileLoadButtonWindowLaunch(s
						.getAbsolutePath());

				eb.setHorizontalAlignment(SwingConstants.LEFT);
				fileScroll.addComp(eb);
			}
			Cuker.gui.PANES.put("SCENARIOS", fileScroll);
			 
		}
	}

	private static void extractAllFeatureFiles(File features,Collection<File> addUp) {
 

                File root =features;
            	System.out.println("file: "+root.getName()+"   "+root.isDirectory());
                File[] list = root.listFiles();

                if (list == null)
                        return;

                for (File original : list) {
                        if (original.isDirectory()) {
                        	System.out.println("file: "+addUp);
                        	extractAllFeatureFiles(original,addUp);
                        } else {
                               if(original.getName().endsWith("feature")){
                            	   addUp.add(original);
                               }

                        }
                }
        }

 

	private static String getFeaturePathFromStepsPath() {
		String[] paths = Cuker.programSettings.pathsToCalabashSteps.split(";");
		for (String s : paths) {
			if (s.contains("features"))
				return s.substring(0, s.indexOf("features") + 9);

		}
		return "";
	}

	private static EnterButton makeFeatureFileLoadButtonWindowLaunch(
			final String s) {

		final EnterButton a = new EnterButton(s);
		final String shortName = new File(s).getName();
		a.setText(shortName);
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 
				try {
					Cuker.gui.textLower.setText( FileUtils.readFileToString(new File(s)));
					  
				} catch (IOException e) { 
					e.printStackTrace();
				}
//				FileMemoryManager.loadCalabashFileTabData(
//						Cuker.programSettings, s);
//
//				final JFrame newWindow = new JFrame(shortName);
//				JTabbedPane fileDataWindow = new JTabbedPane();
//				newWindow.setSize(800, 700);
//				newWindow.setLocation(550, 30);
//				newWindow.setVisible(true);
//				JScrollPanelledPane buttons = Maker.makeScrollPane();
//				newWindow.add(fileDataWindow);
//				// fileDataWindow.add("formated data", dataTextArea);
//
//				for (EnterButton eb : Cuker.buttonPanelMap.get(s)) {
//
//					buttons.add(Maker.generatePanel(eb));
//				}
//				fileDataWindow.add("generate steps", buttons);
//				fileDataWindow.add("data outline",
//						buildScrollText(pretyData(Cuker.RAW_TEXT.get(s))));
//				fileDataWindow.add("raw data",
//						buildScrollText(Cuker.RAW_TEXT.get(s)));
//
		}

		});
		return a;
	}

	private static EnterButton makeFileLoadButtonWindowLaunch(final String s) {

		final EnterButton a = new EnterButton(s);
		final String shortName = new File(s).getName();
		a.setText(shortName);
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				FileMemoryManager.loadCalabashFileTabData(
						Cuker.programSettings, s);

				final JFrame newWindow = new JFrame(shortName);
				JTabbedPane fileDataWindow = new JTabbedPane();
				newWindow.setSize(800, 700);
				newWindow.setLocation(550, 30);
				newWindow.setVisible(true);
				JScrollPanelledPane buttons = Maker.makeScrollPane();
				newWindow.add(fileDataWindow);
				// fileDataWindow.add("formated data", dataTextArea);

				for (EnterButton eb : Cuker.buttonPanelMap.get(s)) {

					buttons.add(Maker.generatePanel(eb));
				}
				fileDataWindow.add("generate steps", buttons);
				fileDataWindow.add("data outline",
						buildScrollText(pretyData(Cuker.RAW_TEXT.get(s))));
				fileDataWindow.add("raw data",
						buildScrollText(Cuker.RAW_TEXT.get(s)));

			}

		});
		return a;
	}

	protected static Component buildScrollText(String s) {
		JScrollPanelledPane rawPane = new JScrollPanelledPane();
		rawPane.addComp(new JTextArea(s));

		return rawPane;
	}

	public static String pretyData(String rawData) {
		String[] lines = rawData.split("\n");
		StringBuilder addToView = new StringBuilder();
		for (String line : lines) {

			if (line.contains("[1;37m["))
				addToView.append("\n");
			for (String key : IParserKeywords.KEYWORDS) {
				if (line.contains(key)) {
					String[] parts = line.split("\"");
					String divider = "";

					for (int i = 0; i < (30 - key.length()); i++) {
						divider += "_";
					}
					if (parts.length > 3)
						addToView.append(parts[1] + divider + parts[3] + "\n");
				}
			}
		}

		return addToView.toString();

	}

	public static void setupStepsButtonsTab() {
		JScrollPanelledPane stepScroll = Maker.makeScrollPane();
		ArrayList<File> stepsFileList = new ArrayList<File>();
		for (String path : Cuker.programSettings.pathsToCalabashSteps
				.replaceAll("\n", "").split(";")) {
			File dir = new File(path);
			if (dir.exists()) {
				stepsFileList.addAll(Arrays.asList(dir.listFiles()));
			}
		}
		for (File dr : stepsFileList) {
			if (Cuker.first
					|| !Cuker.programSettings.fileModifiedMap.containsKey(dr
							.getAbsolutePath())
					|| dr.lastModified() != Cuker.programSettings.fileModifiedMap
							.get(dr.getAbsolutePath())) {
				FileMemoryManager.loadStepDefinitions(dr);
				Cuker.programSettings.fileModifiedMap.put(dr.getAbsolutePath(),
						dr.lastModified());
			}
		}
		// FIX confirm data obtained boolean
		// if (!Cuker.programSettings.hasCheckedGithub && Cuker.first) {
		if ((Cuker.programSettings.stepsFromGitHub.length() == 0 && Cuker.first)) {
			Cuker.programSettings.stepsFromGitHub = "";
			for (String s : IGitStepLocations.GIT_LOC) {

				FileMemoryManager.loadStepDefinitionsFromUrl(s);

			}
			Cuker.programSettings.hasCheckedGithub = true;
		}
		for (Entry<String, ArrayList<String>> ent : Cuker.programSettings.STEPS
				.entrySet()) {
			if (Cuker.programSettings.cannedStepsInclusion.get(ent.getKey())) {
				EnterButton eb = Maker.makeStepDefinitionButton(ent, Cuker.gui);

				eb.setHorizontalAlignment(SwingConstants.LEFT);
				stepScroll.addComp(eb);
			}
		}
		Cuker.gui.PANES.put("STEPS", stepScroll);

	}

	public static EnterButton makeFileLoadButton(final String s,
			final SplitPaneTabbedGUI gui) {
		final EnterButton a = new EnterButton(s);
		final String shortName = new File(s).getName();
		a.setText(shortName);
		a.setHorizontalAlignment(SwingConstants.LEFT);
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				FileMemoryManager.loadCalabashFileTabData(
						Cuker.programSettings, s);
				JScrollPanelledPane buttons = Maker.makeScrollPane();
				gui.PANES.put(shortName, buttons);
				for (EnterButton eb : Cuker.buttonPanelMap.get(s)) {
					buttons.add(Maker.generatePanel(eb));
				}
				gui.updateTabs();
				gui.tabbedPane.setSelectedComponent(buttons);
				// cuker.refreshGuiLayout();

			}

		});
		return a;
	}

	public static EnterButton makeStepDefinitionButton(
			Entry<String, ArrayList<String>> ent, final SplitPaneTabbedGUI gui) {
		final String macro = ent.getKey();
		final EnterButton a = new EnterButton(macro);

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				gui.textLower.insert("\n\t\t" + macro+ "   #"+Cuker.programSettings.LINE_NUMBERS.get(macro),
						gui.textLower.getCaretPosition());
				for (String step : Cuker.programSettings.STEPS.get(macro)) {
					if (Cuker.programSettings.commentOutSteps) {
						gui.textLower.insert("\n\t\t " + step,
								gui.textLower.getCaretPosition());
					} else {
						gui.textLower.insert(
								"\n\t\t" + step.replaceAll("#", ""),
								gui.textLower.getCaretPosition());
					}
				}
				gui.updateTabs();
				// cuker.refreshGuiLayout();

			}

		});
		return a;
	}

	public static JCheckBox makeCommentCheckbox() {

		final JCheckBox a = new JCheckBox("Comment out steps in definition");

		a.setSelected(Cuker.programSettings.commentOutSteps);
		a.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
				Cuker.programSettings.commentOutSteps = cbStatus;
			}

		});
		return a;

	}

	public static JCheckBox makeUseSystemLookCheckbox() {

		final JCheckBox a = new JCheckBox("Use system look and feel");

		a.setSelected(Cuker.programSettings.useSystemLookAndFeel);
		a.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
				Cuker.programSettings.useSystemLookAndFeel = cbStatus;
				resetGuiFromSettings();
			}

		});
		return a;

	}

	public static EnterButton makeConsoleEnterButton(final JTextArea textFrom,
			final JavaRuntimeHost terminal) {
		final EnterButton a = new EnterButton("enter");

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				terminal.writeToRuntimeOutputStream(textFrom.getText().trim());
			}

		});
		return a;
	}

	public static EnterButton makeMacroConverterButton() {
		EnterButton macroConverter = new EnterButton("step->macro");
		macroConverter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String textToMacro = Cuker.gui.textLower.getSelectedText();
				boolean selectMode = true;
				if (textToMacro == null) {
					textToMacro = Cuker.gui.textLower.getText();
					selectMode = false;
				}
				String lines[] = textToMacro.split("\n");
				String newLines[] = new String[lines.length];
				int pos = 0;
				for (String line : lines) {
					line = line.trim();
					// Given (/^I log in$/) do
					if (line.contains("Scenario")) {
						line = line.replaceAll("Scenario:", "Given (/^");
						line += "$/) do";
						newLines[pos++] = line;
					} else if (!line.startsWith("performAction")
							&& line.length() > 0 && !line.contains("#")
							&& !line.contains("$/) do")
							&& !line.startsWith("macro") && !line.equals("end")
							&& // this could leave out certain text or ids with
								// many underscores
							line.split("_").length < 5) {
						line = line.replaceAll("Then", "");
						line = line.replaceAll("And", "");
						line = line.replaceAll("When", "");
						line = line.replaceAll("Given", "");
						line = "macro '" + line.trim() + "'";
						while (line.contains("''"))
							line = line.replaceAll("''", "'");
						newLines[pos++] = line;
						// macro 'I wait for "Username" to appear'
					} else if (line.length() > 0) {
						newLines[pos++] = line;
					}
				}
				if (!selectMode)
					Cuker.gui.textLower.selectAll();
				Cuker.gui.textLower.cut();
				String opener = "Given(/^MMMMMM$/) do";
				Cuker.gui.textLower.insert("\n" + opener,
						Cuker.gui.textLower.getCaretPosition());

				for (String line : newLines) {
					if (line != null)
						Cuker.gui.textLower.insert("\n" + line,
								Cuker.gui.textLower.getCaretPosition());

				}

				Cuker.gui.textLower.insert("\n\t\t" + "end",
						Cuker.gui.textLower.getCaretPosition());

			}

		});
		return macroConverter;
	}

	public static EnterButton makeMacroReverterButton() {
		EnterButton macroReverter = new EnterButton("steps<-macro");
		macroReverter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String textToMacro = Cuker.gui.textLower.getSelectedText();
				boolean selectMode = true;
				if (textToMacro == null) {
					textToMacro = Cuker.gui.textLower.getText();
					selectMode = false;
				}
				String lines[] = textToMacro.split("\n");
				String newLines[] = new String[lines.length];
				int pos = 0;
				for (String line : lines) {
					line = line.trim();
					// Given (/^I log in$/) do

					if (line.contains("$/) do"))
						continue;
					if (line.equals("end"))
						line = line.replaceAll("end", "");
					line = line.replaceAll("'", "");
					line = line.replaceAll("macro", "And");
					newLines[pos++] = line;
					// macro 'I wait for "Username" to appear'

				}
				if (!selectMode)
					Cuker.gui.textLower.selectAll();
				Cuker.gui.textLower.cut();

				for (String line : newLines) {
					if (line != null)
						Cuker.gui.textLower.insert("\n" + line,
								Cuker.gui.textLower.getCaretPosition());

				}

			}

		});
		return macroReverter;
	}

	public static EnterButton makeStepsInsertButton() {
		EnterButton appendSteps = new EnterButton("Insert Scenario");
		appendSteps.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] paths = Cuker.programSettings.pathsToCalabashSteps
						.split(";");
				String path = "";
				for (String s : paths)
					if (s.contains("features"))
						path = s.substring(0, s.indexOf("features") + 9)
								+ "cucumberjam.feature";
				File cukeFeature = new File(path);

				String text = Cuker.gui.textLower.getText();
				String existingText = "";
				try {
					existingText = FileUtils.readFileToString(cukeFeature);
				} catch (IOException e) {
				}
				text = ensureFeatureSyntax(existingText, text);
				try {
					FileUtils.writeStringToFile(cukeFeature, text, false);
				} catch (IOException e) {
				}
				// get path to user steps
				// append text from jtextarea to file
			}

			// WIP
			private String ensureFeatureSyntax(String existingText, String text) {
				String textForFile = "@cucumberjam\nFeature: default \n\n";
				// existingText = existingText.replaceAll("@cucumberjam", "");
				// existingText =
				// existingText.replaceAll("Feature: default (Wipe)", "");
				int index = existingText.indexOf("Feature:");
				if (index > 0 && existingText.length()>24)
					existingText = existingText.substring(index + 16);
				// appendText+= existingText+"\n";
				if (!text.contains("Scenario:"))
					textForFile += "Scenario: cucumberjam\n\n";
				String[] lines = text.split("\n");
				for (String s : lines) {
					if (s.startsWith("Scenario:") || s.trim().length() == 0)
						continue;
					textForFile +=   s.trim() + "\n";
				}
				return textForFile + existingText + "\n\n";
			}

		});
		return appendSteps;
	}

	public static JPanel makeControlPanel(JTextArea textLower) {

		return Maker.generatePanel(//
				Maker.makeMacroConverterButton(),//
				Maker.makeMacroReverterButton(),//
				Maker.makeStepsInsertButton(),//
				Maker.makeUndoButton(),//
				Maker.makeRedoButton(),//
				Maker.makeFormatTextArea(),//
				Maker.makeClearTextAreaButton()//
				);
	}

	private static JComponent makeFormatTextArea() {
		final EnterButton a = new EnterButton("Allign");

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String originalLines[] = Cuker.gui.textLower.getText().split(
						"\n");
				String newText = "\n\n\n";
				for (String line : originalLines) {
					if (line.trim().length() > 0)
						newText += "\n\t" + line.trim();
				}
				Cuker.gui.textLower.setText(newText);
			}

		});
		return a;
	}

	private static JComponent makeClearTextAreaButton() {
		final EnterButton a = new EnterButton("Clear");

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Cuker.gui.textLower.setText("\n\n\n\n\n\n\n\n\n\n\n\n");

			}

		});
		return a;
	}

	public static EnterButton makeRedoButton() {
		final EnterButton a = new EnterButton("Redo", true);

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Cuker.gui.redo();
			}

		});
		return a;
	}

	public static EnterButton makeUndoButton() {
		final EnterButton a = new EnterButton("Undo", true);

		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Cuker.gui.undo();
			}

		});
		return a;
	}

	public static JCheckBox makeUseSuggestionTextCheckbox() {

		final JCheckBox a = new JCheckBox("Generate Steps From Suggestion Box");

		a.setSelected(Cuker.programSettings.generateStepsWithSuggestionBox);
		a.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
				Cuker.programSettings.generateStepsWithSuggestionBox = cbStatus;
			}

		});
		return a;
	}
}
