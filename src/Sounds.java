package mooninvaders;

import sun.audio.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class Sounds {
    
    private static InputStream inputStream;
    private static AudioStream audioStream;
    private static AudioData data;
    private static ContinuousAudioDataStream cas;
    private Clip clip;
    
    public Sounds( String filename ) {
        try {
            clip = AudioSystem.getClip();
            clip.open( AudioSystem.getAudioInputStream( getClass().getResource( filename ) ) );
        } catch (UnsupportedAudioFileException e) {
        } catch (IOException e) {
        } catch (LineUnavailableException e) {}
    }
    
    public void stop() {
        clip.stop();
    }
    
    public void rewind() {
        clip.setFramePosition(0);
    }
    
    public void loop() {
        clip.loop( 0 );
    }
    
    public void start() {
        clip.start();
    }
    
    public static void continuousAudio( String filename, int board ) {
         try {
            if ( board == 0 ) {
                inputStream = ClassicBoard.class.getResourceAsStream( filename );
            } else if ( board == 1 ) {
                inputStream = SurvivalBoard.class.getResourceAsStream( filename );
            } else if ( board == 2 ) {
                inputStream = TargetBoard.class.getResourceAsStream( filename );
            } else if ( board == 3 ) {
                inputStream = ModesPanel.class.getResourceAsStream( filename );
            } else if ( board == 4 ) {
                inputStream = HowToPlay.class.getResourceAsStream( filename );
            } else {
                inputStream = Startup.class.getResourceAsStream( filename );
            }
            audioStream = new AudioStream( inputStream );
            data = audioStream.getData();
            cas = new ContinuousAudioDataStream( data );
        } catch (FileNotFoundException f) {
        } catch (IOException i) {}
        AudioPlayer.player.start( cas );
    }
    
    public static void stopContinuousAudio() {
        AudioPlayer.player.stop( cas );
    }
    
    public static void loadAndPlay( String filename, int board ) {
        try {
            if ( board == 0 ) {
                inputStream = ClassicBoard.class.getResourceAsStream( filename );
            } else if ( board == 1 ) {
                inputStream = SurvivalBoard.class.getResourceAsStream( filename );
            } else if ( board == 2 ) {
                inputStream = TargetBoard.class.getResourceAsStream( filename );
            } else if ( board == 3 ) {
                inputStream = ModesPanel.class.getResourceAsStream( filename );
            } else if ( board == 4 ) {
                inputStream = HowToPlay.class.getResourceAsStream( filename );
            } else {
                inputStream = Startup.class.getResourceAsStream( filename );
            }
            audioStream = new AudioStream( inputStream );
        } catch (FileNotFoundException f) {
        } catch (IOException i) {}
        AudioPlayer.player.start( audioStream );
    }
}