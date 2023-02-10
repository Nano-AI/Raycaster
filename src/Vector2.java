/**
 * Basic Vector2 class meant to store coordinates of 2D points.
 *
 * @author Aditya B
 * @date 25 January 2023
 */

public class Vector2 {
    int x, y;
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    // Check if two vectors are equal to each other
    // Uses function overload for ease of access
    public boolean equals(Vector2 other) {
        return x == other.x && y == other.y;
    }
    public static boolean equals(Vector2 one, Vector2 two) {
        return one.x == two.x && one.y == two.y;
    }
}
