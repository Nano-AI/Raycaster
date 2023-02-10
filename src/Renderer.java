/**
 * Rendering functions to display the actual game.
 *
 * @author Aditya B
 * @date 25 January 2023
 */

import jdk.jshell.execution.Util;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.net.URL;
import java.util.Random;


// Thanks to: https://www.youtube.com/watch?v=J7ylPL6LBuw
public class Renderer extends Canvas implements Runnable {
    // The JFrame for the window
    JFrame frame;
    // Thread on which the program is going to run on
    private Thread thread;
    // Boolean indicating whether the program is currently running
    boolean running = false;
    // Title of the window
    private String title;
    // Reference to the "Data" class with all the game values
    Data data;
    // Create buffer to hold the screen's buffer strategy
    BufferStrategy bs;

    // Create graphics, so we don't have to all a function every time to get it
    Graphics graphics;

    // Title is the name of the window.
    // Width and height are the width and height of the window
    // Data is the game values
    public Renderer(String title, Data data) {
        // Initialize class variables
        this.data = data;
        this.title = title;
        this.frame = new JFrame();
        // Create a Dimension for the screen size
        Dimension size = new Dimension(data.screenWidth, data.screenHeight);
        // Set size of the window
        frame.setSize(size);
        setPreferredSize(size);
        // Make sure program closes when the close button is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // No relative positioning
        frame.setLocationRelativeTo(null);
        // Make screen not resizeable
        frame.setResizable(false);
        // Make the window visible
        frame.setVisible(true);
        // Add the current class to the window
        frame.add(this);
        // Pack it to the window
        frame.pack();
        // Add event listeners for the program
        registerInput();
        // Start the game
        start();
        play(data.music);
    }

    // Play an audio file. NEEDS TO BE IN .wav FORMAT
    public void play(String file) {
        try {
            // Get the URL path of the file
            URL musicPath = (new File(file)).toURI().toURL();
            // Input it into the audio stream
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(musicPath);
            // Get the clip so it can be played
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            // Play the audio file
            clip.start();
        } catch (Exception ex) {
            // Throw an error if file cannot be found
            ex.printStackTrace();
        }
    }

