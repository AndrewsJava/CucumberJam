package applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

public class TestCondenser {
	public static final TreeMap<String, ArrayList<String>> SCENARIOS_BY_INITIAL_LINE = new TreeMap<String, ArrayList<String>>();
	public static final TreeMap<String, Integer> INITIAL_LINES = new TreeMap<String, Integer>();
	public static final String FIRST_LOGIN_FEATURE = "@LoginOnly\n@CondensedScenarios\n" + "Feature: condensed login scenarios\n"//
			+ "\n@NeedsFix\n@Known2Run\nScenario: all scenarios that require login\nGiven I log in and sync";
	public static final String THE_REST_FEATURE = "@TestDriveOnly\n@CondensedScenarios\n" + "Feature: condensed rest\n\n" + "Scenario: all scenarios that don't require qa account access";
	public static final ArrayList<File> REQUIRE_LOGIN = new ArrayList<File>();

	public static final ArrayList<File> ALL_THE_REST = new ArrayList<File>();
	static String path = "\\Users\\aparelius\\svn_repo_tablet\\FreeTablet\\features";
	static File cond = new File(path + "\\CondensedLoginScenarios.feature");
	static File rest = new File(path + "\\CondensedRestScenarios.feature");
	static File pinReset, pinDisable;

	public static void main(String[] args) {
		String path = "\\Users\\aparelius\\svn_repo_tablet\\FreeTablet\\features";
		cond.delete();
		rest.delete();
		splitFiles(path);
		System.out.println("File moved to end:" + pinReset.getAbsoluteFile());
		REQUIRE_LOGIN.remove(pinReset);
		REQUIRE_LOGIN.add(pinReset);
		REQUIRE_LOGIN.remove(pinDisable);
		REQUIRE_LOGIN.add(pinDisable);
		condenseRequireLogin();
		condenseTheRest();
	}

	private static void condenseRequireLogin() {
		StringBuilder forFile = new StringBuilder(FIRST_LOGIN_FEATURE);
		for(File f : REQUIRE_LOGIN) {
			appendAllScenarios(forFile, f);
		}
		saveToFile(forFile, cond);

	}

	private static void condenseTheRest() {
		StringBuilder forFile = new StringBuilder(THE_REST_FEATURE);
		for(File f : ALL_THE_REST) {
			appendAllScenarios(forFile, f);
		}
		saveToFile(forFile, rest);

	}

	private static void appendAllScenarios(StringBuilder forFile, File f) {

		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try {

			isr = new InputStreamReader(new FileInputStream(f));
			br = new BufferedReader(isr);

			while(( line = br.readLine() ) != null) {
				if(line.contains("Scenario:")) {
					break;
				}
				do {
					line = line.replace("I log in and sync", "I enter my pin");
					if(line.contains("@KnownToRun") || line.contains("@NotKnownToRun") || line.contains("@KnownToRunWithPrinters") || line.contains("@FixMe"))
						forFile.append(line.replaceAll("KnownToRun", "Known2Run").replaceAll("FixMe", "NeedsFix") + "\n\n");
					if(!line.contains("@") && !line.contains("Feature:"))
						forFile.append(line + "\n\n");
				} while(( line = br.readLine() ) != null);
			}
			br.close();
			isr.close();

		} catch(Exception e) {
			// suck it!!!!
		}
	}

	private static void saveToFile(StringBuilder forFile, File cond2) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(cond2, true)));

			out.println(forFile.toString());
			out.close();
		} catch(IOException e) {
			// oh noes!
		}
	}

	public static void splitFiles(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		if(list == null)
			return;

		for(File f : list) {
			if(f.isDirectory()) {
				splitFiles(f.getAbsolutePath());

				// System.out.println( "Dir:" + f.getAbsoluteFile() );
			} else {
				if(f.getName().contains(( "feature" ))) {
					collectScenariosFromFile(f);
					if(f.getName().equals("CorrectPINReset.feature")) {

						System.out.println("File moved to end:" + f.getAbsoluteFile());
						pinReset = f;
					}
					if(f.getName().equals("DisablePIN.feature")) {

						System.out.println("File moved to end:" + f.getAbsoluteFile());
						pinDisable = f;
					}
					// System.out.println( "File:" + f.getAbsoluteFile() );
				}
			}
		}
	}

	public static void collectScenariosFromFile(File f) {

		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try {

			isr = new InputStreamReader(new FileInputStream(f));
			br = new BufferedReader(isr);

			while(( line = br.readLine() ) != null) {
				if(line.contains("Scenario:")) {
					String firstLine = br.readLine().trim();
					int i = firstLine.indexOf(' ');
					firstLine = firstLine.substring(i).trim();

					if(firstLine.equals("I log in and sync")) {
						REQUIRE_LOGIN.add(f);
						return;
					} else {
						ALL_THE_REST.add(f);
						return;
					}

				}

			}
			br.close();
			isr.close();

		} catch(Exception e) {
			// suck it!!!!
		}
	}

}
