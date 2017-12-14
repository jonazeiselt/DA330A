package view;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import other.ConcurrentMode;
import other.RunningStatus;
import other.ViewListener;

/**
 * Sets up a user interface which displays a combined view, 
 * containing three different views, with which the user can 
 * interact: a view for inputting a string, a view for displaying 
 * the transmitted string, and a view for displaying the received
 * string.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class CombinedView extends HBox implements ViewListener
{
	private ViewListener viewListener;
	
	private InputView inputView;
	private WriterView writerView;
	private ReaderView readerView;

	public void initializeView() 
	{
		inputView = new InputView();
		inputView.initializeView();
		inputView.setOnViewClicked(this);
		
		writerView = new WriterView();
		writerView.initializeView();
		
		readerView = new ReaderView();
		readerView.initializeView();
		
		this.setPadding(new Insets(20));
		this.setSpacing(10);
		this.getChildren().addAll(inputView, writerView, readerView);
	}

	public void setCombinedViewListener(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}

	@Override
	public void onRunClicked(ConcurrentMode concurrentMode, String stringToTransfer)
	{
		viewListener.onRunClicked(concurrentMode, stringToTransfer);
	}

	@Override
	public void onClearClicked()
	{
		writerView.clear();
		readerView.clear();
		
		inputView.setStatus(RunningStatus.DEFAULT);
	}
	
	public void setRunningStatus(RunningStatus runningStatus) 
	{
		inputView.setStatus(runningStatus);
	}
	
	public void onReadingCompleted(String message) 
	{
		inputView.setDisableButtons(false);
		readerView.onReadingComplete(message);
	}
	
	public void onWritingCompleted(String message) 
	{
		writerView.onWritingComplete(message);
	}
	
	public void onReadingUpdate(String message) 
	{
		readerView.onReadingUpdate(message);
	}
	
	public void onWritingUpdate(String message) 
	{
		writerView.onWritingUpdate(message);
	}

	public void setDisableButtons(boolean disable) 
	{
		inputView.setDisableButtons(disable);
	}
}
