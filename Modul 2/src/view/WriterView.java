package view;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * Sets up a view for displaying a text area containing live 
 * events from Controller and a text field containing the result
 * of those live events (i.e. the transmitted string).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class WriterView extends TitledPane
{
	private TextArea writerOutputField;
	private TextField receivedStringField;

	public void initializeView() 
	{
		this.setText("Writer Thread Logger");
		this.setCollapsible(false);
		
		VBox root = getRootView();
		
		this.setContent(root);
		this.setMaxHeight(Double.MAX_VALUE);
	}

	private VBox getRootView() 
	{
		writerOutputField = new TextArea();
		writerOutputField.setEditable(false);
		writerOutputField.setWrapText(true);
		writerOutputField.setMinHeight(387);
		
		TitledPane transmittedStringView = getTransmittedStringView(); 
		
		VBox root = new VBox(5);
		root.getChildren().addAll(writerOutputField, transmittedStringView);
		
		return root;
	}

	private TitledPane getTransmittedStringView() 
	{
		TitledPane titledPane = new TitledPane();
		titledPane.setText("Transmitted");
		titledPane.setCollapsible(false);
		
		receivedStringField = new TextField();
		receivedStringField.setEditable(false);
		
		titledPane.setContent(receivedStringField);
		return titledPane;
	}

	public void onWritingUpdate(String message)
	{
		writerOutputField.appendText(message + "\n");
	}
	
	public void onWritingComplete(String message) 
	{
		receivedStringField.setText(message);
	}

	public void clear()
	{
		writerOutputField.clear();
		receivedStringField.clear();
	}
}
