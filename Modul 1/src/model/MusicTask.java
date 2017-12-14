package model;

import javafx.scene.media.*;
import other.MusicListener;

/**
 * Plays a song and notifies a listener when the song has been 
 * finished.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class MusicTask implements Runnable
{
	private String mp3Path;
	private MusicListener musicListener;

	private volatile MediaPlayer mediaPlayer; 
	private volatile Media media;

	private volatile boolean isRunning = true;

	public MusicTask(String mp3Path)
	{
		this.mp3Path = mp3Path;
	}

	/** 
	 * We can now let the Controller know when a song has been
	 * finished. 
	 */
	public void setOnMusicCompleted(MusicListener musicListener) 
	{
		this.musicListener = musicListener;
	}

	@Override
	public void run() 
	{
		if (isRunning) 
		{
			media = new Media(mp3Path);
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();

			MusicEndTask musicEndTask = new MusicEndTask(musicListener);
			mediaPlayer.setOnEndOfMedia(musicEndTask);
		}
	}

	public void terminate() 
	{
		isRunning = false;

		if (mediaPlayer != null)
			mediaPlayer.stop();
	}

	private class MusicEndTask implements Runnable
	{
		private MusicListener musicListener;

		private MusicEndTask(MusicListener taskListener)
		{
			this.musicListener = taskListener;
		}

		@Override
		public void run() 
		{
			// Notify Controller that song is finished
			musicListener.onMusicCompleted();
		}
	}
}
