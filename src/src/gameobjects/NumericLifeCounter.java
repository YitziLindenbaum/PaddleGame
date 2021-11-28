package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    private Counter livesCounter;
    private GameObject renderedObject;
    private int curLives;
    private TextRenderable livesRenderable;

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

    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() != curLives) {
            livesRenderable.setString(Integer.toString(livesCounter.value()));
            curLives = livesCounter.value();
        }
    }
}
