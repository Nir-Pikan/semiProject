package modules;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Class for replacing the console with {@link ScrollPane}
 *
 */
public class ConsoleReplacer {

	private TextFlow flow;

	/**
	 * set the given {@link ScrollPane} to receive the console
	 * 
	 * @param scroll - {@link ScrollPane} to add the console to
	 */
	public ConsoleReplacer(ScrollPane scroll) {
		flow = new TextFlow();
		scroll.setContent(flow);
		scroll.vvalueProperty().bind(flow.heightProperty());
	}

	/**
	 * Set this replacer to replace\copy the {@link System#out}
	 * 
	 * @param toCopyOrigin - show the console logs in the original logs
	 */
	public void replaceSystemOut(boolean toCopyOrigin) {
		MyStream out = new MyStream(toCopyOrigin, System.out, false);
		System.setOut(out);

	}

	/**
	 * Set this replacer to replace\copy the {@link System#err}
	 * 
	 * @param toCopyOrigin - show the console logs in the original logs
	 */
	public void replaceSystemErr(boolean toCopyOrigin) {
		MyStream err = new MyStream(toCopyOrigin, System.err, true);
		err.setColor(Color.RED);
		System.setErr(err);
	}

	/**
	 * Set this replacer to replace\copy the {@link System#out} and
	 * {@link System#err}
	 * 
	 * @param toCopyOrigin - show the console logs in the original logs
	 */
	public void replaceAll(boolean toCopyOrigin) {
		replaceSystemOut(toCopyOrigin);
		replaceSystemErr(toCopyOrigin);
	}

	/**
	 * Private class, for PrintStream, that shows their prints in the
	 * ScrollPane({@link ConsoleReplacer}
	 * 
	 * @see PrintStream
	 *
	 */
	private class MyStream extends PrintStream {

		private boolean toCopy;
		private boolean isErr;
		private PrintStream origin;
		private Paint color;

		/**
		 * create {@link PrintStream} for the given source {@link PrintStream}
		 * 
		 * @param toCopy - show the console logs in the original logs
		 * @param origin - original {@link PrintStream} for copy
		 * @param isErr  - is err (for "\n" in {@link #print(String)}s)
		 */
		public MyStream(boolean toCopy, PrintStream origin, boolean isErr) {
			super(new OutputStream() {

				@Override
				public void write(int b) throws IOException {
				}
			});
			this.toCopy = toCopy;
			this.origin = origin;
			color = null;
			this.isErr = isErr;
		}

		//do not add javadoc here
		@Override
		public void print(String s) {
			if (isErr) {
				println(s);
				return;
			}
			Platform.runLater(() -> {
				Text t = new Text(s);
				if (color != null)
					t.setFill(color);
				flow.getChildren().add(t);
			});
			if (toCopy)
				origin.print(s);
		}

		//do not add javadoc here
		@Override
		public void println(String s) {
			Platform.runLater(() -> {
				Text t = new Text(s + "\n");
				if (color != null)
					t.setFill(color);
				flow.getChildren().add(t);
			});
			if (toCopy)
				origin.println(s);

		}

		/**
		 * set the color of the text created by this
		 * @param color {@link Color} for the text
		 */
		public void setColor(Paint color) {
			this.color = color;
		}

	}
}
