package modules;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

/**
 * class that extends textFlow, shows messages moving vertically across scrollPane<br>
 * use {@link #setMessages(String[])} to set the messages of this pane<br>
 * CSS - Text class=(text-id)
 *
 */
public class MovingMessage extends TextFlow{
private final DoubleProperty screenHight;
	Timeline t;
	
	/**
	 * create moving message and apply it to the given ScrollPane
	 * @param scroll {@link ScrollPane} the container of the message
	 */
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
		screenHight = new SimpleDoubleProperty(scroll.getPrefHeight());
	}
	
	/**
	 * set the messages for the moving message pane
	 * @param messages messages to show
	 */
	public void setMessages(String[] messages) {
		if(messages == null)
			return;
		this.getChildren().clear();
		for(String s : messages) {
			Text t = new Text(s+"\n");
			t.getStyleClass().add("text-id");
			this.getChildren().add(t);
		}
		t.rateProperty().bind(screenHight.divide(this.heightProperty()));
	}
	
}
