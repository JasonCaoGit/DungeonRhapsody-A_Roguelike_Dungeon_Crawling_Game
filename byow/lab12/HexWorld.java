package byow.lab12;
import byow.lab12.out.production.lab12.byow.lab12.Hexagon;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */

//create a hexagon class that has  attributes, top left, middle right, in a hashmap to represent x,y coordinates.
public class HexWorld {
    private TETile[][] world;


    public Hexagon drawItOnTheLeft3(Hexagon hex,TETile t) {
        Hexagon newHex = new Hexagon(hex.getMiddleRight().x - 9, hex.getMiddleRight().y, 3);
        addHexagon(newHex, t);
        return newHex;
    }

    public Hexagon drawItOnTheRight3(Hexagon hex,TETile t) {
          Hexagon newHex = new Hexagon(hex.getMiddleRight().x+1, hex.getMiddleRight().y, 3);
        addHexagon(newHex, t);
        return newHex;
    }

    public static void renderWorld(TETile[][] world) {
        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);
         ter.renderFrame(world);

    }
    //randomly choose a tile
    //draw the first hexagon on the top
    //calculate where the next should land and continue drawing
    //from left to right

    public void resetWorld(int width, int height) {
            world = new TETile[width][height];
            for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

    }
    public void drawHexagons(int length) {
/*            public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");*/
        TETile grass = Tileset.GRASS;
        TETile water = Tileset.WATER;
        TETile flower = Tileset.FLOWER;
        TETile mountain = Tileset.MOUNTAIN;
        TETile tree = Tileset.TREE;
        ArrayList<TETile> tiles = new ArrayList<TETile>();
        tiles.add(grass);
        tiles.add(water);
        tiles.add(flower);
        tiles.add(mountain);
        tiles.add(tree);
        Random rand = new Random();
        TETile currentTile = null;
        Hexagon hex = null;

        this.resetWorld(30, 30);

        switch(length) {
            case 3:

                currentTile = tiles.get(rand.nextInt(tiles.size()));
                hex = new Hexagon(13, 29, 3);
                addHexagon(hex, currentTile);
                currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft = drawItOnTheLeft3(hex, currentTile);
                currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight = drawItOnTheRight3(hex, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft1 = drawItOnTheLeft3(hexLeft, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexMid1 = drawItOnTheRight3(hexLeft, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight1 = drawItOnTheRight3(hexRight, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft2 = drawItOnTheLeft3(hexMid1, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight2  = drawItOnTheRight3(hexMid1, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));

                 Hexagon hexLeft3 = drawItOnTheLeft3(hexLeft2, currentTile);
                 currentTile = tiles.get(rand.nextInt(tiles.size()));
                 Hexagon hexMid3 = drawItOnTheRight3(hexLeft2, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight3 = drawItOnTheRight3(hexRight2, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft4 = drawItOnTheLeft3(hexMid3, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight4 = drawItOnTheRight3(hexMid3, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft5 = drawItOnTheLeft3(hexLeft4, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexMid5 = drawItOnTheRight3(hexLeft4, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight5 = drawItOnTheRight3(hexRight4, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexLeft6 = drawItOnTheLeft3(hexMid5, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexRight6 = drawItOnTheRight3(hexMid5, currentTile);
                  currentTile = tiles.get(rand.nextInt(tiles.size()));
                Hexagon hexMid7 = drawItOnTheRight3(hexLeft6, currentTile);
                break;
        }


    }




    public static void main(String[] args) {

        TETile t = Tileset.SAND;
        Hexagon hex = new Hexagon(5, 20, 2);
        HexWorld hw = new HexWorld(30,30);
        Hexagon hex1 = new Hexagon(20, 20, 5);
        TETile t1 = Tileset.GRASS;
        TETile[][] hexworld = new TETile[60][30];
        renderWorld(hw.world);
        hw.drawHexagons(3);

    }


    public TETile[][] getWorld() {
        return world;
    }


    public HexWorld(int width , int height) {
        world = new TETile[width][height];
           for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    //a method that takes in a hexagon object and draw it to the map
    //we will use a helper method which fill a line of x tiles starting from a point
    //It takes in a tile, a int and a point

    //draw a line of tiles in the given length
    public void drawLine(TETile tile, int X, int Y, int length) {






         for (int x = X; x < X + length; x += 1) {

                world[x][Y] = tile;

        }


    }


    public TETile[][] addHexagon(Hexagon hex, TETile tile) {
        int length = hex.getLength();
        int topLeftX = hex.getTopLeft().x;
        int topLeftY = hex.getTopLeft().y;
        TERenderer ter = new TERenderer();


        ter.initialize(world.length, world[0].length);

        switch (length) {
            case 2:
                try{
                    this.drawLine(tile, topLeftX, topLeftY, 2);
                this.drawLine(tile, topLeftX - 1, topLeftY - 1, 4);
                this.drawLine(tile, topLeftX - 1, topLeftY - 2, 4);
                this.drawLine(tile, topLeftX, topLeftY - 3, 2);
                }catch(Exception e){
                    e.printStackTrace();
                }

                ter.renderFrame(this.world);
                break;
            case 3:
                  try{
                    this.drawLine(tile, topLeftX, topLeftY, 3);
                drawLine(tile, topLeftX - 1, topLeftY - 1, 5);
                drawLine(tile, topLeftX - 2, topLeftY - 2, 7);
                drawLine(tile, topLeftX-2, topLeftY - 3, 7);
                drawLine(tile, topLeftX-1, topLeftY - 4, 5);
                drawLine(tile, topLeftX, topLeftY - 5, 3);
                }catch(Exception e){
                    e.printStackTrace();
                }

                ter.renderFrame(this.world);

                break;
            case 4:
                  try{
                    drawLine(tile, topLeftX, topLeftY, 4);
                drawLine(tile, topLeftX - 1, topLeftY - 1, 6);
                drawLine(tile, topLeftX - 2, topLeftY - 2, 8);
                      drawLine(tile, topLeftX - 3, topLeftY - 3, 10);
                      drawLine(tile, topLeftX - 3, topLeftY - 4, 10);
                      drawLine(tile, topLeftX - 2, topLeftY - 5, 8);
                      drawLine(tile, topLeftX - 1, topLeftY - 6, 6);
                      drawLine(tile, topLeftX , topLeftY - 7, 4);
                }catch(Exception e){
                    e.printStackTrace();
                }

                ter.renderFrame(this.world);

                break;
            case 5:
             try{
                    drawLine(tile, topLeftX, topLeftY, 5);
                drawLine(tile, topLeftX - 1, topLeftY - 1, 7);
                drawLine(tile, topLeftX - 2, topLeftY - 2, 9);
                      drawLine(tile, topLeftX - 3, topLeftY - 3, 11);
                      drawLine(tile, topLeftX - 4, topLeftY - 4, 13);
                      drawLine(tile, topLeftX - 4, topLeftY - 5, 13);
                      drawLine(tile, topLeftX - 3, topLeftY - 6, 11);
                      drawLine(tile, topLeftX - 2, topLeftY - 7, 9);
                 drawLine(tile, topLeftX - 1, topLeftY - 8, 7);
                 drawLine(tile, topLeftX , topLeftY - 9, 5);
                }catch(Exception e){
                    e.printStackTrace();
                }


                ter.renderFrame(this.world);


                break;
        }
        return this.world;

    }








}
