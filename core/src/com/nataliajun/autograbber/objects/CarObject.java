package com.nataliajun.autograbber.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import com.nataliajun.autograbber.GameSettings;

public class CarObject extends GameObject {

    long lastShotTime;
    int livesLeft;

    int startY;

    public CarObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.CAR_BIT, world);

        startY = y;

        body.setLinearDamping(10);
        livesLeft = 10;
    }


    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void draw(SpriteBatch batch) {
        putInFrame();
        super.draw(batch);

        if(getY() != startY) {
            body.applyForceToCenter(new Vector2(
                            0, startY - getY()),
                    true
            );
        }
    }

    public void move(Vector3 vector3) {
        Vector2 movementVector = new Vector2(
                (borderMovements(vector3.x) - getX()) * GameSettings.CAR_FORCE_RATIO,
                0);

        body.applyForceToCenter(movementVector,
                true
        );
    }

    private float borderMovements(float x){
        float r = x;

        int leftBorder = 182;
        int rightBorder = 528;

        if(r < leftBorder) r = leftBorder;
        if(r > rightBorder) r = rightBorder;

        return r;
    }

    private void putInFrame() {
        if (getY() > (GameSettings.SCREEN_HEIGHT / 2f - height / 2f)) {
            setY((int) (GameSettings.SCREEN_HEIGHT / 2f - height / 2f));
        }
        if (getY() <= (height / 2f)) {
            setY(height / 2);
        }
        if (getX() < (-width / 2f)) {
            setX(GameSettings.SCREEN_WIDTH);
        }
        if (getX() > (GameSettings.SCREEN_WIDTH + width / 2f)) {
            setX(0);
        }
    }

    @Override
    public void hit(short BIT) {
        if(BIT == GameSettings.TRASH_BIT){
            livesLeft -= 1;
        } else if(livesLeft < 10){
            livesLeft += 1;
        }
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public void drainHealth(){
        livesLeft -= 1;
    }
}
