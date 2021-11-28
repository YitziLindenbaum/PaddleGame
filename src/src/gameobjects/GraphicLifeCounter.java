package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the graphic life-counter in the Bricker game.
 */
public class GraphicLifeCounter extends GameObject {

    private static final float ICON_CLEARANCE = 5; // distance between life icons
    private final Vector2 widgetTopLeftCorner;
    private final Vector2 widgetDimensions;
    private final Counter livesCounter;
    private final Renderable widgetRenderable;
    private final GameObjectCollection gameObjectsCollection;
    private final int numOfLives; // at beginning of game
    private int curLives;
    private GameObject[] widgets = null;

    /**
     *
     * @param widgetTopLeftCorner Position of the widget, in window coordinates (pixels).
     * @param widgetDimensions Width and height in window coordinates.
     * @param livesCounter Counter object to track number of lives.
     * @param widgetRenderable The renderable representing the widget. Can be null, in which case the
     *                         widget will not be rendered.
     * @param gameObjectsCollection Object representing the collection of objects in the game.
     * @param numOfLives Number of lives player begins with in game.
     */
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
        widgets = new GameObject[numOfLives];
        curLives = livesCounter.value();
        initializeCounter();
    }

    /**
     * Method to create and place expected number of counter icons.
     */
    private void initializeCounter() {
        for (int i = 0; i < numOfLives; i++) {
            Vector2 increment = Vector2.of(i * (widgetDimensions.x() + ICON_CLEARANCE), 0);
            widgets[i] = new GameObject(widgetTopLeftCorner.add(increment), widgetDimensions,
                    widgetRenderable);
            gameObjectsCollection.addGameObject(widgets[i], Layer.BACKGROUND); // add to background to avoid
                                                                            // collisions
        }
    }

    /**
     * To be called at every frame.
     * @param deltaTime See parent function.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() != curLives) { // number of lives has changed.
            gameObjectsCollection.removeGameObject(widgets[curLives - 1], Layer.BACKGROUND);
            curLives = livesCounter.value();
        }
    }

}
