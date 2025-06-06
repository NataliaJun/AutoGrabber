package com.nataliajun.autograbber.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nataliajun.autograbber.GameResources;

public class LiveView extends View {

    private final static int scale = 2;
    private final static int livePadding = 2;
    private final static int topMargin = 5;
    private final static int leftMargin = 6;

    private Texture texture;
    private Texture panelTexture;

    private int leftLives;

    public LiveView(float x, float y) {
        super(x, y);
        texture = new Texture(GameResources.LIVE_IMG_PATH);
        panelTexture = new Texture(GameResources.LIVE_PANEL_IMG_PATH);
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        leftLives = 0;
    }

    public void setLeftLives(int leftLives) {
        this.leftLives = leftLives;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(panelTexture, x, y, panelTexture.getWidth() * scale, panelTexture.getHeight() * scale);

        for(int i = 0; i < leftLives; i++){
            batch.draw(texture, x + (leftMargin * scale) + (i * (width + livePadding) * scale),
                    y + (topMargin * scale), width * scale, height * scale);
        }

        //if (leftLives > 0) batch.draw(texture, x + (width + livePadding), y, width, height);
        //if (leftLives > 1) batch.draw(texture, x, y, width, height);
        //if (leftLives > 2) batch.draw(texture, x + 2 * (width + livePadding), y, width, height);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}