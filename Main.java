import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;


public class Main extends Application
{

	public static void main(String[] args)
	{	
		launch(args);
	}

	@Override
	public void start(Stage mainStage)
	{
		
		TextField dirInput = new TextField();
		
		ListView<String> dirList = new ListView<String>();
		dirList.setPrefHeight(200);
		
		Button dirButton = new Button("Click to add audio file directory.");
		
		BorderPane.setAlignment(dirButton, Pos.BOTTOM_CENTER);
		
		dirButton.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent arg0)
					{
						Path p1 = Paths.get(dirInput.getText());
						
						dirList.getItems().clear();
						
						try {
							DirectoryStream<Path> stream = Files.newDirectoryStream(p1);
							
							for (Path file: stream)
								{
									System.out.println(file.getFileName());
									dirList.getItems().add(file.getFileName().toString());
								}
							
							} 
						catch (IOException e) {
							// e.printStackTrace();
							System.out.println("Error occurred, directory may not exist. Please try again.");
						}
					}
			
				}

				);
		
		
		
		//Used to obtain file name from list of directory items.
		class listHandler implements EventHandler<MouseEvent> 
			{
				@Override
				public void handle (MouseEvent c)
				{
					
				}
			}
		
		dirList.setOnMouseClicked(new listHandler()
			{
				@Override
				public void handle(javafx.scene.input.MouseEvent event)
					{
						System.out.println(dirInput.getText() + dirList.getSelectionModel().selectedItemProperty().getValue());
					}
			});
		
		
		BorderPane pane = new BorderPane();
	
		BorderPane.setAlignment(dirButton, Pos.TOP_RIGHT);
		BorderPane.setAlignment(dirInput, Pos.TOP_LEFT);
		
		pane.setTop(dirInput);
		pane.setRight(dirButton);
		pane.setBottom(dirList);
		
		pane.setStyle("-fx-border-width: 1;");
		pane.setStyle("-fx-padding: 15;");
		
		//Final step in GUI Creation.
		
		Scene scene = new Scene(pane, 400, 400);

		mainStage.setScene(scene);
		mainStage.setTitle("Music Player - ECE 5010");
		mainStage.show();
	}
	
} 