    private void registerInput() {
        // Add key listener
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // up	 Get the sin and cos of the angle of the player to increment the player coordinates
                // down	 Get the sin and cos of the angle of the player to decrement the player coordinates
                // left	 Decrement the player angle
                // right Increment the player angle
                int code = e.getKeyCode();
                // Calculate the playerX and playerY values
                double playerCos = Math.cos(Utils.degreeToRads(data.playerAngle)) * data.playerMovementSpeed;
                double playerSin = Math.sin(Utils.degreeToRads(data.playerAngle)) * data.playerMovementSpeed;
                if (code == data.upKey) {
                    System.out.println("Moving forward");
                    // Add values since we're moving in the direction
                    double newX = data.playerX + playerCos, newY = data.playerY + playerSin;
                    if (data.At(newX, newY) == 0) {
                        data.playerX = newX;
                        data.playerY = newY;
                    }
                } else if (code == data.downKey) {
                    System.out.println("Moving backward");
                    // Subtract values since we're moving away
                    double newX = data.playerX - playerCos, newY = data.playerY - playerSin;
                    if (data.At(newX, newY) == 0) {
                        data.playerX = newX;
                        data.playerY = newY;
                    }
                } else if (code == data.rightKey) {
                    System.out.println("Turning right");
                    // Add to the player angle every time
                    data.playerAngle += data.playerRotationSpeed;
                } else if (code == data.leftKey) {
                    System.out.println("Turning left");
                    // Subtract to the player angle every time
                    data.playerAngle -= data.playerRotationSpeed;
                }

                if (Math.random() <= data.jumpScareChance) {
                    jumpScare();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public synchronized void start() {
        // Make sure that running is true so the while true loop runs
        running = true;
        // Create a thread for the program to run on
        thread = new Thread(this, "Display");
        // Start the thread
        thread.start();
    }

    public void jumpScare() {
        // Play jump scare audio
        play(data.jumpScare);
        // Get a random gif
        String gif = data.gifs[(int) (Math.random() * data.gifs.length)];
        try {
            // Create a new window
            JFrame boo = new JFrame("BOO!");
            // Get location of gif
            URL url = (new File(gif)).toURI().toURL();
            // Show the gif on the screen
            ImageIcon icon = new ImageIcon(url);
            JLabel label = new JLabel(icon);
            boo.getContentPane().add(label);
            // Set it to close when x pressed
            boo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // Set it always on top
            boo.setAlwaysOnTop(true);
            // Add contents
            boo.pack();
            // Make it visible
            boo.setVisible(true);
            // Set it to a random position on screen
            Random r = new Random();
            // Get the screen size
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();
            // Randomize new location
            int x = r.nextInt(d.width - getWidth());
            int y = r.nextInt(d.height - getHeight());
            // Set its new position
            boo.setLocation(x, y);
        } catch (Exception e) {
            // Throw an error if file is not found
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        // Make running false so the while loop breaks
        running = false;
        try {
            // Try to close the thread
            this.thread.join();
        } catch (InterruptedException e) {
            // Throw an error (most likely an interrupted operation or the thread was already stopped)
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Get the most precise timer down to the nanoseconds on the system
        long lastTime = System.nanoTime();
        // Get the current time in milliseconds
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        // Change between each run
        double delta = 0;
        // Total frames executed
        int frames = 0;
        // Initialize graphics
        setBufferStrategy();
        setGraphics();
        while (running) {
            // Update the current time
            long now = System.nanoTime();
            // Calculate the time since previous run
            delta += (now - lastTime) / ns;
            // Update the current time
            lastTime = now;
            // If delta is above or equal to one, we want to update the game
            // That way the update method isn't relative to the frame rate
            while (delta >= 1) {
                update();
                delta--;
            }
            // Render to the screen
            render();
            // Add one every time the while loop is run
            frames++;
            // Update the window title to the current frame rate
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }
        // Stop the program in case "running" was manually set to "false"
        stop();
    }

    public void update() {
    }

    public void render() {
        // Clear the screen to draw new things
        clearScreen();
        // Render the game
        rayCasting();
        bs.show();
    }

    private void rayCasting() {
        // Get the angle that player is facing in
        double rayAngle = data.playerAngle - data.playerHalfFOV;
        // Draw the lines on the screen
        for (int rayCount = 0; rayCount < data.screenWidth; rayCount++) {
            // Math to get the wall player is facing
            double rayCos = Math.cos(Utils.degreeToRads(rayAngle)) / data.rayCastPrecision;
            double raySin = Math.sin(Utils.degreeToRads(rayAngle)) / data.rayCastPrecision;
            double rayX = data.playerX;
            double rayY = data.playerY;
            int wall = 0;
            while (wall == 0) {
                rayX += rayCos;
                rayY += raySin;
                wall = data.At(rayX, rayY);
            }
            // Distance formula
            double distance = Math.sqrt(Math.pow(data.playerX - rayX, 2) + Math.pow(data.playerY - rayY, 2));
            // Since this will cause a fish eye effect, there is a fix for this : https://github.com/vinibiavatti1/RayCastingTutorial/wiki/Fisheye-fix
            distance = distance * Math.cos(Utils.degreeToRads(rayAngle - data.playerAngle));
            // Get the height of the wall depending on it's distance
            double wallHeight = Math.floor(data.halfHeight / distance);
            int distanceFactor = 50;
            // Fill the sky
            drawLine(rayCount, 0, rayCount, (int) (data.halfHeight - wallHeight),
                    Color.BLACK
            );
            // Draw the wall, make it darker depending on how far it is
            int greyValue = clamp((int) (255 - distance * distanceFactor), 0, 255);
            drawLine(rayCount, (int) (data.halfHeight - wallHeight), rayCount, (int) (data.halfHeight + wallHeight),
                    new Color(greyValue, greyValue, greyValue)
            );

            drawLine(rayCount, (int) (data.halfHeight + wallHeight), rayCount, data.screenHeight,
                    Color.DARK_GRAY
            );
            // Add to the angle of the ray
            rayAngle += data.rayCastIncrementAngle;
        }
    }

    // Clamp a value such that the min <= x <= max
    public static int clamp(int x, int min, int max) {
        if (x < min) {
            return min;
        }
        return Math.min(x, max);
    }

    private void setGraphics() {
        graphics = bs.getDrawGraphics();
    }

    // Sets  the BufferStrategy for the current screen so we can run graphics on it
    private void setBufferStrategy() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            // Create the buffer strategy in case it hasn't already
            createBufferStrategy(3);
            setBufferStrategy();
            return;
        }
        bs = bufferStrategy;
    }

    // Draw a line between two given points
    // Function overload so that we can either pass a Vector2 or each values individually
    public void drawLine(Vector2 p1, Vector2 p2, Color color) {
        drawLine(p1.x, p1.y, p2.x, p2.y, color);
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        // Get the screen's graphics
        Graphics g = getBufferStrategy().getDrawGraphics();
        // Set the color to the given color
        g.setColor(color);
        // Draw a line with the given x and y coordinates
        g.drawLine(x1, y1, x2, y2);
        // Remove any system resources that have been used already
        g.dispose();
        // Make sure to update the buffer with the current lines by adding it
    }

    public void clearScreen() {
        // Get the buffer strategy for the screen
        Graphics g = graphics;
        // Clears the screen by fulling the screen with the background color
        // getHeight() and getWidth() are options, but they might be more intensive ...
        // than just using the initialized variables
        g.clearRect(0, 0, data.screenWidth, data.screenHeight);
        // Remove any system resources that have been used already
        g.dispose();
        // Make sure to update the buffer after clearing it
    }
}
