package com.nataliajun.autograbber.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.nataliajun.autograbber.GameSettings.SCALE;

public class GameObject {

    public short cBits;

    public int width;
    public int height;

    public Body body;
    Texture texture;

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;

        texture = new Texture(texturePath);
        body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture,
                getX() - (width / 2f),
                getY() - (height / 2f),
                width,
                height);
    }

    public void hit(short BIT) {
        body.setLinearVelocity(new Vector2(0, 0));
    }

    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public void setX(int x) {
        body.setTransform(x * SCALE, body.getPosition().y, 0);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * SCALE, 0);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((float)(width + height)/2 * SCALE / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 2f;
        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        circleShape.dispose();

        body.setTransform(x * SCALE, y * SCALE, 0);
        return body;
    }

}
