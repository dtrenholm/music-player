import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.sound.sampled.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class Main extends Application
{
	boolean audioPlaying = false;
	boolean audioPaused = false;

	public static void main(String[] args)
	{	
		launch(args);
	}

	public void playback (String audioFilePath, Button pauseButton) 
	{

		File audioFile = new File(audioFilePath);
		
		try 
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			AudioFormat format = audioStream.getFormat();
			
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			
			//Ensures that resources are properly freed after audio has finished playing.
			audioClip.addLineListener(e -> 
				{
					if (e.getType() == LineEvent.Type.STOP && audioPlaying == false)
							{
								audioClip.close();
								//System.out.println("Song finished.");
								audioPlaying = false;
							}
				});
			
			audioClip.open(audioStream);
			
			audioClip.start();
			
			audioPlaying = true;
			
			pauseButton.setOnAction(new EventHandler<ActionEvent>()
			{

				@Override
				public void handle(ActionEvent arg0)
				{
					if (audioPaused == false)
					{
						audioClip.stop();
						audioPaused = true;
					}
					
					else
					{
						audioClip.start();
						audioPaused = false;
					}
				}
				
			});
			
			
	
		}
		
		catch (UnsupportedAudioFileException e1) 
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		} 
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}
		
		
		
	

	}
	
	@Override
	public void start(Stage mainStage)
	{
		TextField dirInput = new TextField();
		
		ListView<String> dirList = new ListView<String>();
		dirList.setPrefHeight(200);
		
		Button dirButton = new Button("Click to add audio file directory.");
		
		Button pauseButton = new Button("Pause");
		
		BorderPane.setAlignment(dirButton, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(pauseButton, Pos.BOTTOM_LEFT);
		
		
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
				});
		
		
		
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
						playback(dirInput.getText() + dirList.getSelectionModel().selectedItemProperty().getValue().toString(), pauseButton);
					}
			});
		
		
		BorderPane pane = new BorderPane();
	
		BorderPane.setAlignment(dirButton, Pos.TOP_RIGHT);
		BorderPane.setAlignment(dirInput, Pos.TOP_LEFT);
		BorderPane.setAlignment(pauseButton, Pos.TOP_LEFT);
		
		pane.setTop(dirInput);
		pane.setRight(dirButton);
		pane.setBottom(dirList);
		pane.setLeft(pauseButton);
		
		pane.setStyle("-fx-border-width: 1;");
		pane.setStyle("-fx-padding: 15;");
		
		//Final step in GUI Creation.
		
		Scene scene = new Scene(pane, 400, 400);

		mainStage.setScene(scene);
		mainStage.setTitle("Music Player - ECE 5010");
		mainStage.show();
	}
	
} 