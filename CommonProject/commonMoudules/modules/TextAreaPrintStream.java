package modules;



import java.io.*;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
/**
 * Class TextAreaPrintStream<br>
 * extends PrintStream.<br>
 * A custom made PrintStream which overrides methods println(String)
 * and print(String).<br>
 * Thus, when the out stream is set as this PrintStream (with System.setOut
 * method), all calls to System.out.println(String) or System.out.print(String)
 * will result in an output stream of characters in the TextArea given as an
 * argument of the constructor of the class.<br>
 * call {@link #setShowInOriginalSystemOut()} to show the content in the original System.Out
 **/
public class TextAreaPrintStream extends PrintStream {

	private TextArea textArea;
	private PrintStream source;
	private boolean toCopy = false;

	/**
	 * Method TextAreaPrintStream The constructor of the class.
	 * 
	 * @param the TextArea to wich the output stream will be redirected.
	 * @param a   standard output stream (needed by super method)
	 **/
	public TextAreaPrintStream(TextArea area, OutputStream out) {
		super(out);
		textArea = area;
		source = System.out;
	}

	/**
	 * Method println
	 * 
	 * @param the String to be output in the TextArea textArea (private attribute of
	 *            the class). After having printed such a String, prints a new line.
	 **/
	public void println(String string) {
		Platform.runLater(()->{textArea.appendText(string + "\n");});
		if (toCopy)
			source.println(string);
	}

	/**
	 * Method print
	 * 
	 * @param the String to be output in the TextArea textArea (private attribute of
	 *            the class).
	 **/
	public void print(String string) {
		Platform.runLater(()->{textArea.appendText(string);});
		if (toCopy)
			source.print(string);
	}
	
	/**
	 * set this steam to show the value also in the original System.out
	 */
	public void setShowInOriginalSystemOut() {
		toCopy = true;
	}
}