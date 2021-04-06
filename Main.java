
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.sound.sampled.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

public class Main extends Application
{
	boolean audioPlaying = false;
	boolean audioPaused = false;
	long timeHolder = System.currentTimeMillis();

	public static void main(String[] args)
	{	
		launch(args);
	}

	public void playback (String audioFilePath, Button pauseButton, Button stopButton, Slider volumeSlider, Slider eqSlider1, ListView<String> queueList) 
	{

		File audioFile = new File(audioFilePath);
		
		try 
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			AudioFormat format = audioStream.getFormat();
			
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			Clip audioClip = (Clip) AudioSystem.getLine(info);
			
			
			audioClip.open(audioStream);
			
			audioClip.start();
			
			audioPaused = false;
			
			audioPlaying = true;
			
			audioClip.addLineListener(e -> 
			{
				if (e.getType() == LineEvent.Type.STOP && audioPaused == false)
						{
							audioClip.close();
							System.out.println("Song finished.");	
							
							if (queueList.getItems().isEmpty() != true && audioPlaying == true)
							{
								playback(queueList.getItems().get(0),
										pauseButton, stopButton, volumeSlider, eqSlider1, queueList);
								
								queueList.getItems().remove(queueList.getItems().get(0));
							}
							
							audioPlaying = false;
						}
			});
			
			
			
			
			//Pause button needs to be clicked twice if previous clip was stopped while paused, fix possible?
			pauseButton.setOnAction(new EventHandler<ActionEvent>()
			{

				@Override
				public void handle(ActionEvent arg0)
				{
					if (audioPaused == false)
					{
						audioClip.stop();
						audioPaused = true;
						if (audioPlaying == true) {
							pauseButton.setText("Play");
						}
					}
					
					else
					{
						audioClip.start();
						audioPaused = false;
						pauseButton.setText("Pause");
					}
				}
				
			});
			
			stopButton.setOnAction(new EventHandler<ActionEvent>()
			{

				@Override
				public void handle(ActionEvent arg0)
				{
					if (audioPlaying == true)
					{
						audioClip.stop();
						audioClip.close();
					}
					
					audioPlaying = false;
					audioPaused = false;
					pauseButton.setText("Pause");
					return;
						
					
					
				}	
			});
			
			

			volumeSlider.valueProperty().addListener(e -> 
					{
						setVolume(audioClip, volumeSlider.valueProperty().doubleValue());
					});
			
			eqSlider1.valueProperty().addListener(e -> 
					{
						setPan(audioClip, eqSlider1.valueProperty().doubleValue());
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
	
	
	//Method has slight delay in actually adjusting the volume, unsure if this can be avoided
	public void setVolume(Clip clip, double vol)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		
		//Equation required to convert slider values to audible changes in volume
		volume.setValue((float)((vol+1)*86.0206/2)-80);
		//Old Equation:
		//volume.setValue(20f * (float) Math.log10(vol));
	}
	
	public void setPan(Clip clip, double pan)
	{
		FloatControl panAmount = (FloatControl) clip.getControl(FloatControl.Type.PAN);
		
		panAmount.setValue((float) pan);
	}
	
	
	@Override
	public void start(Stage mainStage)
	{
		TextField dirInput = new TextField();
		
		ListView<String> dirList = new ListView<String>();
		
		ListView<String> queueList = new ListView<String>();
		
		dirList.setPrefHeight(200);
		queueList.setPrefHeight(200);
		
		Button dirButton = new Button("Click to add audio file directory.");
		Button pauseButton = new Button("Pause");
		Button stopButton = new Button("Stop");
		
		
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

		HBox controls = new HBox();
		HBox equalizer = new HBox();
		HBox windows = new HBox();
		
		Slider volSlider = new Slider(-1, 1, 0);
		volSlider.setShowTickMarks(true);
		
		Slider eqSlider1 = new Slider(-1, 1, 0);
		eqSlider1.setOrientation(Orientation.VERTICAL);
		
		Slider eqSlider2 = new Slider(-1, 1, 0);
		eqSlider2.setOrientation(Orientation.VERTICAL);
		
		Slider eqSlider3 = new Slider(-1, 1, 0);
		eqSlider3.setOrientation(Orientation.VERTICAL);
		
		Slider eqSlider4 = new Slider(-1, 1, 0);
		eqSlider4.setOrientation(Orientation.VERTICAL);
		

		controls.getChildren().addAll(dirInput, volSlider, pauseButton, stopButton);
		equalizer.getChildren().addAll(eqSlider1, eqSlider2, eqSlider3, eqSlider4);
		windows.getChildren().addAll(dirList, queueList);
		
		
		BorderPane pane = new BorderPane();
	
		
		BorderPane.setAlignment(dirInput, Pos.TOP_CENTER);
		BorderPane.setAlignment(dirButton, Pos.TOP_RIGHT);
		BorderPane.setAlignment(controls, Pos.BOTTOM_RIGHT);
		BorderPane.setAlignment(equalizer, Pos.CENTER);
		
		
		pane.setTop(controls);
		pane.setRight(dirButton);
		pane.setBottom(windows);
		pane.setCenter(equalizer);
		

		
		pane.setStyle("-fx-border-width: 1;");
		pane.setStyle("-fx-padding: 15;");
		
		//Final step in GUI Creation.
		
		Scene scene = new Scene(pane, 500, 500);

		mainStage.setScene(scene);
		mainStage.setTitle("Music Player - ECE 5010");
		mainStage.show();
		
		
		
		
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
						long mouseCheck = 0;
						
						long currentTime = System.currentTimeMillis();
						
						mouseCheck = currentTime - timeHolder;
						
						if (mouseCheck <= 200 && mouseCheck > 0)
						{				
							//If-statement is used to prevent multiple clips from being played simultaneously.
							if (audioPlaying == false)
							{
								System.out.println(dirInput.getText() + dirList.getSelectionModel().selectedItemProperty().getValue());
								playback(dirInput.getText() 
									+ dirList.getSelectionModel().selectedItemProperty().getValue().toString(), 
										pauseButton, stopButton, volSlider,eqSlider1, queueList);
							}
							else
							{
								queueList.getItems().add(dirInput.getText() + dirList.getSelectionModel().selectedItemProperty().getValue());
							}
						}
						timeHolder = currentTime;
					}
			});
		
		queueList.setOnMouseClicked(new listHandler()
				{
					@Override
					public void handle(javafx.scene.input.MouseEvent event)
					{
						long mouseCheck = 0;
						
						long currentTime = System.currentTimeMillis();
						
						mouseCheck = currentTime - timeHolder;
						
						if (mouseCheck <= 200 && mouseCheck > 0)
						
						if (audioPlaying == false)
						{
							playback(queueList.getSelectionModel().selectedItemProperty().getValue().toString(), 
										pauseButton, stopButton, volSlider, eqSlider1, queueList);
							
							
							
							queueList.getItems().remove(queueList.getSelectionModel().getSelectedIndex());
						}
						timeHolder = currentTime;
					}
					
				}
				
				
				
			);
	
	}
} 
