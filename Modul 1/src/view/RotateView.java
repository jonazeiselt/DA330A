package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import other.OnClick;
import other.OnClickListener;

/**
 * Sets up a view for displaying a rotating triangle shape within a 
 * pane, and a view containing buttons for starting and stopping
 * the rotate thread (see Controller in package controller).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class RotateView extends TitledPane 
{
	private Pane rotateView;
	private Polygon triangle;
	
	private OnClickListener onClickListener;
	private Button startButton;
	
	public void initializeView() 
	{
		this.setText("Triangle Thread");
		this.setCollapsible(false);
		
		VBox root = getRootView();
		this.setContent(root);
	}

	/** 
	 * Design a view containing a pane, in which a triangle shape is
	 * shown, and another view containing two buttons.
	 */
	private VBox getRootView() 
	{
		rotateView = new Pane();
		rotateView.setPrefSize(200,300);
		rotateView.setStyle("-fx-border-color: #cccccc;");
		triangle = new Polygon();
		triangle.getPoints().addAll(new Double[] 
				{
						0.0, 0.0,
						70.0, 10.0,
						10.0, 70.0
				});

		rotateView.getChildren().add(triangle);
		
		triangle.setLayoutX(85); // experimental value
		triangle.setLayoutY(100); // experimental value
		triangle.setFill(Color.GAINSBORO);
		triangle.setStroke(Color.GREY);
	
		HBox buttonView = getButtonView();

		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setPrefWidth(275);

		root.getChildren().addAll(rotateView, buttonView);
		return root;
	}

	/** Design a view containing two buttons. */
	private HBox getButtonView() 
	{
		startButton = new Button("Start Rotate");
		Button stopButton = new Button("Stop");

		// Notify ComboView that start button was pressed
		startButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.ROTATE);
		});
		
		// Notify ComboView that stop button was pressed
		stopButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.STOPROTATE);
		});
		
		HBox view = new HBox(10);
		view.getChildren().addAll(startButton, stopButton);

		return view;
	}

	/** 
	 * Remove existing triangle shape and display a new one at the 
	 * same position but with a different angle. 
	 */
	public void rotate(int degree) 
	{
		rotateView.getChildren().remove(0);
		rotateView.getChildren().add(triangle);
		
		triangle.setRotate(degree);
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
	public void setOnStartRotateClicked() 
	{
		startButton.setDisable(true);
	}

	/** When the stop button is pressed, enable the start button. */
	public void setOnStopRotateClicked() 
	{
		startButton.setDisable(false);	
	}
}
