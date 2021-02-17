import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class PlayerApplication extends Application
{
	@Override
	public void start(Stage mainStage)
	{
		
		Text testText = new Text(40, 40, "Program loaded successfully.");
		
		Button testButton = new Button("Click me!");
		testButton.setTranslateX(160);
		testButton.setTranslateY(300);
		testButton.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent arg0)
					{
						testButton.setText("Button clicked!");
					}
			
				}
		
				
				);
		
		Group buttons = new Group(testButton);
		
		BorderPane pane = new BorderPane(testText);
		
		pane.getChildren().add(buttons);
		
		Scene scene = new Scene(pane, 400, 400);
		
		
		mainStage.setScene(scene);
		mainStage.setTitle("Music Player - ECE 5010");
		mainStage.show();
	}
	
	public static void main(String[] args)
	{	
		launch(args);
	}
	
}
