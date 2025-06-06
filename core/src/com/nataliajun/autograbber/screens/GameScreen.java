package com.nataliajun.autograbber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nataliajun.autograbber.*;
import com.nataliajun.autograbber.components.*;
import com.nataliajun.autograbber.managers.ContactManager;
import com.nataliajun.autograbber.managers.MemoryManager;
import com.nataliajun.autograbber.objects.BonusObject;
import com.nataliajun.autograbber.objects.CarObject;
import com.nataliajun.autograbber.objects.TrashObject;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    GameSession gameSession;
    CarObject carObject;

    ArrayList<TrashObject> trashArray;
    ArrayList<BonusObject> bonusArray;
    ContactManager contactManager;

    // PLAY state UI
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    //TextView liveTextView;
    TextView scoreTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        trashArray = new ArrayList<>();
        bonusArray = new ArrayList<>();

        carObject = new CarObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.CAR_WIDTH, GameSettings.CAR_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(250, 1205);
        //liveTextView = new TextView(myGdxGame.middleYellowFont, 210, 1215);
        scoreTextView = new TextView(myGdxGame.commonBlackFont, 10, 1218);
        pauseButton = new ButtonView(
                605, 1200,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 270, 842, "Pause");
        homeButton = new ButtonView(
                138, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                393, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 150, 842, "Last records");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );

    }

    @Override
    public void show() {
        restartGame();
        myGdxGame.audioManager.updatePlayingFlag(true);
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                TrashObject trashObject = new TrashObject(
                        GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                        GameResources.TRASH_IMG_PATH,
                        myGdxGame.world
                );
                trashArray.add(trashObject);
            }

            if (gameSession.shouldSpawnBonus()) {
                BonusObject bonusObject = new BonusObject(
                        GameSettings.BONUS_WIDTH, GameSettings.BONUS_HEIGHT,
                        GameResources.BONUS_IMG_PATH,
                        myGdxGame.world
                );
                bonusArray.add(bonusObject);
            }

            if (gameSession.shouldDrainHealth()) {
                carObject.drainHealth();
            }

            if (!carObject.isAlive()) {
                gameSession.endGame();
                myGdxGame.audioManager.gameoverSound.play(0.2f);
                myGdxGame.audioManager.setMusicVolume(0f);
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateTrash();
            updateBonus();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            //liveTextView.setText("OIL");
            liveView.setLeftLives(carObject.getLiveLeft());

            myGdxGame.stepWorld();
        }

        draw();
    }
    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                        myGdxGame.audioManager.setMusicVolume(0.05f);
                    }
                    carObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                        myGdxGame.audioManager.setMusicVolume(0.2f);
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.audioManager.setMusicVolume(0.2f);
                        myGdxGame.audioManager.updatePlayingFlag(false);
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

                case ENDED:

                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.audioManager.setMusicVolume(0.2f);
                        myGdxGame.audioManager.updatePlayingFlag(false);
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }

        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        for (BonusObject bonus : bonusArray) bonus.draw(myGdxGame.batch);
        carObject.draw(myGdxGame.batch);
        //for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        //liveTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();

    }

    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++) {

            boolean hasToBeDestroyed = !trashArray.get(i).isAlive() || !trashArray.get(i).isInFrame();

            if (!trashArray.get(i).isAlive() && trashArray.get(i).isRemovedByCar()) {
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.hitSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }
        }
    }

    private void updateBonus() {
        for (int i = 0; i < bonusArray.size(); i++) {

            boolean hasToBeDestroyed = !bonusArray.get(i).isAlive() || !bonusArray.get(i).isInFrame();

            if (!bonusArray.get(i).isAlive()) {
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.grabSound.play(0.1f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(bonusArray.get(i).body);
                bonusArray.remove(i--);
            }
        }
    }

    private void restartGame() {

        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }

        if (carObject != null) {
            myGdxGame.world.destroyBody(carObject.body);
        }

        carObject = new CarObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.CAR_WIDTH, GameSettings.CAR_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        //bulletArray.clear();
        gameSession.startGame();
    }

}
