package harlequinmettle.calican;

import harlequinmettle.overrides.CustomJLabel;
import harlequinmettle.overrides.CustomJTextArea;
import harlequinmettle.overrides.EnterButton;
import harlequinmettle.overrides.JScrollPanelledPane;
import harlequinmettle.overrides.SplitPaneTabbedGUI;
import harlequinmettle.someobjects.ProgramSettings;
import harlequinmettle.utilities.FileMemoryManager;
import harlequinmettle.utilities.Maker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Cuker {   
	// TODO: ADD option to persist history
	// TODO: ADD DEBUG MAPPING  
	// TODO: ADD APPEND TO STEPS FILE: warn if duplicate, overwrite if intended/ add to wip steps 
	// TODO: ADD SETTINGS-remove comments 
	/////////////////////////////// 
	
	public static String loadingText = "Program is Loading. . . ";
	static JViewport savePort = null;
	private static Point portPoint = null;
	// general framework
	static final JFrame application = new JFrame(loadingText);
	// generic tabbedPane: settings, gui, ....
	public static final JTabbedPane tabbedPane = new JTabbedPane();
	// settings: paths to folders etc.
	public static ProgramSettings programSettings;

	public static final JScrollPanelledPane settingsViewPanel = Maker
			.makeScrollPane();
	// JFrame containing a split pane:top-tabbed pane, bottom textarea
	// scenario builder: each text file create a scrollable set of buttons -
	// pressing generates canned steps
	public static final SplitPaneTabbedGUI gui = new SplitPaneTabbedGUI();

	// settings components:
	public static final CustomJLabel labelOutputFile = new CustomJLabel(
			"Path to calabash console output text files : ");
	public static final CustomJTextArea textDataPath = new CustomJTextArea();
	public static final EnterButton buttonBrowseDataFiles = Maker
			.makeFilePathButton(textDataPath, Maker.DATA_PATH);

	public static final CustomJLabel labelStepsFile = new CustomJLabel(
			"Path to calabash canned steps and custom steps : ", new Color(90, 190, 210));
	public static final CustomJTextArea textStepsPath = new CustomJTextArea(
			new Color(140, 195, 190));
	public static final EnterButton buttonBrowseStepsFiles = Maker
			.makeFilePathButton(textStepsPath, Maker.STEPS_PATH);
 
	// -----------------
	public static final EnterButton buttonRefresh = Maker.makeRefreshButton();
	public static final EnterButton buttonSave = Maker.makeSaveButton();
	private static Color checkedColor = new Color(190, 230, 245);
	private static Color uncheckedColor = new Color(100, 100, 100);
	private static Color checkedColorLight = new Color(210, 250, 255);
	private static Color uncheckedColorLight = new Color(150, 150, 150);
	private static Color checkedColorBluer = new Color(180, 230, 255);
	private static Color uncheckedColorBluer = new Color(120, 140, 150);

	public static final TreeMap<String, ArrayList<EnterButton>> buttonPanelMap = new TreeMap<String, ArrayList<EnterButton>>();
	public static final Dimension APP_SIZE = new Dimension(1330, 320);
	public static boolean first = true;

	public static final TreeMap<String,String> RAW_TEXT = new  TreeMap<String,String>();

	public static LookAndFeel originalLook = UIManager.getLookAndFeel();

	private static UndoManager undoManager = new UndoManager();

	public Cuker() {
		refreshGuiLayout();
		first = false;
		
	}

	private static void sendStartUsage() {
		//jframe (sending usage info) 
		
		
	}

	private static void restorePreferences() {

		programSettings = FileMemoryManager.restoreSettings();

		textDataPath.setText(programSettings.pathToTextOutputFiles);

		textStepsPath.setText(programSettings.pathsToCalabashSteps);
		
		FileMemoryManager.loadStepsFromStringArray(programSettings.stepsFromGitHub.split("\n"));

	}

	private static void updateTitle() {
		loadingText += ". . . . ";
		application.setTitle(loadingText);
	}

	public static void refreshGuiLayout() {
		restorePreferences();
		sendStartUsage();
		try {
			if (programSettings.useSystemLookAndFeel) {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} else {
				UIManager.setLookAndFeel(originalLook);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JFrame
		application.setVisible(true);
		// application.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (first) {
			application.setSize(1230, 700);
			application.setLocation(50, 50);
		}
		application.add(tabbedPane);
		tabbedPane.add("step builder", gui);
		tabbedPane.add("settings", settingsViewPanel);

		textDataPath.setText(programSettings.pathToTextOutputFiles);

		textStepsPath.setText(programSettings.pathsToCalabashSteps);

		updateTitle();
		Maker.setupStepsButtonsTab();
		updateTitle();

		Maker.setupFilesButtonsTab();
		updateTitle();


		Maker.setupScenarioExtractorTab();
		updateTitle();

		setupSettingsTab();
		updateTitle();

		// gui - split pane: lower(text area/workspace); upper(tabbs with
		// scrolling lists of buttons)
		gui.updateTabs();
		updateTitle();
		gui.addControlPanel(Maker.makeControlPanel(gui.textLower));
		// only works for keytyped not inserttext
		// addUndoRedoLogic();
		application.setTitle("Calabash Tool");
	}

	private static void setupSettingsTab() {
		settingsViewPanel.parts.clear();
		settingsViewPanel.add(Maker.generatePanel(buttonRefresh, buttonSave));

		settingsViewPanel.add(Maker.generatePanel(labelOutputFile,
				textDataPath, buttonBrowseDataFiles));

		settingsViewPanel.add(Maker.generatePanel(labelStepsFile,
				textStepsPath, buttonBrowseStepsFiles));

		settingsViewPanel.add(Maker.generatePanel(//
				Maker.makeCommentCheckbox(),//
				Maker.makeUseSystemLookCheckbox(),//
				Maker.makeUseSuggestionTextCheckbox()));
		buildCalabashCannedStepsCheckboxUI(settingsViewPanel);
		buildCalabashCodeGeneratorCheckboxUI(settingsViewPanel);
		buildCalabashFilterCheckboxUI(settingsViewPanel);
		if (savePort != null) {
			savePort.setViewPosition(portPoint);
			settingsViewPanel.setViewport(savePort);
		}
	}

	private static void buildCalabashCannedStepsCheckboxUI(
			final JScrollPanelledPane settings) {
		for (final Entry<String, Boolean> ent : programSettings.cannedStepsInclusion
				.entrySet()) {
			final String tGen = ent.getKey();
			final JCheckBox bx = new JCheckBox(tGen);
			boolean selected = ent.getValue();
			if (selected) {
				bx.setBackground(checkedColorBluer);
			} else {
				bx.setBackground(uncheckedColorBluer);
			}
			bx.setSelected(selected);
			bx.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					savePort = settings.getViewport();
					portPoint = savePort.getViewPosition();
					boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
					programSettings.cannedStepsInclusion.put(tGen, cbStatus);
					// Maker.resetGuiFromSettings();
					if (cbStatus) {
						bx.setBackground(checkedColorBluer);
					} else {

						bx.setBackground(uncheckedColorBluer);
					}
				}

			});
			settings.add(Maker.generatePanel(bx));
		}

	}

	// MOVE TO MAKER
	private static void buildCalabashCodeGeneratorCheckboxUI(
			final JScrollPanelledPane settings) {
		for (final Entry<String, Boolean> ent : programSettings.autoGenerateStepsPreferences
				.entrySet()) {
			final String tGen = ent.getKey();
			final JCheckBox bx = new JCheckBox(tGen);
			boolean selected = ent.getValue();
			if (selected) {
				bx.setBackground(checkedColorLight);
			} else {
				bx.setBackground(uncheckedColorLight);
			}
			bx.setSelected(selected);
			bx.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
					programSettings.autoGenerateStepsPreferences.put(tGen,
							cbStatus);
					Maker.resetGuiFromSettings();
					if (cbStatus) {
						bx.setBackground(checkedColorLight);
					} else {

						bx.setBackground(uncheckedColorLight);
					}
				}

			});
			settings.add(Maker.generatePanel(bx));
		}

	}

	// MOVE TO MAKER
	private static void buildCalabashFilterCheckboxUI(
			final JScrollPanelledPane settings) {
		settings.add(Maker.generatePanel(new CustomJLabel("Simple Class Name",
				new Color(200, 200, 200)), new CustomJLabel("Full Class Name",
				new Color(200, 200, 200))));
		for (final Entry<String, Boolean> ent : programSettings.androidViewTypes
				.entrySet()) {

			final String fullClassName = ent.getKey();
			String[] objectPath = fullClassName.split("\\.");
			String className = objectPath[objectPath.length - 1];
			final JCheckBox bx = new JCheckBox(className);
			boolean selected = ent.getValue();
			if (selected) {
				bx.setBackground(checkedColor);
			} else {

				bx.setBackground(uncheckedColor);
			}
			bx.setSelected(selected);
			bx.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					boolean cbStatus = (arg0.getStateChange() == ItemEvent.SELECTED);
					programSettings.androidViewTypes.put(fullClassName,
							cbStatus);
					Maker.resetGuiFromSettings();
					if (cbStatus) {
						bx.setBackground(checkedColor);
					} else {

						bx.setBackground(uncheckedColor);
					}
				}

			});
			settings.add(Maker.generatePanel(bx, new CustomJLabel(
					fullClassName, new Color(190, 220, 255))));
		}

	}

	private static void addUndoRedoLogic() {

		Document doc = gui.textLower.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {

				System.out.println("Add edit");
				undoManager.addEdit(e.getEdit());

			}
		});

		InputMap im = gui.textLower.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = gui.textLower.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");

		am.put("Undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canUndo()) {
						undoManager.undo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});
		am.put("Redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canRedo()) {
						undoManager.redo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		Cuker c = new Cuker();
	}

}
