import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import io.github.team9.escapefromuni.AudioManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
import com.badlogic.gdx.Gdx;
import static org.mockito.Mockito.mock;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import io.github.team9.escapefromuni.Main;


public class AudioManagerTest extends BaseTest { //needed as a base to create other tests
    private static Main testMain;
    private static Music testMusic;
    private static Sound testSound;
    private static AudioManager testAudioManager;

    @BeforeAll
    public static void testSetup() {
        testMusic = mock(Music.class);
        testMain = mock(Main.class);
        testSound = mock(Sound.class);
        testAudioManager = new AudioManager(testMain) {
            @Override
            public Music createMusic(String filePath) {
                return testMusic;
            }
            @Override
            public Sound createSound(String filePath) {
                return testSound;
            }
        };
    }

    @Test //explanation here
    public void testPlayMusic() { //music plays automatically in the constructor
        verify(testMusic, times(1)).play();
        verify(testMusic, times(1)).setLooping(true);
    }

//    @Test //explanation here
//    public void testPlaySound() {
//        testAudioManager.playSound();
//        verify(testSound, times(1)).play();
//    }

    @Test //explanation here
    public void testSetMusicVolume() {
        testMain.musicVolume = 10f;
        testAudioManager.setMusicVolume();
        verify(testMusic, times(1)).setVolume(0.01f * 10f);
    }

    @Test //explanation here
    public void testStopMusic() {
        testAudioManager.stopMusic();
        verify(testMusic, times(1)).stop();
    }

    @Test //explanation here
    public void testPauseMusic() {
        testAudioManager.pauseMusic();
        verify(testMusic, times(1)).pause();
    }

}

//public void playMusic(){
//    setMusicVolume();
//    music.play();
//    music.setLooping(true);
//}
//public void setMusicVolume(){
//    music.setVolume(0.01f * game.musicVolume);
//}
//public void stopMusic(){
//    music.stop();
//}
//public void pauseMusic(){
//    music.pause();
//}
//
//
//
//public void dispose(){};
