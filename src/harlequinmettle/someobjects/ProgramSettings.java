package harlequinmettle.someobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class ProgramSettings implements Serializable {
	// generated serial version id, ok
	private static final long serialVersionUID = 853687484565870980L;
	public String pathToTextOutputFiles = "";
	public String pathsToCalabashSteps = ""; 
	// loaded as the calabash data is read in (for now from files)
	public final TreeMap<String, Boolean> androidViewTypes = new TreeMap<String, Boolean>();
	// defined in InViewItem : the actual string text
	public final TreeMap<String, Boolean> autoGenerateStepsPreferences = new TreeMap<String, Boolean>();
	// read in from all files found in the step paths, typically canned steps
	// provided, and user defined macros
	public final TreeMap<String, Boolean> cannedStepsInclusion = new TreeMap<String, Boolean>();
	public final TreeMap<String, Long> fileModifiedMap = new TreeMap<String, Long>();
	public boolean commentOutSteps = false;
	public boolean useSystemLookAndFeel = false;
	public boolean generateStepsWithSuggestionBox = true;
	public boolean hasCheckedGithub = false;
	public String stepsFromGitHub = "";
	public static final TreeMap<String, ArrayList<String>> STEPS = new TreeMap<String, ArrayList<String>>();
	public static final TreeMap<String, Integer> LINE_NUMBERS = new TreeMap<String,Integer>();

	public boolean useWindows = true;

	// public boolean generateButtonsTab = true;
	// public boolean generateDataTab = true;

	public ProgramSettings() {
		for (String s : InViewItem.TITLES) {
			autoGenerateStepsPreferences.put(s, true);
		}
	}

}
