package view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Point;
import other.OnClick;
import other.OnClickListener;
import javafx.stage.Stage;

/**
 * Sets up a user interface which displays a combined view, 
 * containing three different views, with which the user can 
 * interact: a view for playing music, a view for displaying text at
 * random positions, and a view for displaying a rotating triangle 
 * shape.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class CombinedView extends VBox implements OnClickListener 
{	
	private Stage stage;

	private MusicView musicView;
	private DisplayView displayView;
	private RotateView rotateView;

	private OnClickListener onClickListener;

	public CombinedView(Stage stage) 
	{
		// The FileChooser needs a reference to Stage
		this.stage = stage;
	}

	/**
	 * Design a view containing the three different view: MusicView,
	 * DisplayView, and a RotateView.
	 */
	public void initializeView() 
	{
		musicView = new MusicView();
		musicView.initializeView();
		musicView.setOnViewClicked(this);

		displayView = new DisplayView();
		displayView.initializeView();
		displayView.setOnViewClicked(this);

		rotateView = new RotateView();
		rotateView.initializeView();
		rotateView.setOnViewClicked(this);

		HBox displayAndRotateView = new HBox(20);
		displayAndRotateView.getChildren().addAll(displayView, rotateView);

		this.setSpacing(10);
		this.setPadding(new Insets(20));
		this.getChildren().addAll(musicView, displayAndRotateView);		
	}

	/** 
	 * We can now let the Controller know when a specific button has 
	 * been pressed. 
	 */
	public void setOnComboViewClicked(OnClickListener onClickListener) 
	{
		this.onClickListener = onClickListener;
	}

	/** 
	 * This method gets called by the ComboViews different views when 
	 * a button in one of these views has been pressed (see
	 * MusicView, DisplayView, and RotateView in package view). 
	 */
	@Override
	public void setOnViewClicked(OnClick onClick) 
	{
		onClickListener.setOnViewClicked(onClick);
	}

	/**
	 * Opens a file chooser, initializes and displays the file 
	 * chooser. The method returns the mp3 file selection made.
	 * 
	 * @return the selected file, returns null if a selection was
	 * not made
	 */
	public File openMp3FileChooser() 
	{
		FileChooser fileChooser = new FileChooser();
		initializeFileChooser(fileChooser);

		return fileChooser.showOpenDialog(stage);
	}

	/** 
	 * Initialize the file chooser by setting title and making sure 
	 * that only mp3 files can be selected. 
	 */
	private void initializeFileChooser(FileChooser fileChooser) 
	{
		fileChooser.setTitle("Open Mp3 song");		
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Mp3", "*.mp3"));	
	}

	/** 
	 * When the open button in MusicView is pressed, notify the 
	 * view. 
	 */
	public void setOnMusicOpenClicked(File selectedFile) 
	{
		musicView.setOnMusicOpenClicked(selectedFile);
	}

	/** 
	 * When the play button in MusicView is pressed, notify the 
	 * view. 
	 */
	public void setOnMusicPlayClicked() 
	{
		musicView.setOnMusicPlayClicked();
	}

	/** 
	 * When the stop button in MusicView is pressed, notify the 
	 * view. 
	 */
	public void setOnMusicStopClicked() 
	{
		musicView.setOnMusicStopClicked();
	}

	public void display(String text, Point point) 
	{
		displayView.display(text, point);
	}

	/** 
	 * When the start button in DisplayView is pressed, notify the 
	 * view. 
	 */
	public void setOnStartDisplayClicked() 
	{
		displayView.setOnStartDisplayClicked();
	}

	/** 
	 * When the stop button in DisplayView is pressed, notify the 
	 * view. 
	 */
	public void setOnStopDisplayClicked() 
	{
		displayView.setOnStopDisplayClicked();
	}

	public void rotate(int degree) 
	{
		rotateView.rotate(degree);
	}

	/** 
	 * When the start button in RotateView is pressed, notify the 
	 * view. 
	 */
	public void setOnStartRotateClicked() 
	{
		rotateView.setOnStartRotateClicked();
	}

	/** 
	 * When the stop button in RotateView is pressed, notify the 
	 * view. 
	 */
	public void setOnStopRotateClicked() 
	{
		rotateView.setOnStopRotateClicked();
	}
}
