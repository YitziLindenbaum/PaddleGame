package src.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

class LifeCounter extends GameObject {


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case Counter will
     *                     not be rendered.
     */
    public LifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);

    }
}
