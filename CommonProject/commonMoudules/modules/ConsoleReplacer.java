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

//TODO add javadoc
public class ConsoleReplacer {

	private TextFlow flow;
	
	public ConsoleReplacer(ScrollPane scroll) {
		flow = new TextFlow();
		scroll.setContent(flow);
		scroll.vvalueProperty().bind(flow.heightProperty());
	}
	
	public void replaceSystemOut(boolean toCopyOrigin) {
		MyStream out=new MyStream(toCopyOrigin, System.out,false);
		System.setOut(out);
		
	}
	
	public void replaceSystemErr(boolean toCopyOrigin) {
		MyStream err=new MyStream(toCopyOrigin, System.err,true);
		err.setColor(Color.RED);
		System.setErr(err);
	}
	
	public void replaceAll(boolean toCopyOrigin) {
		replaceSystemOut(toCopyOrigin);
		replaceSystemErr(toCopyOrigin);
	}
	
	private class MyStream extends PrintStream{

		private boolean toCopy;
		private boolean isErr;
		private PrintStream origin;
		private Paint color;
		public MyStream(boolean toCopy,PrintStream origin,boolean isErr) {
			super(new OutputStream() {
				
				@Override
				public void write(int b) throws IOException {
				}
			});
			this.toCopy = toCopy;
			this.origin = origin;
			color = null;
			this.isErr=isErr;
		}
		
		@Override
		public void print(String s) {
			if(isErr) {println(s);return;}
			Platform.runLater(()->{
				Text t = new Text(s);
				if(color!=null)
					t.setFill(color);
				flow.getChildren().add(t);
				});
			if (toCopy)
				origin.print(s);
		}
		
		@Override
		public void println(String s) {
			Platform.runLater(()->{
				Text t = new Text(s+"\n");
				if(color!=null)
					t.setFill(color);
				flow.getChildren().add(t);
				});
			if (toCopy)
				origin.println(s);
			
		}
		
		public void setColor(Paint color) {
			this.color = color;
		}
		
		
	}
}
