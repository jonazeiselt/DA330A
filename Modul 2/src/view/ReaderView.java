package view;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * Sets up a view for displaying a text area containing live 
 * events from Controller and a text field containing the result
 * of those live events (i.e. the received string).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class ReaderView extends TitledPane
{
	private TextArea readerOutputField;
	private TextField receivedStringField;

	public void initializeView() 
	{
		this.setText("Reader Thread Logger");
		this.setCollapsible(false);
		
		VBox root = getRootView();
		this.setContent(root);
		this.setMaxHeight(Double.MAX_VALUE);
	}

	private VBox getRootView() 
	{
		readerOutputField = new TextArea();
		readerOutputField.setEditable(false);
		readerOutputField.setWrapText(true);
		readerOutputField.setMinHeight(387);
		
		TitledPane receivedStringView = getReceivedStringView(); 
		
		VBox root = new VBox(5);
		root.getChildren().addAll(readerOutputField, receivedStringView);
		
		return root;
	}

	private TitledPane getReceivedStringView() 
	{
		TitledPane titledPane = new TitledPane();
		titledPane.setText("Received");
		titledPane.setCollapsible(false);
		
		receivedStringField = new TextField();
		receivedStringField.setEditable(false);
		
		titledPane.setContent(receivedStringField);
		return titledPane;
	}

	public void onReadingUpdate(String message) 
	{
		readerOutputField.appendText(message + "\n");
	}
	
	public void onReadingComplete(String message) 
	{
		receivedStringField.setText(message);
	}

	public void clear() 
	{
		readerOutputField.clear();
		receivedStringField.clear();
	}
}
