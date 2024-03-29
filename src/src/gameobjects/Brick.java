package src.gameobjects;

import danogl.util.Counter;
import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a brick in the game.
 */
public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;
    private Counter brickCounter;


    /**
     * Constructor for brick object.
     * @param topLeftCorner – Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *               top-left corner of the window.
     * @param dimensions – Width and height in window coordinates.
     * @param renderable – The renderable representing the object. Can be null, in which case the Brick
     * will not be rendered.
     * @param collisionStrategy Object to handle collisions between bricks and other objects.
     * @param brickCounter Counter object ot keep track of existing bricks.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter brickCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickCounter = brickCounter;
    }

    /**
     * Handle collisions with other objects.
     * @param other The other object in the collision.
     * @param collision Collision object to represent collision in game.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
        brickCounter.decrement();
    }
}
