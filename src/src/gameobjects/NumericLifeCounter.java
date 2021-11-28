package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents numeric life counter.
 */
public class NumericLifeCounter extends GameObject {

    private Counter livesCounter;
    private GameObject renderedObject;
    private int curLives;
    private TextRenderable livesRenderable;

    /**
     * Create a new numeric life counter object.
     * @param livesCounter Game's global life counter.
     * @param topLeftCorner of Counter in window.
     * @param dimensions in pixels.
     * @param gameObjectCollection of global Game.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                       GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        curLives = livesCounter.value();

        String livesStr = Integer.toString(livesCounter.value());
        livesRenderable = new TextRenderable(livesStr);
        livesRenderable.setColor(Color.RED);
        renderedObject = new GameObject(topLeftCorner, dimensions, livesRenderable);
        gameObjectCollection.addGameObject(renderedObject, Layer.BACKGROUND);

    }

    /**
     * Called once per frame.
     * @param deltaTime See parent.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() != curLives) { // number of lives has changed.
            livesRenderable.setString(Integer.toString(livesCounter.value()));
            curLives = livesCounter.value();
        }
    }
}
