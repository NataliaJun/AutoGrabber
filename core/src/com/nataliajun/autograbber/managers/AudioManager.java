package com.nataliajun.autograbber.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.nataliajun.autograbber.GameResources;

public class AudioManager {

    public boolean isSoundOn;
    public boolean isMusicOn;

    public boolean isInGame;

    public Music menuBackgroundMusic;
    public Music gameBackgroundMusic;

    public Music currentBackgroundMusic;
    public Sound grabSound;
    public Sound hitSound;
    public Sound gameoverSound;

    public AudioManager() {
        isInGame = false;

        menuBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.MENU_BACKGROUND_MUSIC_PATH));
        gameBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.GAME_BACKGROUND_MUSIC_PATH));

        grabSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.GRAB_SOUND_PATH));
        hitSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.DESTROY_SOUND_PATH));
        gameoverSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.FAIL_SOUND_PATH));

        menuBackgroundMusic.setVolume(0.2f);
        menuBackgroundMusic.setLooping(true);

        gameBackgroundMusic.setVolume(0.2f);
        gameBackgroundMusic.setLooping(true);

        currentBackgroundMusic = menuBackgroundMusic;

        updateSoundFlag();
        updateMusicFlag();
    }

    public void updateSoundFlag() {
        isSoundOn = MemoryManager.loadIsSoundOn();
    }

    public void updateMusicFlag() {
        isMusicOn = MemoryManager.loadIsMusicOn();

        if (isMusicOn) currentBackgroundMusic.play();
        else currentBackgroundMusic.stop();
    }

    public void updatePlayingFlag(boolean inGame) {
        if(isInGame != inGame){
            isInGame = inGame;

            currentBackgroundMusic.stop();
            currentBackgroundMusic = inGame ? gameBackgroundMusic : menuBackgroundMusic;
            updateMusicFlag();
        }
    }

    public void setMusicVolume(float volume){
        currentBackgroundMusic.setVolume(volume);
    }
}
