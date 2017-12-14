package view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import other.*;

/**
 * Sets up a view for displaying a status message and a message
 * containing the file path of a loaded mp3 file. It also displays a
 * a view containing buttons for opening a file chooser, starting, 
 * and stopping the music thread (see Controller in package 
 * controller).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class MusicView extends TitledPane
{
	private OnClickListener onClickListener;
	
	private Button openButton;
	private Button playButton;
	
	private Label infoLabel;
	private Label loadLabel;

	public void initializeView() 
	{
		this.setText("Music Thread");
		this.setCollapsible(false);
		
		VBox root = getRootView();
		this.setContent(root);
	}

	/** Design a view containing two labels and three buttons. */
	private VBox getRootView() 
	{
		VBox labelView = getLabelView();
		HBox buttonView = getButtonView();
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));

		root.getChildren().addAll(labelView, buttonView);
		return root;
	}

	/** Design a view containing two labels. */
	private VBox getLabelView() 
	{
		infoLabel = new Label("Sleeping...");
		infoLabel.setStyle(infoLabel.getStyle() + "-fx-font-weight: bold;");
		loadLabel = new Label();
		loadLabel.setWrapText(true);
		
		VBox view = new VBox(10);
		view.getChildren().addAll(infoLabel, loadLabel);
		
		return view;
	}

	/** Design a view containing three buttons. */
	private HBox getButtonView() 
	{
		openButton = new Button("Open");
		playButton = new Button("Play");
		Button stopButton = new Button("Stop");
		
		// Notify ComboView that open button was pressed
		openButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.OPEN);
		});
		
		// Notify ComboView that play button was pressed
		playButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.PLAY);
		});
		
		// Notify ComboView that stop button was pressed
		stopButton.setOnMouseClicked(e -> {
			onClickListener.setOnViewClicked(OnClick.STOPMUSIC);
		});
			
		HBox view = new HBox(10);
		view.getChildren().addAll(openButton, playButton, stopButton);
		
		return view;
	}

	/** 
	 * We can now let the ComboView know when a specific button has 
	 * been pressed. 
	 */
	public void setOnViewClicked(OnClickListener onClickListener) 
	{
		this.onClickListener = onClickListener;
	}
	
	/** When the open button is pressed, update view. */
	public void setOnMusicOpenClicked(File mp3File) 
	{
		if (mp3File != null) 
		{
			infoLabel.setText("Waiting to be played...");
			loadLabel.setText(mp3File.getAbsolutePath());
		}
		else 
		{
			infoLabel.setText("Sleeping...");
			loadLabel.setText("No mp3-file selection made");
		}
	}

	/** When the play button is pressed, update view. */
	public void setOnMusicPlayClicked() 
	{
		infoLabel.setText("Playing song...");		
		openButton.setDisable(true);
		playButton.setDisable(true);
	}

	/** When the stop button is pressed, update view. */
	public void setOnMusicStopClicked()
	{
		infoLabel.setText("Sleeping...");
		openButton.setDisable(false);
		playButton.setDisable(false);
	}
}
