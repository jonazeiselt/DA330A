package controller;

import java.io.File;

import javafx.application.Platform;
import model.DisplayTask;
import model.MusicTask;
import model.Point;
import model.RotateTask;
import other.DisplayListener;
import other.MusicListener;
import other.OnClick;
import other.OnClickListener;
import other.RotateListener;
import view.CombinedView;

/**
 * Handles the user interaction with the three different views (see
 * java files in package view). Keeps track of the different threads,
 * including closing them when requested.
 * 
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class Controller implements OnClickListener, MusicListener, DisplayListener, RotateListener
{
	private CombinedView combinedView;

	private Thread musicThread;
	private Thread displayThread;
	private Thread rotateThread;

	private MusicTask musicTask;
	private DisplayTask displayTask;
	private RotateTask rotateTask;

	private File selectedFile;

	public Controller() {}

	public Controller(CombinedView combinedView) 
	{
		this.combinedView = combinedView;
	}

	public void initializeView() 
	{
		String defaultFontStyle = "-fx-font-family: 'Droid Sans'; "
				+ "-fx-font-size: 11pt;";

		combinedView.initializeView();
		combinedView.setStyle(defaultFontStyle);

		combinedView.setOnComboViewClicked(this);
	}

	@Override
	public void setOnViewClicked(OnClick onClick) 
	{
		switch (onClick) 
		{
		case OPEN:
			openMp3FileChooser();
			break;
		case PLAY:
			startMusicThread();
			break;
		case STOPMUSIC:
			stopMusicThread();
			break;
		case DISPLAY:
			startDisplayThread();
			break;
		case STOPDISPLAY:
			stopDisplayThread();
			break;
		case ROTATE:
			startRotateThread();
			break;
		case STOPROTATE:
			stopRotateThread();
		}
	}

	public void openMp3FileChooser() 
	{
		selectedFile = combinedView.openMp3FileChooser();
		combinedView.setOnMusicOpenClicked(selectedFile);		
	}

	/**
	 * Starts a new music thread if such one is not already running,
	 * and if a mp3 file has been chosen from the FileChooser.
	 */
	public void startMusicThread() 
	{
		if (musicThread == null && selectedFile != null) 
		{
			String mp3Path = selectedFile.toURI().toString();
			musicTask = new MusicTask(mp3Path);
			musicTask.setOnMusicCompleted(this);

			musicThread = new Thread(musicTask);
			musicThread.start();

			// Simulate a play music button click
			combinedView.setOnMusicPlayClicked();
		}
	}

	@Override
	public void onMusicCompleted() 
	{
		stopMusicThread();
	}

	public void stopMusicThread() 
	{
		// Stop if musicThread is running
		if (musicThread != null) 
		{
			musicTask.terminate();
			musicThread = null;

			// Simulate a stop music button click
			combinedView.setOnMusicStopClicked();
		}
	}

	public void startDisplayThread() 
	{
		// Start if displayThread is not already running
		if (displayThread == null) 
		{
			displayTask = new DisplayTask("Display Thread");
			displayTask.setOnDisplayUpdate(this);

			displayThread = new Thread(displayTask);
			displayThread.start();

			// Simulate a start display button click
			combinedView.setOnStartDisplayClicked();
		}
	}

	@Override
	public void setOnDisplay(String text, Point point) 
	{
		// We need Platform.runLater: setOnDisplay is called from a 
		// non-gui thread (DisplayTask + Thread)
		Platform.runLater(new Runnable() 
		{	
			@Override
			public void run() 
			{
				combinedView.display(text, point);
			}
		});
	}

	public void stopDisplayThread() 
	{
		// Stop if displayThread is running
		if (displayThread != null) 
		{
			displayTask.terminate();
			displayThread = null;

			// Simulate a stop display button click
			combinedView.setOnStopDisplayClicked();
		}
	}

	public void startRotateThread() 
	{
		// Start if rotateThread is not already running
		if (rotateThread == null) 
		{
			rotateTask = new RotateTask();
			rotateTask.setOnRotateUpdate(this);

			rotateThread = new Thread(rotateTask);
			rotateThread.start();

			// Simulate a start rotate button click
			combinedView.setOnStartRotateClicked();
		}
	}

	@Override
	public void setOnRotate(int degree) 
	{
		// We need Platform.runLater: setOnRotate is called from a 
		// non-gui thread (RotateTask + Thread) 
		Platform.runLater(new Runnable() 
		{	
			@Override
			public void run() 
			{		
				combinedView.rotate(degree);
			}
		});
	}

	public void stopRotateThread() 
	{
		// Stop if rotateThread is running
		if (rotateThread != null) 
		{
			rotateTask.terminate();
			rotateThread = null;

			// Simulate a stop rotate button click
			combinedView.setOnStopRotateClicked();
		}
	}

	public void closeActiveThreads() 
	{
		stopMusicThread();
		stopDisplayThread();
		stopRotateThread();

		System.out.println("> Active threads closed");
	}
}
