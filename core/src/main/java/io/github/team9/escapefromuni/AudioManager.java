package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Control and play game sounds
 */
public class AudioManager {

    private final Main game;
    private final Sound torchClick;
    private final Sound honk;
    private final Sound footSteps;
    private final Music music;
    private final Sound noAccess;
    private final Sound collect;
    public static Sound coin;
    public static Sound planet;
    public static Sound essay;
    public static Sound dean;

    /**
     * Initialised audio manager
     * @param game current instance of Main
     */
    public AudioManager(final Main game){
        this.game = game;

        honk = createSound("soundEffects/honk.mp3");
        torchClick = createSound("soundEffects/click.mp3");
        footSteps = createSound("soundEffects/footsteps.mp3");
        noAccess = createSound("soundEffects/wrong.mp3");
        collect = createSound("soundEffects/tap.mp3");

        coin = createSound("soundEffects/coin-drop-422703.mp3");
        planet = createSound("soundEffects/laser-90052.mp3");
        essay = createSound("soundEffects/paper-rip-sfx-432494.mp3");
        dean = createSound("soundEffects/happy-message-ping-351298.mp3");

        music = createMusic("soundEffects/music.mp3");
        playMusic();
    }

    public Music createMusic(String filePath) {
        return Gdx.audio.newMusic(Gdx.files.internal(filePath));
    }

    public Sound createSound(String filePath) {
        return Gdx.audio.newSound(Gdx.files.internal(filePath));
    }

    public void playHonk(){
        honk.play(game.gameVolume);
    }
    public void playTorch(){
        torchClick.play(game.gameVolume);
    }
    public void playNoAccess(){
        noAccess.play(game.gameVolume);
    }
    public void playCollect(){collect.play(game.gameVolume);}

    public static void playCoinSound(){coin.play();}
    public static void playPlanetSound(){planet.play();}
    public static void playEssaySound(){essay.play();}
    public static void playDeanSound(){dean.play();}

    public void loopFootsteps(){
        footSteps.loop(.2f *game.gameVolume);
    }
    public void stopFootsteps(){
        footSteps.stop();
    }
    public void playMusic(){
        setMusicVolume();
        music.play();
        music.setLooping(true);
    }
    public void setMusicVolume(){
        music.setVolume(0.01f * game.musicVolume);
    }
    public void stopMusic(){
        music.stop();
    }
    public void pauseMusic(){
        music.pause();
    }



    public void dispose(){
        if (torchClick != null) {
            torchClick.dispose();
        }
        if (honk != null) {
            honk.dispose();
        }
    }
}
