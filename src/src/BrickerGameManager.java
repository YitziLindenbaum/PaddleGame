package src;

import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.util.Random;

/**
 * Principle class of Bricker game.
 */
public class BrickerGameManager extends GameManager {

    // constant distances are in numbers of pixels
    private static final float BALL_SIZE = 20;
    private static final float BALL_SPEED = 200;
    private static final float PADDLE_SIZE_Y = 15;
    private static final float PADDLE_SIZE_X = 100;
    private static final float PADDLE_DIST_FROM_BOTTOM = 30;
    private static final int BRICKS_PER_ROW = 8;
    private static final int BRICKS_PER_COLUMN = 5;
    private static final float BRICK_HEIGHT = 15;
    private static final int MIN_DIST_FROM_EDGE = 10;
    private static final float COUNTERS_X = 10;
    private static final float G_COUNTER_Y_FROM_BTM = 60;
    private static final float COUNTERS_HEIGHT = 15;
    private static final float COUNTERS_WIDTH = 15;
    private static final int NUM_LIVES = 2;
    private static final float N_COUNTER_Y_FROM_BTM = 90;
    private static int BORDER_WIDTH = 5;
    private static final float BRICK_BORDER_CLEARANCE = 5;
    private static final float BRICK_BRICK_CLEARANCE = 1;
    private final float brickWidth;
    private final Vector2 windowDimensions;
    private Ball ball;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Counter brickCounter = new Counter();
    private Counter livesCounter = new Counter();


    /**
     * Constructor for Bricker Game Manager.
     * @param windowTitle Title to be given to window that houses game.
     * @param windowDimensions Dimensions of window to house game.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        float totalBricksWidth = windowDimensions.x() - (2 * (BORDER_WIDTH + BRICK_BORDER_CLEARANCE)) -
                ((BRICKS_PER_ROW - 1) * BRICK_BRICK_CLEARANCE);
        brickWidth = totalBricksWidth / BRICKS_PER_ROW;
    }

    /**
     * Creates and places all the necessary objects to begin Bricker game.
     * @param imageReader object to process images that will compose game objects.
     * @param soundReader object to process sound files that will compose game sounds.
     * @param inputListener object to process user input.
     * @param windowController object to control dialogue windows etc.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        livesCounter.increaseBy(NUM_LIVES);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;

        windowController.setTargetFramerate(150);

        initializeBackground();
        initializeWalls();
        initializeBall();
        initializePaddle();
        initializeBricks();
        initializeGraphicCounter();
        initializeNumCounter();
    }

    /**
     * Creates and places numeric life counter.
     */
    private void initializeNumCounter() {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(livesCounter, Vector2.of(COUNTERS_X,
                windowDimensions.y() - N_COUNTER_Y_FROM_BTM), Vector2.of(COUNTERS_WIDTH, COUNTERS_HEIGHT),
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter);
    }

    /**
     * Creates and places graphic life counter.
     */
    private void initializeGraphicCounter() {
        Renderable widgetRenderable = imageReader.readImage("assets/heart.png", true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(Vector2.of(COUNTERS_X,
            windowDimensions.y() - G_COUNTER_Y_FROM_BTM),
                Vector2.of(COUNTERS_WIDTH, COUNTERS_HEIGHT), livesCounter, widgetRenderable,
                gameObjects(), NUM_LIVES);
        gameObjects().addGameObject(graphicLifeCounter);
    }

    /**
     * Function to be run at every iteration of game.
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkLifeLost();
        checkGameEnd();
    }

    /**
     * Checks if the game has ended based on the game's logic and communicates with user accordingly.
     */
    private void checkLifeLost() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) { // ball has passed paddle on bottom of screen.
            livesCounter.decrement();
            if (livesCounter.value() == 0) {
                return;
            }
            String prompt = "You lost a life!";
            windowController.showMessageBox(prompt);
            ball.setCenter(windowDimensions.mult(0.5f));
        }
    }

    private void checkGameEnd(){
        String prompt = "";
        if (brickCounter.value() == 0) {
            prompt = "You win!";
        }
        if (livesCounter.value() == 0) {
            prompt = "You lose!";
        }
        if (!(prompt.isEmpty())) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Creates and places brick objects.
     */
    private void initializeBricks() {

        float brickLocY = BORDER_WIDTH + BRICK_BORDER_CLEARANCE; // y-value of location of top row bricks.
        for (int i = 0; i < BRICKS_PER_COLUMN; i++) {
            float brickLocX = BORDER_WIDTH + BRICK_BORDER_CLEARANCE; // x-value of loc of left column bricks.
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                initializeSingleBrick(brickLocX, brickLocY);
                brickLocX += brickWidth + BRICK_BRICK_CLEARANCE; // increment to next column
            }
            brickLocY += BRICK_HEIGHT + BRICK_BRICK_CLEARANCE; // increment to next row
        }

    }

    /**
     * Creates and places a single brick in the given location.
     * @param brickLocX x-value of top-left corner of brick to be created.
     * @param brickLocY y-value of top-left corner of brick to be created.
     */
    private void initializeSingleBrick(float brickLocX, float brickLocY) {
        GameObject brick = new Brick(Vector2.of(brickLocX, brickLocY),
                Vector2.of(brickWidth, BRICK_HEIGHT),
                imageReader.readImage("assets/brick.png", false),
                new CollisionStrategy(gameObjects()), brickCounter);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
        brickCounter.increment();
    }

    /**
     * Creates and places background picture.
     */
    private void initializeBackground() {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }


    /**
     * Creates and places ball object.
     */
    private void initializeBall() {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage,
                collisionSound);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;

        // ball starts with randomly chosen diagonal velocity.
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(Vector2.of(ballVelX, ballVelY));

        // place ball in center
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }


    /**
     * Creates and places paddle object.
     */
    private void initializePaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, Vector2.of(PADDLE_SIZE_X, PADDLE_SIZE_Y), paddleImage,
                inputListener,
                windowDimensions, MIN_DIST_FROM_EDGE);
        paddle.setCenter(Vector2.of(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_DIST_FROM_BOTTOM));
        gameObjects().addGameObject(paddle);
    }


    /**
     * Creates and places border that keeps ball in window.
     */
    private void initializeWalls() {
        GameObject wallLeft = new GameObject(Vector2.ZERO, Vector2.of(BORDER_WIDTH, windowDimensions.y()),
                null);
        GameObject wallRight = new GameObject(Vector2.of(windowDimensions.x() - BORDER_WIDTH, 0),
                Vector2.of(BORDER_WIDTH, windowDimensions.y()), null);
        GameObject wallTop = new GameObject(Vector2.ZERO,
                Vector2.of(windowDimensions.x(), BORDER_WIDTH), null);

        gameObjects().addGameObject(wallLeft);
        gameObjects().addGameObject(wallRight);
        gameObjects().addGameObject(wallTop);
    }

    /**
     * Main function to start game.
     * @param args
     */
    public static void main(String[] args) {
        new BrickerGameManager("Bricker",
                new Vector2(700, 500)).run();
    }
}
