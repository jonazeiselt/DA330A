package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * An extended label with data members to control its design.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class StatusLabel extends Label 
{
	public StatusLabel() 
	{
		this.setMaxWidth(Double.MAX_VALUE);
	}
	
	public void setDefault(String string) 
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #e6e6e6; -fx-border-weight: 1px; -fx-border-color: #cccccc; "
				+ "-fx-text-fill: #999999; -fx-font-weight: bold; -fx-padding: 10;");
	}

	public void setSuccess(String string) 
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #9ae59a; -fx-border-weight: 1px; -fx-border-color: #5cd65c; "
				+ "-fx-text-fill: #2fb62f; -fx-font-weight: bold; -fx-padding: 10;");
	}
	
	public void setError(String string) 
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #ff9999; -fx-border-weight: 1px; -fx-border-color: #ff6666; "
				+ "-fx-text-fill: #ff4d4d; -fx-font-weight: bold; -fx-padding: 10;");
	}

	public void setRunning(String string) 
	{
		this.setText(string);
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #ffd280; -fx-border-weight: 1px; -fx-border-color: #ffaf1a; "
				+ "-fx-text-fill: #ffa500; -fx-font-weight: bold; -fx-padding: 10;");
	}	
}
