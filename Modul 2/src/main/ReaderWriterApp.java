package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.CombinedView;

/**
 * Handles two different threads in a gui-application: one which 
 * writes a character to a buffer, and one which reads from the same
 * buffer.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class ReaderWriterApp extends Application 
{
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception 
	{
		System.out.println("The quick brown fox jumped over the lazy dog.".length());
		System.out.println("TThhequick bbownfjpedvertlayy doog".length());
		
		CombinedView combinedView = new CombinedView();
		
		Controller controller = new Controller(combinedView);
		controller.initializeView();
		
		stage.setTitle("ReaderWriter App");
		stage.setScene(new Scene(combinedView,1000,550));
		
		try {
			stage.getIcons().add(new Image("/files/threadicon.png"));
		} 
		catch (IllegalArgumentException e) {
			// Ignore if file path is invalid  	
		}
		
		stage.setResizable(false);
		stage.show();
		
		stage.setOnCloseRequest(e -> 
		{
			controller.closeActiveThreads();			
			System.exit(0);
		});
	}
}
