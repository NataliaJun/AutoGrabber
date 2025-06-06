package com.nataliajun.autograbber.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nataliajun.autograbber.GameSettings;
import com.nataliajun.autograbber.objects.GameObject;

public class ContactManager {

    World world;

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDef = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                Fixture fixCar;
                Fixture fixObj;

                int cDefObj;

                if(cDef == GameSettings.CAR_BIT){
                    fixCar = fixA;
                    fixObj = fixB;
                    cDefObj = cDef2;
                } else if(cDef2 == GameSettings.CAR_BIT){
                    fixCar = fixB;
                    fixObj = fixA;
                    cDefObj = cDef;
                } else {
                    ((GameObject)((cDef == GameSettings.TRASH_BIT ? fixA : fixB).getUserData()))
                            .hit(GameSettings.BONUS_BIT);
                    return;
                }

                if(cDefObj == GameSettings.TRASH_BIT) {
                    ((GameObject) fixCar.getUserData()).hit(GameSettings.TRASH_BIT);
                } else if(cDefObj == GameSettings.BONUS_BIT){
                    ((GameObject) fixCar.getUserData()).hit(GameSettings.BONUS_BIT);
                }

                ((GameObject) fixObj.getUserData()).hit(GameSettings.CAR_BIT);
                resetBodyMovement(fixObj.getBody());
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });


    }
    public void resetBodyMovement(Body body) {
        body.setLinearVelocity(0, 0);
        body.setAwake(false);
        body.setAwake(true);

        body.setLinearVelocity(new Vector2(0, -GameSettings.TRASH_VELOCITY));
    }
}
