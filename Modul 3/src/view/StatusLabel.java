package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * An extended label with data members to control its design.
 * Created by Jonas Eiselt on 2017-11-19.
 */
public class StatusLabel extends Label 
{
	public enum LabelStatus
	{
		DEFAULT, RUNNING, WAITING, COMPLETED, ERROR
	}

	StatusLabel()
	{
		this.setMaxWidth(Double.MAX_VALUE);
	}

	void setStatus(LabelStatus labelStatus, String string)
	{
		switch (labelStatus)
		{
			case DEFAULT:
				setDefault(string + ": sleeping...");
				break;
			case RUNNING:
				setRunning(string + ": running...");
				break;
			case WAITING:
				setWaiting(string + ": waiting...");
				break;
			case COMPLETED:
				setSuccess(string + ": finished operation...");
				break;
			case ERROR:
				setError(string + ": error...");
		}
	}

	void setDefault(String string)
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #e6e6e6; -fx-border-weight: 1px; -fx-border-color: #cccccc; "
				+ "-fx-text-fill: #999999; -fx-font-weight: bold; -fx-padding: 10;");
	}

	private void setRunning(String string)
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #ffd280; -fx-border-weight: 1px; -fx-border-color: #ffaf1a; "
				+ "-fx-text-fill: #ffa500; -fx-font-weight: bold; -fx-padding: 10;");
	}

	private void setWaiting(String string)
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #809fff; -fx-border-weight: 1px; -fx-border-color: #3366ff; "
				+ "-fx-text-fill: #3366ff; -fx-font-weight: bold; -fx-padding: 10;");
	}

	private void setSuccess(String string)
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #9ae59a; -fx-border-weight: 1px; -fx-border-color: #5cd65c; "
				+ "-fx-text-fill: #2fb62f; -fx-font-weight: bold; -fx-padding: 10;");
	}

	private void setError(String string)
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #ff9999; -fx-border-weight: 1px; -fx-border-color: #ff6666; "
				+ "-fx-text-fill: #ff4d4d; -fx-font-weight: bold; -fx-padding: 10;");
	}
}
