package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private final Main game;
    private final Sound torchClick;
    private final Sound honk;
    private final Sound footSteps;
    private final Music music;
    private final Sound noAccess;
    public AudioManager(final Main game){
        this.game = game;

        honk = Gdx.audio.newSound(Gdx.files.internal("soundEffects/honk.mp3"));
        torchClick = Gdx.audio.newSound(Gdx.files.internal("soundEffects/click.mp3"));
        footSteps = Gdx.audio.newSound(Gdx.files.internal("soundEffects/footsteps.mp3"));
        noAccess = Gdx.audio.newSound(Gdx.files.internal("soundEffects/wrong.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("soundEffects/music.mp3"));
        playMusic();
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
    public void loopFootsteps(){
        footSteps.loop(.2f *game.gameVolume);
    }
    public void stopFootsteps(){
        footSteps.stop();
    }
    public void playMusic(){
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
