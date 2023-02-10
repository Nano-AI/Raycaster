/**
 * Data for the game. Can be edited to change properties of the game like width, height,
 * resolution, etc.
 *
 * @author Aditya B
 * @date 25 January 2023
 */

public class Data {
    // The default window width and height
    int screenWidth = 640, screenHeight = 480;
    // The middle of the screen`
    int halfWidth, halfHeight;
    // Delay time for each render iteration
    int renderDelay = 60;
    // The "resolution" of the graphics
    // A good value would be 64
    int rayCastPrecision = 64;
    // Value to increment each ray in relation to the screen width
    double rayCastIncrementAngle;
    // The player's field of view
    double playerFOV = 60;
    // Half of the player's FOV
    double playerHalfFOV;
    // Initial start X and Y position
    double playerX = 2, playerY = 2;
    // Angle of the player's position
    double playerAngle = 90;
    // Player speed value
    double playerMovementSpeed = 0.5;
    // Player rotation speed value
    double playerRotationSpeed = 5.0;
    // The map of the game
    int[][] map = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,1,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,0,1,0,0,1},
            {1,0,0,1,0,0,1,0,0,1},
            {1,0,0,1,0,0,1,0,0,1},
            {1,0,0,1,0,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1},
    };

    String[] gifs = {
        "./media/gif1.gif",
        "./media/gif2.gif",
        "./media/gif3.gif",
        "./media/gif4.gif",
        "./media/gif5.gif",
        "./media/gif6.gif",
        "./media/gif7.gif",
        "./media/gif8.gif",
        "./media/gif9.gif"
    };

    String jumpScare = "./media/jumpscare.wav";

    double jumpScareChance = 0.01f;

    // Key codes for each button press
    // 87 -> w
    // 65 -> a
    // 83 -> s
    // 68 -> d
    int upKey = 87, downKey = 83, leftKey = 65, rightKey = 68;
    String music = "./media/music.wav";
    public Data() {
        // Calculate half values and increment values
        halfHeight = screenHeight / 2;
        halfWidth = screenWidth / 2;
        playerHalfFOV = playerFOV / 2;
        // Math to calculate the increment angle
        rayCastIncrementAngle = this.playerFOV / this.screenWidth;
    }

    // Calculate the data at the closest cell
    // Used a lot to check for the type of cell at a point
    public int At(double x, double y) {
        return map[(int) Math.floor(y)][(int) Math.floor(x)];
    }
    public int At(Vector2 at) {
        return At(at.x, at.y);
    }
}
