package harlequinmettle.utilities;
public class Feedback {
	static long startTime = System.currentTimeMillis();
	static long updateTime = System.currentTimeMillis();
	public static float totalMem;
	public static int filesMoved;

	public static void compareTime(String msg) {

		long mTime = System.currentTimeMillis();
		float totalTime = (mTime - startTime) / 1000.0f;
		float iter = (mTime - updateTime) / 1000.0f;

		System.out.println("b/s: 			"
				+ String.format("%.3f", totalMem / totalTime));
		System.out
				.println(String.format("%.3f", totalTime) + "   elapsed time");
		System.out.println(String.format("%.3f", iter) + "   " + filesMoved
				+ "     " + msg+"\n");
		updateTime = mTime;
	}

}
