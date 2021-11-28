package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 500;
    private final UserInputListener inputListener;
    private final float screenWidth;
    private int minDistanceFromEdge;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case object will not
     *                     be rendered
     * @param screenWidth
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.screenWidth = windowDimensions.x();
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        boolean tooFarLeft = getTopLeftCorner().x() <= minDistanceFromEdge;
        boolean tooFarRight = getTopLeftCorner().x() >=
                screenWidth - minDistanceFromEdge - getDimensions().x();

        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !(tooFarLeft)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !(tooFarRight)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
