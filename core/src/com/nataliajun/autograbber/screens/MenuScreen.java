package com.nataliajun.autograbber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nataliajun.autograbber.GameResources;
import com.nataliajun.autograbber.MyGdxGame;
import com.nataliajun.autograbber.components.ButtonView;
import com.nataliajun.autograbber.components.MovingBackgroundView;
import com.nataliajun.autograbber.components.TextView;

public class MenuScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;

    MovingBackgroundView backgroundView;
    TextView titleView;
    ButtonView startButtonView;
    ButtonView settingsButtonView;
    ButtonView exitButtonView;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new MovingBackgroundView(GameResources.MENU_BACKGROUND_STATIC_IMG_PATH);
        titleView = new TextView(myGdxGame.titleFont, 115, 860, "AUTOGRABBER");
        startButtonView = new ButtonView(140, 646, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "start");
        settingsButtonView = new ButtonView(140, 551, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "settings");
        exitButtonView = new ButtonView(140, 456, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "exit");
    }

    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleView.draw(myGdxGame.batch);
        exitButtonView.draw(myGdxGame.batch);
        settingsButtonView.draw(myGdxGame.batch);
        startButtonView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (exitButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.settingsScreen);
            }
        }
    }
}
