package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import other.ConcurrentMode;
import other.RunningStatus;
import other.ViewListener;

/**
 * Sets up a view which controls the two threads for reading 
 * and writing to a buffer.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class InputView extends TitledPane
{
	private ViewListener viewListener;
	
	private RadioButton synchronousModeSelector;
	private RadioButton asynchronousModeSelector;
	
	private StatusLabel statusLabel;
	
	private Button runButton;
	private Button clearButton;
	
	public void initializeView() 
	{
		this.setText("Concurrent Tester");
		this.setCollapsible(false);

		VBox root = getRootView();
		this.setContent(root);
		this.setMaxHeight(Double.MAX_VALUE);
	}

	private VBox getRootView() 
	{
		VBox modeSelectorView = getModeSelectorView();
		VBox stringTransferView = getStringTransferView();
		VBox runningStatusView = getRunningStatusView();

		VBox root = new VBox(20);
		root.getChildren().addAll(modeSelectorView, stringTransferView, runningStatusView);

		return root;
	}

	private VBox getModeSelectorView() 
	{
		synchronousModeSelector = new RadioButton("Synchronous mode");
		asynchronousModeSelector = new RadioButton("Asynchronous mode");

		ToggleGroup toggleGroup = new ToggleGroup();
		synchronousModeSelector.setSelected(true);
		synchronousModeSelector.setToggleGroup(toggleGroup);
		asynchronousModeSelector.setToggleGroup(toggleGroup);

		VBox root = new VBox(5);
		root.getChildren().addAll(synchronousModeSelector, asynchronousModeSelector);

		return root;
	}

	private VBox getStringTransferView() 
	{
		Label label = new Label("String to transfer:");

		String defaultString = "The quick brown fox jumped over the lazy dog.";
		TextArea stringInputField = new TextArea(defaultString);
		stringInputField.setWrapText(true);

		runButton = new Button("Run");
		runButton.setMaxWidth(Double.MAX_VALUE);
		runButton.setPrefHeight(35);
		
		runButton.setOnMouseClicked(e -> 
		{	
			ConcurrentMode concurrentMode;
			if (synchronousModeSelector.isSelected())
				concurrentMode = ConcurrentMode.SYNCHRONOUS;
			else if (asynchronousModeSelector.isSelected())
				concurrentMode = ConcurrentMode.ASYNCHRONOUS;
			else
				concurrentMode = ConcurrentMode.SYNCHRONOUS;

			viewListener.onRunClicked(concurrentMode, stringInputField.getText().trim());
		});

		VBox root = new VBox(5);
		root.getChildren().addAll(label, stringInputField, runButton);

		return root;
	}

	private VBox getRunningStatusView() 
	{
		Label headerLabel = new Label("Running status:");

		statusLabel = new StatusLabel();
		statusLabel.setDefault("Sleeping");

		clearButton = new Button("Clear");
		clearButton.setMaxWidth(Double.MAX_VALUE);
		clearButton.setPrefHeight(35);

		clearButton.setOnMouseClicked(e -> {
			viewListener.onClearClicked();
		});

		VBox root = new VBox(5);
		root.getChildren().addAll(headerLabel, statusLabel, clearButton);

		return root;
	}

	public void setOnViewClicked(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}

	public void setStatus(RunningStatus runningStatus) 
	{
		if (runningStatus == RunningStatus.MATCH)
			statusLabel.setSuccess("Match");
		else if (runningStatus == RunningStatus.NOMATCH)
			statusLabel.setError("No match");
		else if (runningStatus == RunningStatus.RUNNING)
			statusLabel.setRunning("Running");
		else
			statusLabel.setDefault("Sleeping");
	}

	public void setDisableButtons(boolean disable) 
	{	
		runButton.setDisable(disable);
		clearButton.setDisable(disable);
	}
}
