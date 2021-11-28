package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class GraphicLifeCounter extends GameObject {

    private static final float ICON_CLEARANCE = 5;
    private final Vector2 widgetTopLeftCorner;
    private final Vector2 widgetDimensions;
    private final Counter livesCounter;
    private final Renderable widgetRenderable;
    private final GameObjectCollection gameObjectsCollection;
    private final int numOfLives;
    private GameObject[] lives = null;

    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);

        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.widgetDimensions = widgetDimensions;
        this.livesCounter = livesCounter;
        this.widgetRenderable = widgetRenderable;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        lives = new GameObject[numOfLives];
    }

//    private void initializeCounter() {
//
//        for (int i = 0; i < numOfLives; i++) {
//            Vector2 increment = Vector2.of(i * (widgetDimensions.x() + ICON_CLEARANCE), 0);
//            GameObject widget = new GameObject(widgetTopLeftCorner.add(increment), widgetDimensions,
//                    widgetRenderable);
//            gameObjectsCollection.addGameObject(widget, Layer.BACKGROUND); // add to background to avoid
//                                                                            // collisions
//        }
//    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int displayedLives = 0;
    }
}
