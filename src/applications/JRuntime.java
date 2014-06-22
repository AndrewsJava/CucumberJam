package applications;


import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class JRuntime {

	public static void main(String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("cmd");
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		//pb.redirectInput(Redirect.INHERIT);
		Process p = pb.start();
//pb.redirectInput(new File("/home/andrew/Desktop/JavaRuntimeInputCommand.txt"));
	}

}