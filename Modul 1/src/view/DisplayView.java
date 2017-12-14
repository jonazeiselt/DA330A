package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Point;
import other.OnClick;
import other.OnClickListener;

/**
 * Sets up a view for displaying a label within a pane at a random 
 * position, and a view containing buttons for starting and stopping
 * the display thread (see Controller in package controller).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class DisplayView extends TitledPane 
{
	private Pane displayView;

	private Label moveLabel;
	private Button startButton;
	
	private OnClickListener onClickListener;
	
	public void initializeView() 
	{
		this.setText("Display Thread");
		this.setCollapsible(false);
		
		VBox root = getRootView();
		this.setContent(root);
	}

	/** 
	 * Design a view containing a pane, in which a label should be 
	 * able to be shown, and another view containing two buttons.
	 */
	private VBox getRootView() 
	{
		displayView = new Pane();
		displayView.setPrefSize(200,300);
		displayView.setStyle("-fx-border-color: #cccccc;");
		HBox buttonView = getButtonView();
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setPrefWidth(275);

		root.getChildren().addAll(displayView, buttonView);
		return root;
	}

	/** Design a view containing two buttons. */
	private HBox getButtonView() 
	{
		startButton = new Button("Start Display");
		Button stopButton = new Button("Stop");
			
		// Notify ComboView that start button was pressed
		startButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.DISPLAY);
		});
		
		// Notify ComboView that stop button was pressed
		stopButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.STOPDISPLAY);
		});
		
		HBox view = new HBox(10);
		view.getChildren().addAll(startButton, stopButton);
		
		return view;
	}

	/** 
	 * Remove existing label if such one exists and display a new 
	 * one at the value of point. 
	 */
	public void display(String text, Point point) 
	{ 
		if (moveLabel != null)
			displayView.getChildren().remove(0);

		moveLabel = new Label(text);
		displayView.getChildren().add(moveLabel);
		
		moveLabel.setLayoutX(point.getX());
		moveLabel.setLayoutY(point.getY());
	}
	
	/** 
	 * We can now let the ComboView know when a specific button has 
	 * been pressed. 
	 */
	public void setOnViewClicked(OnClickListener onClickListener) 
	{
		this.onClickListener = onClickListener;
	}

	/** When the start button is pressed, disable the button. */
	public void setOnStartDisplayClicked() 
	{
		startButton.setDisable(true);
	}	
	
	/** When the stop button is pressed, enable the start button. */
	public void setOnStopDisplayClicked() 
	{
		startButton.setDisable(false);
	}	
}
