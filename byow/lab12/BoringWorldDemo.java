package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        //char character, Color textColor, Color backgroundColor, String description,
        //                  String filepath
        TETile wall = new TETile('w', Color.GREEN, Color.BLACK,"a wall tile", "byow\\lab12\\walls.png");
        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = wall;
            }
        }
        TETile floor = new TETile('w', Color.GREEN, Color.BLACK,"a floor tile", "byow\\lab12\\floor.png");

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 20; x < 35; x += 1) {
            for (int y = 5; y < 10; y += 1) {
                world[x][y] =Tileset.AVATAR;
            }
        }


             for (int x = 35; x < 59; x += 1) {
            for (int y = 2; y < 4; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }

                   // draws the world to the screen
        ter.renderFrame(world);
    }


}
