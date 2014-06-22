package applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.TreeMap;

public class StepStats {
 public static final TreeMap<String, Integer> ALL_STEPS = new TreeMap<String, Integer>();
 
	static String path = "\\Users\\aparelius\\svn_repo_tablet\\FreeTablet\\features";
 
	public static void main(String[] args) { 
		 
		readSteps(path);
	 
		System.out.println(ALL_STEPS.size());
		for(Entry<String,Integer> e: ALL_STEPS.entrySet()){

			System.out.println(e.getValue()+"           "+e.getKey());
		}
	}
 

	 

	 

	public static void readSteps(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		if(list == null)
			return;

		for(File f : list) {
			if(f.isDirectory()) {
				readSteps(f.getAbsolutePath());
 
			} else {
				if(f.getName().contains(( "feature" ))) {
					
					collectSFromFile(f);
			  
				}
			}
		}
	}

	public static void collectSFromFile(File f) {

		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try {

			isr = new InputStreamReader(new FileInputStream(f));
			br = new BufferedReader(isr);

			while(( line = br.readLine().trim() ) != null) {

				line = line.replaceAll("Then", "");
				line = line.replaceAll("And", "");
				line = line.replaceAll("When", "");
				line = line.replaceAll("Given", "");
				line = line.replaceAll("[\\\"][\\w][\\\"]", "HMMM");
				line = line.trim();
				if(!line.contains("Scenario:") && !line.contains("|")&& !line.contains("Feature:")) {
					 
					if(ALL_STEPS.containsKey(line)) {
						ALL_STEPS.put(line, ALL_STEPS.get(line)+1);
					 
					} else {
						ALL_STEPS.put(line, 1);
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
