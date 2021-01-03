package module;



import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class MovingMessage extends TextFlow{

	Timeline t;
	public MovingMessage( ScrollPane scroll){
		super();
		scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		DoubleProperty y = new SimpleDoubleProperty(0.0);
		t = new Timeline(new KeyFrame(Duration.seconds(7), new KeyValue(y, 1.0)));
		y.addListener((obs, oldValue, newValue) ->{
			scroll.setVvalue(newValue.doubleValue());
		});
		scroll.setOnMouseEntered((event)->{
			t.play();
		});
		scroll.setOnMouseExited((event)->{
			t.stop();
			y.set(0.0);
		});
		scroll.setContent(this);
		this.setLineSpacing(10);
	}
	
	public void setMessages(String[] messges) {
		this.getChildren().clear();
		for(String s : messges) {
			Text t = new Text(s+"\n");
			t.getStyleClass().add("text-id");
			this.getChildren().add(t);
		}
	}
	
}
