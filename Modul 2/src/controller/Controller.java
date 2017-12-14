package controller;

import javafx.application.Platform;
import model.CharacterBuffer;
import model.Reader;
import model.Writer;
import other.ConcurrentMode;
import other.IOListener;
import other.RunningStatus;
import other.ViewListener;
import view.CombinedView;

/**
 * Handles the user interaction with the three different views (see
 * java files in package view). Keeps track of the different threads,
 * including closing them when requested.
 * 
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class Controller implements ViewListener, IOListener
{
	private CombinedView combinedView;
	private String transmittedString;

	private Thread writerThread;
	private Thread readerThread;
	private Writer writer;
	private Reader reader;

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

		combinedView.setCombinedViewListener(this);
	}

	@Override
	public void onRunClicked(ConcurrentMode concurrentMode, String stringToTransfer) 
	{
		if (writerThread != null || readerThread != null) 
		{
			System.err.println("\nThreads already running:\nwriter=" 
					+ writerThread + ", reader=" + readerThread + "\n");
		}
		else 
		{
			if (!stringToTransfer.equals("")) 
			{
				System.out.println("> Sending \"" + stringToTransfer + "\" " + concurrentMode + "ly");
				CharacterBuffer buffer = new CharacterBuffer();

				writer = new Writer(buffer, stringToTransfer);
				writer.setConcurrentMode(concurrentMode);
				writer.setOnWritingCompleted(this);

				writerThread = new Thread(writer);
				writerThread.start();

				reader = new Reader(buffer);
				reader.setConcurrentMode(concurrentMode);
				reader.setOnReadingCompleted(this);

				readerThread = new Thread(reader);
				readerThread.start();

				combinedView.setDisableButtons(true);
				combinedView.setRunningStatus(RunningStatus.RUNNING);
			}
		}
	}

	@Override
	public void onClearClicked() 
	{
		combinedView.onClearClicked();
	}

	@Override
	public void onReadingCompleted(String message) 
	{
		System.out.println("\ninfo:\nsent: " + transmittedString);
		System.out.println("received: " + message + "\n");

		RunningStatus runningStatus;
		if (transmittedString.equals(message))
			runningStatus = RunningStatus.MATCH;
		else 
			runningStatus = RunningStatus.NOMATCH;

		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				combinedView.onReadingCompleted(message);
				combinedView.setRunningStatus(runningStatus);
			}
		});

		closeReaderThread();
		closeWriterThread();
	}

	private void closeReaderThread() 
	{
		if (readerThread != null) 
		{
			reader.terminate();
			readerThread = null;
		}
	}

	@Override
	public void onWritingCompleted(String message) 
	{
		transmittedString = message;
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				combinedView.onWritingCompleted(message);
			}
		});
	}

	private void closeWriterThread() 
	{
		if (writerThread != null) 
		{
			writer.terminate(); 
			writerThread = null;
		}
	}

	@Override
	public void onReadingUpdate(String message) 
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				combinedView.onReadingUpdate(message);
			}
		});
	}

	@Override
	public void onWritingUpdate(String message) 
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				combinedView.onWritingUpdate(message);
			}
		});
	}

	public void closeActiveThreads() 
	{
		System.out.println("> Closing active threads");

		closeWriterThread();
		closeReaderThread();
	}
}
