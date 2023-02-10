/**
 * Main file for the ray casting game.
 * <a href="https://en.wikipedia.org/wiki/Ray_casting">Read more here</a>
 *
 * @author Aditya B
 * @date 25 January 2023
 */
public class Main {
    public static void main(String[] args) {
        // Initialize game data
        Data data = new Data();
        // Create the game renderer by passing initialized data
        Renderer renderer = new Renderer("Ray Caster by Aditya B", data);
    }
}
