# music-player
This is a music player application which will allow the user the ability to listen to audio files stored on their computer, As well as let the user create playlists and edit track metadata. The user's collection of songs/albums will be displayed in a customizable array. Standard functions will be included, such as the ability to pause, restart, repeat, skip to a point in a track, entirely skip a track, change the output volume, change mixer settings. As well, there may be some more complicated features included in later versions of the software, such as an audio visualizer, or Last.FM integration.

# instructions

1. Using the command prompt, navigate to the main project folder.

2. In order to make the 'jar' command functional, set the system enviroment variable of 'path' to your Java directory, which should be similar to 'C:\Program Files\Java\jdk-15.0.1\bin'. This can be done with the command "path C:\Program Files\Java\jdk-15.0.1\bin; %path%".

3. Compile the class in the 'src' folder. Since this has JavaFX dependencies, it is necessary to compile with those libraries, which are included in the 'lib' folder. This can be done with the command "javac -cp lib\lib\javafx.base.jar;lib\lib\javafx.controls.jar;lib\lib\javafx.fxml.jar;lib\lib\javafx.graphics.jar;lib\lib\javafx.media.jar;lib\lib\javafx.swing.jar;lib\lib\javafx.web.jar;lib\lib\javafx-swt.jar -d src src\Main.java".

4. Create the jar file using the manifest file and the compiled class file. This is done with the command "jar cvfm Main.jar manifest.txt src\*.class".

5. The newly created jar file should now be accessible. Test it with the command "java -jar Main.jar".
