package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.CombinedView;

/**
 * Handles three different threads in a gui-application: one which 
 * plays music, one which displays text at different positions, and 
 * another one which rotates a triangle shape.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class MultiThreadApp extends Application 
{
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception 
	{
		CombinedView combinedView = new CombinedView(stage);
		
		Controller controller = new Controller(combinedView);
		controller.initializeView();
		
		stage.setTitle("MultiThread App");
		stage.setScene(new Scene(combinedView,600,640));
		
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
