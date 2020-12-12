package util;

import javafx.application.Platform;

/**class for creating GUI updates*/
public class UIupdate {

	/**<pre>execute code in the JavaFx thread
	 *use exemple:
	 * 	<code>UIupdate.update(()-&gt;{
	 * 		//code to run
	 * 	});</code></pre>
	 * @param toDo the code to run in form of Runnable(use labda for easier work) */
	public static void update(Runnable toDo) {
		Platform.runLater(toDo);
	}
}
