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
import src.gameobjects.Ball;
import src.gameobjects.Brick;
import src.gameobjects.Paddle;

import java.util.Random;


public class BrickerGameManager extends GameManager {

    private static final float BALL_SIZE = 20;
    private static final float BALL_SPEED = 200;
    private static final float PADDLE_SIZE_Y = 15;
    private static final float PADDLE_SIZE_X = 100;
    private static final float PADDLE_DIST_FROM_BOTTOM = 30;
    private static final int BRICKS_PER_ROW = 8;
    private static final int BRICKS_PER_COLUMN = 5;
    private static final float BRICK_HEIGHT = 15;
    private static final int MIN_DIST_FROM_EDGE = 10;
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


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        float totalBricksWidth = windowDimensions.x() - (2 * (BORDER_WIDTH + BRICK_BORDER_CLEARANCE)) -
                ((BRICKS_PER_ROW - 1) * BRICK_BRICK_CLEARANCE);
        brickWidth = totalBricksWidth / BRICKS_PER_ROW;
        brickCounter.reset();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

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
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameEnd();
    }

    private void checkGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if (ballHeight > windowDimensions.y()) {
            prompt = "You lose!";
        }
        if (brickCounter.value() == 0) {
            prompt = "You win!";
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

    private void initializeBricks() {

        float brickLocY = BORDER_WIDTH + BRICK_BORDER_CLEARANCE;
        for (int i = 0; i < BRICKS_PER_COLUMN; i++) {
            float brickLocX = BORDER_WIDTH + BRICK_BORDER_CLEARANCE;
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                initializeSingleBrick(brickLocX, brickLocY);
                brickLocX += brickWidth + BRICK_BRICK_CLEARANCE;
            }
            brickLocY += BRICK_HEIGHT + BRICK_BRICK_CLEARANCE;
        }

    }

    private void initializeSingleBrick(float brickLocX, float brickLocY) {
        GameObject brick = new Brick(Vector2.of(brickLocX, brickLocY),
                Vector2.of(brickWidth, BRICK_HEIGHT),
                imageReader.readImage("assets/brick.png", false),
                new CollisionStrategy(gameObjects()), brickCounter);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
        brickCounter.increment();
    }

    private void initializeBackground() {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }


    private void initializeBall() {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage,
                collisionSound);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(Vector2.of(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    private void initializePaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, Vector2.of(PADDLE_SIZE_X, PADDLE_SIZE_Y), paddleImage,
                inputListener,
                windowDimensions, MIN_DIST_FROM_EDGE);
        paddle.setCenter(Vector2.of(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_DIST_FROM_BOTTOM));
        gameObjects().addGameObject(paddle);
    }

    private void initializeWalls() {
        GameObject wallLeft = new GameObject(Vector2.ZERO, Vector2.of(BORDER_WIDTH, windowDimensions.y()),
                null);
        GameObject wallRight = new GameObject(Vector2.of(windowDimensions.x(), 0),
                Vector2.of(BORDER_WIDTH, windowDimensions.y()), null);
        GameObject wallTop = new GameObject(Vector2.ZERO,
                Vector2.of(windowDimensions.x(), BORDER_WIDTH), null);

        gameObjects().addGameObject(wallLeft);
        gameObjects().addGameObject(wallRight);
        gameObjects().addGameObject(wallTop);
    }


    public static void main(String[] args) {
        new BrickerGameManager("Bricker",
                new Vector2(700, 500)).run();
    }
}
