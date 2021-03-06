package org.crumbleworks.forge.karmen.objects;

import org.crumbleworks.forge.karmen.physics.FixtureType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class DollContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == FixtureType.FLOOR || contact.getFixtureB().getUserData() == FixtureType.FLOOR) {
            Gdx.app.debug("COLLISION", "STH hit the floor!");
            Fixture actor;
            
            if(contact.getFixtureA().getUserData() == FixtureType.FLOOR) {
                actor = contact.getFixtureB();
            } else if(contact.getFixtureB().getUserData() == FixtureType.FLOOR) {
                actor = contact.getFixtureA();
            } else {
                return;
            }
            
            if(actor.getUserData() == FixtureType.DOLL) {
                Gdx.app.debug("COLLISION", " twas a DOLL!");
                StatefulDoll sd = ((StatefulDoll)actor.getBody().getUserData());
                sd.land();
            }
        }
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
}
