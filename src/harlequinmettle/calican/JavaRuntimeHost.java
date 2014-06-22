package harlequinmettle.calican;

import harlequinmettle.overrides.EnterButton;
import harlequinmettle.overrides.JScrollPanelledPane;
import harlequinmettle.overrides.SplitPaneTabbedGUI;
import harlequinmettle.utilities.Maker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

public class JavaRuntimeHost {

	JFrame app = new JFrame("Interactive Calabash");

	SplitPaneTabbedGUI gui = new SplitPaneTabbedGUI();
	JScrollPanelledPane console = Maker.makeScrollPane();
	JTextArea text = new JTextArea();
	EnterButton buttonSubmit = Maker.makeConsoleEnterButton(text, this);

	ProcessBuilder pb = new ProcessBuilder().command("irb");
	Process p = null;
	BufferedWriter writer = null;
	OutputStream outS = null;

	public static void main(String[] args) throws ScriptException,
			FileNotFoundException {
		JavaRuntimeHost jrh = new JavaRuntimeHost();
		// scriptManager();
		// testCommonsExec();
		// testCommontsExec2("irb");
	}

	public void startCommonsCmd() {
		CommandLine cmdLine = new CommandLine("irb");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(stdout);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
		Executor executor = new DefaultExecutor();
		executor.setStreamHandler(psh);
		executor.setWatchdog(watchdog);
		try {
			executor.execute(cmdLine);
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testArgumentQuoting() throws Exception {
		DefaultExecutor executor = new DefaultExecutor();
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(out, out);
		executor.setStreamHandler(streamHandler);
		CommandLine cmdLine = new CommandLine("java");
		cmdLine.addArgument("what version");
		executor.execute(cmdLine, resultHandler);
		resultHandler.waitFor();

		String res = out.toString();
	}

	public String execToString(String command) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CommandLine commandline = CommandLine.parse(command);
		DefaultExecutor exec = new DefaultExecutor();
		// ///***

		PipedOutputStream stdout = new PipedOutputStream();
		PipedOutputStream stderr = new PipedOutputStream();
		PipedInputStream stdin = new PipedInputStream();
		AutoFlushingPumpStreamHandler streamHandler = new AutoFlushingPumpStreamHandler(
				stdout, stderr, stdin);

		exec.setStreamHandler(streamHandler);
		//
		// BufferedInputStream processOutput = new BufferedInputStream(new
		// PipedInputStream(stdout));
		// BufferedInputStream processError = new BufferedInputStream(new
		// PipedInputStream(stderr));
		// BufferedOutputStream processInput = new BufferedOutputStream(new
		// PipedOutputStream(stdin));

		// ///*****
		exec.setWorkingDirectory(new File("/home/andrew"));
		exec.setStreamHandler(streamHandler);
		exec.setExitValue(139);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		exec.execute(commandline, resultHandler);
		// exec.execute(commandline).waitFor();
		// // get the output only after the program has finished.
		// return(outputStream.toString());
		resultHandler.waitFor();
		return (outputStream.toString());
	}

	private static void testCommontsExec2(String command) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CommandLine commandline = CommandLine.parse(command);
		DefaultExecutor exec = new DefaultExecutor();
		// exec.setWorkingDirectory(new File("/home/vigna"));
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		exec.setStreamHandler(streamHandler);
		exec.setExitValue(139);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		try {
			exec.execute(commandline, resultHandler);
			resultHandler.waitFor();
		} catch (Exception e) {
		}
		System.out.println(outputStream.toString());
	}

	public static void testCommonsExec() {

		DefaultExecutor executor = new DefaultExecutor();

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PumpStreamHandler streamHandler = new PumpStreamHandler(out, out);

		executor.setStreamHandler(streamHandler);

		CommandLine cmdLine = new CommandLine("irb");

		cmdLine.addArgument(" -v");

		try {

			executor.execute(cmdLine, resultHandler);

			resultHandler.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JavaRuntimeHost() {
		setupGui();
		// "gnome-terminal"
		try {
			p = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToRuntimeOutputStream(String command) {

		if (p == null) {
			System.out.println("process is null");
			return;
		}
		//outS = (BufferedOutputStream) p.getOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
				"ERROR");

		// any output?
		StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
				"OUTPUT");

		// start gobblers
		outputGobbler.start();
		errorGobbler.start();

		try {
			outS.write((command).getBytes(Charset.forName("UTF-8")));
			// outS.write((command ).getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToRuntimeWriter(String input) {
		try {

			writer.write(input);

			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setupGui() {
		console.addComp(text, buttonSubmit);
		gui.PANES.put("Console", console);
		gui.updateTabs();
		app.add(gui);
		app.setSize(900, 700);
		app.setVisible(true);
	}

	private class StreamGobbler extends Thread {
		InputStream is;
		String type;

		private StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		@Override
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				int count = 0;
				while ((line = br.readLine()) != null) {
					gui.textLower.insert("\n" + line,
							gui.textLower.getCaretPosition());
					System.out
							.println("reading output of process   " + count++);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public static void scriptManager() {
		String execute = new String(
				"calabash-android console C:\\Users\\aparelius\\svn_repo_tablet\\FreeTablet\\bin\\FreeTablet.apk'");
		String rubyFile = new String(
				"C:\\Users\\aparelius\\Downloads\\calabash-android-master\\ruby-gem\\bin\\calabash-android-console.rb");
		// JavaRuntimeHost test = new JavaRuntimeHost();

		// String filename = "my-analyzer.rb";

		ScriptEngineManager manager = new ScriptEngineManager();
		System.out.println(manager.getEngineFactories());
		// if(true) return;
		ScriptEngine engine = manager.getEngineByName("jruby");
		// Reader reader = new FileReader(filename);
		try {
			engine.eval(execute);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Instantiate the JRuby class, and cast the result of eval.
		// Analyzer analyzer = (Analyzer) engine.eval("MyAnalyzer.new");
	}

}
