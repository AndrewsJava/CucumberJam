package harlequinmettle.utilities;

import harlequinmettle.calican.Cuker;
import harlequinmettle.interfaces.IParserKeywords;
import harlequinmettle.overrides.EnterButton;
import harlequinmettle.someobjects.InViewItem;
import harlequinmettle.someobjects.ProgramSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

//Class to serialize objects
public class FileMemoryManager {

	private static final String SETTINGS_OBJECT_PATH = ".cucumber_jam_program_settings";

	public static void saveSettings() {
		memorizeObject(Cuker.programSettings, SETTINGS_OBJECT_PATH);
	}

	// /////////// ////////////////////////////
	public static ProgramSettings restoreSettings() {
		ProgramSettings settings = null;
		try {
			FileInputStream filein = new FileInputStream(SETTINGS_OBJECT_PATH);
			ObjectInputStream objin = new ObjectInputStream(filein);
			try {
				settings = (ProgramSettings) objin.readObject();
			} catch (ClassCastException cce) {
				System.out.println("CLASSCASTEXCEPTION");
			}
			objin.close();
		} catch (Exception ioe) {
			System.out.println("NO resume: saver");
		}
		if (settings == null)
			settings = new ProgramSettings();
		return settings;
	} //

	public static void memorizeObject(Object ob, String obFileName) {
		System.out.println("memorizing object ... ");
		File nextFile = new File(obFileName);
		// nextFile.mkdirs();//in case they don't exist
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

	// built in to read all text files in folder
	public static void readCalabashDataFiles(ProgramSettings programSettings) {

		String[] paths = programSettings.pathToTextOutputFiles.split(";");
		for (String path : paths) {
			File f = new File(path);
			if (!f.exists())
				continue;
			String[] files = new File(path).list();
			if (files == null) {
				continue;
			}
			for (String textFile : files) {

				InputStreamReader isr = null;
				BufferedReader br = null;
				String line = "";
				try {
					String fullPath = path + File.separator + textFile;

					isr = new InputStreamReader(new FileInputStream(fullPath));
					br = new BufferedReader(isr);
					// make into buttons (action: append canned steps with
					// id/text)
					Cuker.buttonPanelMap.put(textFile,
							new ArrayList<EnterButton>());
					int totalCount = 0;
					int useCount = 0;
					while ((line = br.readLine()) != null) {
						Thread.yield();
						if (line.contains("[1;37m[")) {

							InViewItem calabashItem = new InViewItem();
							totalCount++;

							for (String key : IParserKeywords.KEYWORDS) {
								if (line.contains(key)) {
									String[] parts = line.split("\"");

									if (parts.length > 3) {
										if (parts[1].equals("id")) {
											calabashItem.setId(parts[3]);
										}
										if (parts[1].equals("text")) {
											calabashItem.setText(parts[3]);
										}
										if (parts[1]
												.equals("contentDescription")) {
											calabashItem
													.setContentDescription(parts[3]);
										}
										if (parts[1].equals("class")) {
											if (!programSettings.androidViewTypes
													.containsKey(parts[3]))
												programSettings.androidViewTypes
														.put(parts[3], true);
											calabashItem
													.setAndroidObjectClassName(parts[3]);
										}
									}

								}
								if ( // is id or text size>0
								(calabashItem.retrieveId().trim().length() > 0 || calabashItem
										.retrieveText().trim().length() > 0)
										// are settings set to ignore this class
										&& Cuker.programSettings.androidViewTypes
												.get(calabashItem
														.getFullClassName())) {
									useCount++;
									EnterButton textGenerator = Maker
											.makeCalabashObjectButton(
													calabashItem,
													Cuker.gui.textLower,
													totalCount, useCount);

									Cuker.buttonPanelMap.get(textFile).add(
											textGenerator);
								}
							}
						}
					}
					br.close();
					isr.close();

				} catch (Exception e) {
					// suck it!!!!
				}
			}
		}
	}

	// built in to read all text files in folder
	public static void readCalabashDataFiles2(ProgramSettings programSettings) {

		String[] paths = programSettings.pathToTextOutputFiles.split(";");
		for (String path : paths) {
			File f = new File(path);
			if (!f.exists())
				continue;
			String[] files = new File(path).list();
			if (files == null) {
				continue;
			}
			for (String textFile : files) {

				InputStreamReader isr = null;
				BufferedReader br = null;
				String line = "";
				try {
					String fullPath = path + File.separator + textFile;

					isr = new InputStreamReader(new FileInputStream(fullPath));
					br = new BufferedReader(isr);
					// make into buttons (action: append canned steps with
					// id/text)
					Cuker.buttonPanelMap.put(textFile,
							new ArrayList<EnterButton>());
					InViewItem calabashItem = new InViewItem();
					int count = 0;
					int counta = 0;
					while ((line = br.readLine()) != null) {
						Thread.yield();
						// if (line.contains("[1;37m"))

						for (String key : IParserKeywords.KEYWORDS) {
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
										calabashItem.setId(parts[3]);
									}
									if (parts[1].equals("text")) {
										calabashItem.setText(parts[3]);
									}
									if (parts[1].equals("contentDescription")) {
										calabashItem
												.setContentDescription(parts[3]);
									}
									if (parts[1].equals("class")) {
										if (!programSettings.androidViewTypes
												.containsKey(parts[3]))
											programSettings.androidViewTypes
													.put(parts[3], true);
										calabashItem
												.setAndroidObjectClassName(parts[3]);
									}
								}

							}
							//
							if (line.contains("[1;37m[")
									|| line.contains("irb(main)")) {
								if (!(calabashItem.retrieveId() == null && calabashItem
										.retrieveText() == null)
										&& (calabashItem.retrieveId().trim()
												.length() > 0 || calabashItem
												.retrieveText().trim().length() > 0)
										&& Cuker.programSettings.androidViewTypes
												.get(calabashItem
														.getFullClassName())) {
									// IS ITEMS CLASS WITHIN SETTINGS????
									counta++;
									EnterButton textGenerator = Maker
											.makeCalabashObjectButton(
													new InViewItem(calabashItem),
													Cuker.gui.textLower, count,
													counta);

									Cuker.buttonPanelMap.get(textFile).add(
											textGenerator);
									// if (isValidObject(calabashItem))
									// calabashObjects.add(calabashItem);
								}
								count++;
								calabashItem = new InViewItem();
							}
						}
					}
					br.close();
					isr.close();

				} catch (Exception e) {
					// suck it!!!!
				}
			}
		}
	}

	//
	public static void loadCalabashFileTabData(ProgramSettings programSettings,
			String fullPath) {

		InViewItem itemForLastOddButton = new InViewItem();
		String fileText = "";
		try {
			fileText = FileUtils.readFileToString(new File(fullPath));
			Cuker.RAW_TEXT.put(fullPath, fileText);

		} catch (Exception e) {
			// suck it!!!!
		}

		Cuker.buttonPanelMap.put(fullPath, new ArrayList<EnterButton>());
		int totalCount = 0;
		int useCount = 0;
		String[] caliObjText = fileText.split("\\[1;37m\\[");
		for (int i = 0; i < caliObjText.length; i++) {
			String objText = caliObjText[i];
			Thread.yield();
			// Starting a new  
String [] lines = objText.split("\n");
				InViewItem calabashItem = new InViewItem();
				itemForLastOddButton = calabashItem;
				totalCount++;

				for(String line : lines )  {
					for (String key : IParserKeywords.KEYWORDS) {
						if (line.contains(key)) {
							String[] parts = line.split("\"");

							if (parts.length > 3) {
								if (parts[1].equals("id")) {
									calabashItem.setId(parts[3]);
								}
								if (parts[1].equals("text")) {
									calabashItem.setText(parts[3]);
								}
								if (parts[1].equals("contentDescription")) {
									calabashItem
											.setContentDescription(parts[3]);
								}
								if (parts[1].equals("class")) {
									if (!programSettings.androidViewTypes
											.containsKey(parts[3]))
										programSettings.androidViewTypes.put(
												parts[3], true);
									calabashItem
											.setAndroidObjectClassName(parts[3]);
								}
							}
						}
					}
				}
//				 System.out.println("obj: " +
//				 calabashItem.getFullClassName());
//				 System.out.println("txt: " + calabashItem.retrieveText());
//				 System.out.println("idi: " + calabashItem.retrieveId() +
//				 "\n");
//		 
//					//e.printStackTrace(); 
//					 System.out.println("-->: " + Cuker.programSettings ); 
//					 System.out.println("-->: " + Cuker.programSettings.androidViewTypes); 
//					 System.out.println("-->: " + calabashItem ); 
//					 System.out.println("-->: " + calabashItem.getFullClassName());
//					 System.out.println("-->: " + Cuker.programSettings.androidViewTypes
		//						.get(calabashItem.getFullClassName() )); 
//					Cuker.programSettings.androidViewTypes
//					.put(calabashItem.getFullClassName(),true);
				 
				if ( // is id or text size>0
				(calabashItem.retrieveId().trim().length() > 0 || calabashItem
						.retrieveText().trim().length() > 0)
						// are settings set to ignore this class
						&& Cuker.programSettings.androidViewTypes
						.get(calabashItem.getFullClassName())) {
					useCount++;
					// EnterButton textGenerator = Maker
					// .makeCalabashObjectButton(calabashItem,
					// Cuker.gui.textLower, totalCount,
					// useCount);
					EnterButton textGenerator = Maker
							.makeCalabashObjectWindowedOptionsButton(
									calabashItem, Cuker.gui.textLower,
									totalCount, useCount);
					Cuker.buttonPanelMap.get(fullPath).add(textGenerator);
				}
			} 
		// System.out.println(fullPath);
		// for (EnterButton eb : Cuker.buttonPanelMap.get(fullPath))
		// System.out.println("\n"+eb.getCalabashTitle());
		EnterButton oddDumy = Maker
				.makeCalabashObjectWindowedOptionsButton(itemForLastOddButton,
						Cuker.gui.textLower, totalCount, useCount);
		Cuker.buttonPanelMap.get(fullPath).add(oddDumy);
	}

	//
	public static void loadCalabashFileTabData2(
			ProgramSettings programSettings, String fullPath) {

		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try {

			isr = new InputStreamReader(new FileInputStream(fullPath));
			br = new BufferedReader(isr);
			// make into buttons (action: append canned steps with
			// id/text)
			// String fileName = new File(fullPath).getName();
			Cuker.buttonPanelMap.put(fullPath, new ArrayList<EnterButton>());
			InViewItem calabashItem = new InViewItem();
			int count = 0;
			int counta = 0;
			while ((line = br.readLine()) != null) {
				Thread.yield();
				// if (line.contains("[1;37m"))

				for (String key : IParserKeywords.KEYWORDS) {
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
								calabashItem.setId(parts[3]);
							}
							if (parts[1].equals("text")) {
								calabashItem.setText(parts[3]);
							}
							if (parts[1].equals("contentDescription")) {
								calabashItem.setContentDescription(parts[3]);
							}
							if (parts[1].equals("class")) {
								if (!programSettings.androidViewTypes
										.containsKey(parts[3]))
									programSettings.androidViewTypes.put(
											parts[3], true);
								calabashItem
										.setAndroidObjectClassName(parts[3]);
							}
						}
						// is enabled
						// is button, edittext, textview, etc -
					}

					if (line.contains("[1;37m[")) {
						if (!(calabashItem.retrieveId() == null && calabashItem
								.retrieveText() == null)
								&& (calabashItem.retrieveId().trim().length() > 0 || calabashItem
										.retrieveText().trim().length() > 0)
								&& Cuker.programSettings.androidViewTypes
										.get(calabashItem.getFullClassName())) {
							// IS ITEMS CLASS WITHIN SETTINGS????
							counta++;
							EnterButton textGenerator = Maker
									.makeCalabashObjectButton(new InViewItem(
											calabashItem), Cuker.gui.textLower,
											count, counta);

							Cuker.buttonPanelMap.get(fullPath).add(
									textGenerator);
							// if (isValidObject(calabashItem))
							// calabashObjects.add(calabashItem);
						}
						count++;
						calabashItem = new InViewItem();
					}
				}
			}
			br.close();
			isr.close();

		} catch (Exception e) {
			// suck it!!!!
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

	public static void loadStepDefinitions(File dr) {

		String[] lines = null;
		try {

			lines = FileUtils.readFileToString(dr).split("\n");

		} catch (IOException e) {
			return;
		}
		loadStepsFromStringArray(lines);
	}

	public static void loadStepsFromStringArray(String[] lines) {

		// ArrayList<String> macros = new ArrayList<String>();
		int lineCounter = 0;

		String def = "";
		for (String a : lines) {
			lineCounter++;
			if (a.contains("(/^") || a.contains("/^")) {
				try {
					def = a.substring(a.indexOf("/^") + 2, a.indexOf("$/"));
					Cuker.programSettings.LINE_NUMBERS.put(def, lineCounter);
				} catch (Exception e) {
					System.out.println("Error: "+a);
				}
				// macros.add(def);
				if (Cuker.programSettings.STEPS.containsKey(def)) {
					System.out
							.println("\n\n\nDUPLICATE CALABASH DEFINITION\n****\n***\n***\n***"
									+ def);
				}
				if (!Cuker.programSettings.cannedStepsInclusion
						.containsKey(def))
					Cuker.programSettings.cannedStepsInclusion.put(def, true);
				Cuker.programSettings.STEPS.put(def, new ArrayList<String>());
			} else if (isValidStep(a)) {
				Cuker.programSettings.STEPS.get(def).add("#" + a);
			}
			// /
			// C:\Users\aparelius\svn_repo_tablet\FreeTablet\features\step_definitions
		}

	}

	public static void loadStepDefinitionsFromUrl(String s) {
		URL git;

		try {
			git = new URL(s);

			URLConnection yc = git.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				Cuker.programSettings.stepsFromGitHub += (inputLine) + "\n";
			in.close();
		} catch (Exception e) {
		}
		loadStepsFromStringArray(Cuker.programSettings.stepsFromGitHub.split("\n"));
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

}
