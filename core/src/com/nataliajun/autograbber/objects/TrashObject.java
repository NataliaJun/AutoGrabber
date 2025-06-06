package com.nataliajun.autograbber.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nataliajun.autograbber.GameSettings;

import java.util.Random;

public class TrashObject extends GameObject {

    private static final int paddingHorizontal = 30;

    private int livesLeft;
    private boolean destroyedByCar;

    public TrashObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                (int)borderSpawn(width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width))),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.TRASH_BIT,
                world
        );

        destroyedByCar = false;

        body.setLinearVelocity(new Vector2(0, -GameSettings.TRASH_VELOCITY));
        livesLeft = 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean isRemovedByCar() {
        return destroyedByCar;
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

    @Override
    public void hit(short BIT) {
        livesLeft -= 1;
        if(BIT == GameSettings.CAR_BIT) destroyedByCar = true;
    }

    private static float borderSpawn(float x){
        float r = x;

        int leftBorder = 182;
        int rightBorder = 528;

        if(r < leftBorder) r = leftBorder;
        if(r > rightBorder) r = rightBorder;

        return r;
    }
}
