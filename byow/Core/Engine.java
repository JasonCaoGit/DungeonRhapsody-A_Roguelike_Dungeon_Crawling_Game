package byow.Core;

import java.io.*;
import java.nio.file.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 40;
    private final int numOfRooms = 20;
    TERenderer ter = new TERenderer();
    TETile[][] finalWorldFrame;
    private ArrayList<Room> rooms;
    private final ArrayList<Room> unconnectedRooms = new ArrayList<>();
    private final ArrayList<Position[]> allCriticalPoints = new ArrayList<>(); //the first is topleft, second is bottomright
    private KDTree allCenters;
    private final Map<Position, Room> centerToRoom = new HashMap<>();
    private final Map<Integer, Room> IDToRoom = new HashMap<>();
    private final DisjointSetForest<Integer> connectedUnion = new DisjointSetForest<>();
    private final ArrayList<Integer> roomIDs = new ArrayList<>();
    private final ArrayList<Position> allFloorTiles = new ArrayList<>();
    private Position characterPosition;
    private Random rand = new Random();
    private Avatar avatar;
    private int heart;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private Map<Position, Monster> positionToMonster = new HashMap<>();
    private String input = "";




//    /*
//    *
//    *1 Initialize bestKnownCost array with infinity for all tiles except source (set to 0)
//2 Initialize edgeTo array (best predecessor) with null for all tiles
//3 Initialize heuristics array with estimated cost to goal for each tile

    ///*
    //
    // use catersian distance?*/
//4 Create a priority queue ordered by (bestKnownCost + heuristic)
    // so the node with the minimum cost will be visited first, make sure to modify the value after you relax the edges
//5 Add source tile to the priority queue
//6 While priority queue is not empty:
//7 Remove tile with lowest (bestKnownCost + heuristic) from queue (current tile)


//8 If current tile is the goal, reconstruct and return the path using edgeTo array

    //that means we reached our goal
//9 For each neighbor of the current tile:


//10 Calculate tentative cost (current tile's bestKnownCost + cost to neighbor)
//11 If tentative cost < neighbor's bestKnownCost:
//12 Update neighbor's bestKnownCost to tentative cost
//13 Set neighbor's edgeTo to current tile
//14 Add neighbor to priority queue (or update its position if already in queue)
//15 If priority queue is empty, return failure (no path exists)
//
//Helper functions:
//16 calculateHeuristic(tile, goal): Estimate cost from tile to goal (e.g., Manhattan distance)
//17 reconstructPath(goal): Build path from source to goal using edgeTo array
//18 getNeighbors(tile): Return list of walkable adjacent tiles



    /*
     * (Random rand, Room currentRoom, Room otherRoom)
     * */
//    public void connectAllUnconnectedRooms(Random rand) {
//        Room[] rooms = findTwoRoomsToConnect();
//        while (rooms != null) {
//             Room room1 = rooms[0];
//         Room room2 = rooms[1];
//            Map<String, Object> roomInfo = pick2PointsAndRooms(rand, room1, room2);
//            connect2Rooms(rand, finalWorldFrame, roomInfo);
//            rooms = findTwoRoomsToConnect();
//        }
//
//
//    }

    public Engine() {


    }

    public static void main(String[] args) {

        Engine engine = new Engine();
//        engine.interactWithInputString("N673212421Swwdddaaa");
        engine.interactWithKeyboard();
//        engine.interactWithInputString("n123sdddddddddddddd");


//        Random rand = new Random();
//        int numOfRooms = rand.nextInt(20, 24);
//
//        engine.drawNRooms(rand, 10, 5, 20, 4, 5);
//        engine.connectUnconnectedRooms(rand);
//        engine.connectAllUnconnectedRooms(rand);
//
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        ter.renderFrame(engine.finalWorldFrame);
//        System.out.println(engine.connectedUnion);



        /* connect2Rooms(Random rand, TETile[][] world, Map<String, Object> roomsToConnect) */
//         pointsAndRooms.put("points", twoPoints);
//        pointsAndRooms.put("rooms", rooms);
//        pointsAndRooms.put("edge", edgeInts);*/
//         Map<String, Object> pick2PointsAndRooms(Random rand, Room currentRoom)


    }



    public void connectAllUnconnectedRooms(Random rand) {
        while (countDisjointSets() > 1) {
            Room[] rooms = findTwoRoomsToConnect();
            if (rooms == null) {
                // This should not happen if countDisjointSets() > 1
                break;
            }
            Map<String, Object> roomInfo = pick2PointsAndRooms(rand, rooms[0], rooms[1]);
            connect2Rooms(rand, finalWorldFrame, roomInfo);
            System.out.println(connectedUnion);
        }
    }

    public int[] findTwoUnconnectedRoomIDs() {
        int[] result = new int[2];
        for (int i = 0; i < roomIDs.size() - 1; i++) {
            int firstID = roomIDs.get(i);
            for (int j = i + 1; j < roomIDs.size(); j++) {
                int secondID = roomIDs.get(j);
                if (connectedUnion.find(firstID) != connectedUnion.find(secondID)) {
                    result[0] = firstID;
                    result[1] = secondID;
                    return result;
                }
            }
        }
        return null;
    }

    //so duplicates will not appear, if there is only 1 root, only 1 element in the set
    public int countDisjointSets() {
        Set<Integer> representatives = new HashSet<>();
        for (int roomID : roomIDs) {
            representatives.add(connectedUnion.find(roomID));
        }
        return representatives.size();
    }

    //find the rooms that are not in the same set
    //have a connectedUnion that keeps track what rooms are interconnected
    //so every time two room are chosen to be connected, union them in  the disjointUnion
    public Room[] findTwoRoomsToConnect() {
        Room[] rooms = new Room[2];

        int[] roomIDs = findTwoUnconnectedRoomIDs();
        if (roomIDs != null) {
            Room room1 = IDToRoom.get(roomIDs[0]);
            Room room2 = IDToRoom.get(roomIDs[1]);
            rooms[0] = room1;
            rooms[1] = room2;
            return rooms;
        } else {
            return null;
        }


    }

    //we have a list that has 2* unconnected rooms
    // we have a random number and try to connect that room to another room
    //we remove that room from our list until there is nothing
    public void connectUnconnectedRooms(Random rand) {
        while (!unconnectedRooms.isEmpty()) {
            int randomIndex = rand.nextInt(unconnectedRooms.size());
            Room room = unconnectedRooms.get(randomIndex);
            HashMap<String, Object> roomInfo = (HashMap<String, Object>) pick2PointsAndRooms(rand, room);
            connect2Rooms(rand, finalWorldFrame, roomInfo);


            //remove the room
            unconnectedRooms.remove(randomIndex);
        }


    }

    //Check all the tile in the array,
    //if it is a floor, use a helper to return a list of 8 tiles around it
    // if those positions have nothing, fill them with walls
    public void wrapAllFloorWithWall(TETile[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    Position floorPos = new Position(i, j);
                    ArrayList<Position> positions = floorPos.calcAroundArea();
                    for (Position pos : positions) {

                        int x = pos.x();
                        int y = pos.y();
                        if (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0) {
                            if (world[x][y].equals(Tileset.NOTHING)) {
                                world[x][y] = Tileset.WALL;
                            }
                        }

                    }
                }
            }
        }

    }

//    public void moveChar(char c) {
//
//
//        switch (c) {
//            case 'w' -> {
//                moveUp();
//            }
//            case 's' -> {
//                moveDown();
//            }
//            case 'a' -> {
//                moveLeft();
//            }
//            case 'd' -> {
//                moveRight();
//            }
//        }
//
//    }



    public Position randomPosition() {
        int positionInt = rand.nextInt(allFloorTiles.size());

        Position ret = allFloorTiles.get(positionInt);
        return ret;
    }


    public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    public void win() {
          StdDraw.clear(StdDraw.BLACK);

            // Draw the big title (always visible)
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 45));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 9, "You Won!");
               StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "You helped Knight Chris clear all monsters!");
               StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
                  StdDraw.text(WIDTH / 2, HEIGHT / 2 -1, "Feel proud of your self!");

            StdDraw.show();
    }


        public void tryAgain() {
               StdDraw.clear(StdDraw.BLACK);

            // Draw the big title (always visible)
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 45));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 9, "That is how tough a dungeon is!");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 +4, "Try AGAIN!");
            StdDraw.show();
        }

    public void enterYourSeed() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 40));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(WIDTH/2, HEIGHT/2, "ENTER A NUMBER TO GENERATE A MAP! ");
            StdDraw.show();
    }


        public void displaySeed(String seed) {



             StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 40));
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(WIDTH/2-10, HEIGHT/2, "Your Seed: " +seed);
            StdDraw.show();




        }


public void walkSound() {
    try {
        File tempAudioFile = extractResourceToTempFile("audio/WALKSOUND.wav");
        if (tempAudioFile != null && tempAudioFile.exists()) {
            System.out.println("WALKSOUND.wav found and extracted");
            StdAudio.playInBackground(tempAudioFile.getAbsolutePath());
        } else {
            System.err.println("Failed to extract WALKSOUND.wav");
        }
    } catch (IOException e) {
        System.err.println("Error playing WALKSOUND.wav: " + e.getMessage());
        e.printStackTrace();
    }
}

    public void attackSound() {
    try {
        File tempAudioFile = extractResourceToTempFile("audio/ATTACK.wav");
        if (tempAudioFile != null && tempAudioFile.exists()) {
            System.out.println("WALKSOUND.wav found and extracted");
            StdAudio.playInBackground(tempAudioFile.getAbsolutePath());
        } else {
            System.err.println("Failed to extract WALKSOUND.wav");
        }
    } catch (IOException e) {
        System.err.println("Error playing WALKSOUND.wav: " + e.getMessage());
        e.printStackTrace();
    }
    }
    public void staminaSound() {


 try {
        File tempAudioFile = extractResourceToTempFile("audio/HEALTHUP.wav");
        if (tempAudioFile != null && tempAudioFile.exists()) {
            System.out.println("WALKSOUND.wav found and extracted");
            StdAudio.playInBackground(tempAudioFile.getAbsolutePath());
        } else {
            System.err.println("Failed to extract WALKSOUND.wav");
        }
    } catch (IOException e) {
        System.err.println("Error playing WALKSOUND.wav: " + e.getMessage());
        e.printStackTrace();
    }

    }

    public void displayTitle() {
        String bigTitle = "BIG TITLE";
        String smallTitle = "Small Title";
        boolean running = true;

        while (running) {
            if (StdDraw.hasNextKeyTyped()) {
                  char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    running = false;
                    input += "n";
                    System.out.println("quit");
                    return;

                }
            }
            // Clear the canvas
            //display the screen that has the small title
            StdDraw.clear(StdDraw.BLACK);

            // Draw the big title (always visible)
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 60));
            StdDraw.text(WIDTH/2, HEIGHT/2+9, "Dungeon Rhapsody");

            // Draw the small title (flashing)
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "PRESS (N) TO START");


            // Show the drawing
            StdDraw.show();
                 if (StdDraw.hasNextKeyTyped()) {
                  char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    running = false;
                    input += "n";
                    System.out.println("quit");
                                        return;


                }
            }

            // Wait for 0.5 seconds
            StdDraw.pause(700);

            // Clear the canvas again
            //display the screen that does not have the title for
            StdDraw.clear(StdDraw.BLACK);

            // Draw only the big title
              StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 60));
            StdDraw.text(WIDTH/2, HEIGHT/2+9, "Dungeon Rhapsody");

            // Show the drawing
            StdDraw.show();
                 if (StdDraw.hasNextKeyTyped()) {
                  char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    running = false;
                    input += "n";
                    System.out.println("quit");
                                        return;


                }
            }

            // Wait for 0.3 seconds
            StdDraw.pause(300);
            System.out.println("running");





        }
    }

    public void flashTheOption() {
        Font title2 = new Font("Serif", Font.BOLD, 40);
        StdDraw.setFont(title2);
        StdDraw.text(WIDTH / 2, HEIGHT / 2+4, "press (n) to start");
        StdDraw.show(500);
        StdDraw.clear(Color.BLACK);

    }



    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
         StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Comic Sans", Font.PLAIN, TILE_SIZE);
        StdDraw.setFont(font);
        StdDraw.textLeft(1, HEIGHT - 1, "Player Health: " + avatar.getHeart());
        StdDraw.textLeft(1, HEIGHT - 2, "Player Stamina: " + avatar.getStamina());

        StdDraw.show();
    }

    public void chargeSound() {
 try {
        File tempAudioFile = extractResourceToTempFile("audio/CHARGE.wav");
        if (tempAudioFile != null && tempAudioFile.exists()) {
            System.out.println("WALKSOUND.wav found and extracted");
            StdAudio.playInBackground(tempAudioFile.getAbsolutePath());
        } else {
            System.err.println("Failed to extract WALKSOUND.wav");
        }
    } catch (IOException e) {
        System.err.println("Error playing WALKSOUND.wav: " + e.getMessage());
        e.printStackTrace();
    }

    }


    public void playBGM() {
  try {
        File tempAudioFile = extractResourceToTempFile("audio/BGM.wav");
        if (tempAudioFile != null && tempAudioFile.exists()) {
            System.out.println("WALKSOUND.wav found and extracted");
            StdAudio.loopInBackground(tempAudioFile.getAbsolutePath());
        } else {
            System.err.println("Failed to extract WALKSOUND.wav");
        }
    } catch (IOException e) {
        System.err.println("Error playing WALKSOUND.wav: " + e.getMessage());
        e.printStackTrace();
    }



    }








    }


    public class StaminaRecover {
        private Position position;


        public StaminaRecover(Position position) {
            this.position = position;
            finalWorldFrame[position.x()][position.y()] = Tileset.POTION;
        }
    }


private static File extractResourceToTempFile(String resourcePath) throws IOException {
    try (InputStream is = Engine.class.getResourceAsStream("/" + resourcePath)) {
        if (is == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        String fileName = new File(resourcePath).getName();
        Path tempFile = Files.createTempFile("temp_", fileName);
        Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Ensure the temp file is deleted on JVM exit
        tempFile.toFile().deleteOnExit();

        return tempFile.toFile();
    }
}






    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */


    public void interactWithKeyboard() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Initial world generation
        TETile[][] world = new TETile[WIDTH][HEIGHT];




        Monster monster1;
        Monster monster2;
        Monster monster3;
        Monster monster4;
        Monster monster5;
        Monster monster6;
        Monster monster7;
        Monster monster8;
        Sword sword;
        StaminaRecover staminaRecover1;
        StaminaRecover staminaRecover2;
        StaminaRecover staminaRecover3;
        StaminaRecover staminaRecover4;
        StaminaRecover staminaRecover5;
        StaminaRecover staminaRecover6;
        StaminaRecover staminaRecover7;
        ter.displayTitle();
        System.out.println("returned");
//        ter.displayOptions();
        ter.enterYourSeed();
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());



                seed += c;
                if (c == 's') {
                    break;
                }
                System.out.println(input);
                System.out.println(seed);

                ter.displaySeed(seed);
                System.out.println("reached");

            }
        }
        input += seed + 's';
        System.out.println("input is " + input);






                System.out.println(input);







                    interactWithInputString(input);

                    avatar = new Avatar(randomPosition());
                    monster1 = new Monster(randomPosition());
                    monster2 = new Monster(randomPosition());
                    monster3 = new Monster(randomPosition());
                    monster4 = new Monster(randomPosition());
                      monster5 = new Monster(randomPosition());
                    monster6 = new Monster(randomPosition());
                    monster7 = new Monster(randomPosition());
                    monster8 = new Monster(randomPosition());
                    staminaRecover1 = new StaminaRecover(randomPosition());
                    staminaRecover2 = new StaminaRecover(randomPosition());
                    staminaRecover3 = new StaminaRecover(randomPosition());
                    staminaRecover4 = new StaminaRecover(randomPosition());
                    staminaRecover5 = new StaminaRecover(randomPosition());
                    staminaRecover6 = new StaminaRecover(randomPosition());
                    staminaRecover7 = new StaminaRecover(randomPosition());
                    monsters.add(monster1);
                    monsters.add(monster2);
                    monsters.add(monster3);
                    monsters.add(monster4);
                    monsters.add(monster5);
                    monsters.add(monster6);
                    monsters.add(monster7);
                    monsters.add(monster8);
                    sword = new Sword(randomPosition());

                    //now make the enermy move to you

                    ter.renderFrame(finalWorldFrame);








        ter.playBGM();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());



                if (c == 'w' || c == 's' || c == 'a' || c == 'd' || c == 'e') {

                    input += c;
                    avatar.setIsAttacking(false);

                    avatar.moveChar(c);
                    //preserve the heart of
                    int tempHeart = avatar.getHeart();
                    //set the avatar to be invincible because he stuns people
                    if (c == 'e') {
                        avatar.setIsStunningMonster(true);

                    }

                    monster1.foundPlayer();
                    monster1.moveToCharacter1Step();
                    monster1.roamSlowly();
                    monster2.foundPlayer();
                    monster2.moveToCharacter1Step();
                    monster2.roamSlowly();
                        monster3.foundPlayer();
                    monster3.moveToCharacter1Step();
                    monster3.roamSlowly();
                        monster4.foundPlayer();
                    monster4.moveToCharacter1Step();
                    monster4.roamSlowly();
                    monster5.foundPlayer();
                    monster5.moveToCharacter1Step();
                    monster5.roamSlowly();
                    monster6.foundPlayer();
                    monster6.moveToCharacter1Step();
                    monster6.roamSlowly();
                        monster7.foundPlayer();
                    monster7.moveToCharacter1Step();
                    monster7.roamSlowly();
                        monster8.foundPlayer();
                    monster8.moveToCharacter1Step();
                    monster8.roamSlowly();
                    avatar.otherMovement(c);

                    avatar.setAvatarToRightPosture(c);


                    //if the avatar attack this round, its heart will be stored to the beginning of this round
                    //so nobody can hurt him if he is attackin
                    if (avatar.getIsAttacking()) {
                        avatar.setHeart(tempHeart);

                    }
                    //after the monster moves, next round the stunning effect does not happen
                    if (c == 'e') {
                        avatar.setIsStunningMonster(false);

                    }

                    if (avatar.getHeart() <= 0) {

                        System.out.println("Game Over");
                        ter.tryAgain();
                        System.out.println("reached");
                        break;

                    }
                    if (monsters.size() == 0) {
                        ter.win();
                        break;
                    }

                    ter.renderFrame(finalWorldFrame);



                }



            }
        }




    }




//    public void interactWithKeyboard() {
//        String input = "";
//
//
//           while (true) {
//            StdDraw.clear(Color.BLACK);
//               StdDraw.show();
//            if (StdDraw.hasNextKeyTyped()) {
//            char c = StdDraw.nextKeyTyped();
//                input += c;
//
//                //Start generating the world after use input 's'
//                if (Character.toLowerCase(c) == 's') {
//                    interactWithInputString(input);
//                    break;
//                }
//
//
//
//
//
//
//        }
//
//
//
//        }
//                ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//               ter.renderFrame(finalWorldFrame);
//
//           while (true) {
//
//            if (StdDraw.hasNextKeyTyped()) {
//            char c = Character.toLowerCase(StdDraw.nextKeyTyped());
//
//
//                //Start generating the world after use input 's'
//                if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
//                    input += c;
//                }
//          finalWorldFrame =   interactWithInputString(input);
//                System.out.println(input);
//ter.renderFrame(finalWorldFrame);
//
//
//
//
//
//        }
//
//
//
//
//        }
//
//
//
//    }


    //works
    public TETile[][] resetWorld(TETile[][] world) {

        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        return world;

    }

    //works
    public TETile[][] drawLine(TETile[][] world, TETile tile, Position pos, int length) {


        for (int x = pos.x(); x < pos.x() + length; x += 1) {
            if (x < 0 || x >= WIDTH || pos.y() < 0 || pos.y() >= HEIGHT) continue;
            world[x][pos.y()] = tile;

        }
        return world;


    }


    public TETile[][] drawLine(TETile[][] world, TETile tile, int X, int Y, int length) {


        for (int x = X; x < X + length; x += 1) {
            if (x < 0 || x > WIDTH || Y < 0 || Y > HEIGHT) continue;
            world[x][Y] = tile;

        }
        return world;


    }

    //works
    public void drawRoomMiddleLine(TETile[][] world, int width, Position start) {

        //draw one wall tile first
        world[start.x()][start.y()] = Tileset.WALL;


        //draw the remaining floor tile, use draw line function, poisition shift right 1 unit, width -2
        start = start.shift(1, 0);
        drawLine(world, Tileset.FLOOR, start, width - 2);


        //draw one tile

        start = start.shift(width - 2, 0);
        if (start.x() < 0 || start.x() >= WIDTH || start.y() < 0 || start.y() >= HEIGHT) {

        } else {

            world[start.x()][start.y()] = Tileset.WALL;
        }


    }

    public void drawNRooms(Random rand, int n, int minWidth, int maxWidth, int minHeight, int maxHeight) {
        for (int i = 0; i < n; i++) {
            Room newRoom = generateRoom(rand, maxWidth, minWidth, minHeight, maxHeight);
            drawRectangleRoom(finalWorldFrame, newRoom);


        }
        Room newRoom = generateRoom(rand, maxWidth, minWidth, minHeight, maxHeight);
        drawRectangleRoom(finalWorldFrame, newRoom);
        if (allCenters.size() > 1) {

            //TODO connect rooms
//            HashMap<String, Object> roomInfo = (HashMap<String, Object>) pick2PointsAndRooms(rand, newRoom);
//         connect2Rooms(rand, finalWorldFrame, roomInfo);
//         TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//         ter.renderFrame(finalWorldFrame);
        }


    }

    //@return a map that has "point" and "edge"(integer)
    public Map<String, Object> chooseRandomFromEdge(Random rand, Room room) {

        int edgeInt = rand.nextInt(4);
        HashMap<String, Object> map = new HashMap<>();

        ArrayList<Position> currentEdge = null;
        switch (edgeInt) {
            case 0 -> {
                currentEdge = room.getLeftEdge();
                break;
            }
            case 1 -> {
                currentEdge = room.getRightEdge();
                break;
            }
            case 2 -> {
                currentEdge = room.getTopEdge();
                break;
            }
            case 3 -> {
                currentEdge = room.getBottomEdge();
                break;
            }
        }

        //find a random point in the edge
        int pointsInEdge = currentEdge.size();
        int pointInt = rand.nextInt(pointsInEdge);
        Position currentPoint = currentEdge.get(pointInt);
        map.put("point", currentPoint);
        map.put("edge", edgeInt);

        return map;
    }

    /*
    * case 0 -> {
                currentEdge = room.getLeftEdge();
                break;
            }
            case 1 -> {
                currentEdge = room.getRightEdge();
                break;
            }
            case 2 -> {
                currentEdge = room.getTopEdge();
                break;
            }
            case 3 -> {
                currentEdge = room.getBottomEdge();
                break;
            }*/
    public Map<String, Object> pick2PointsAndRooms(Random rand, Room currentRoom) {
        //0 means leftEdge
        //1 means rightEdge
        //2 means topEdge
        //3 means bottomEdge
        int[] edgeInts = new int[2];//edgeInts corresponds to the edge we chose for the current room and the other room

        HashMap<String, Object> pointsAndRooms = new HashMap<>();

        //choose the edge from the room
        Position[] twoPoints = new Position[2];
        HashMap<String, Object> pointAndEdgeInt = (HashMap<String, Object>) chooseRandomFromEdge(rand, currentRoom);


        //find the edge point and the integer corresponding to the edge chosen
        Position currentPoint = (Position) pointAndEdgeInt.get("point");
        int currentEdgeInt = (Integer) pointAndEdgeInt.get("edge");
        //find the near point to the current point
        Position nearestPoint = allCenters.nearest(currentPoint.x(), currentPoint.y());
        //get the near room
        Room otherRoom = centerToRoom.get(nearestPoint);
        KDTree newTree = allCenters.clone();
        //if the conntected to has the current room, have a new KDTree that does not have the current nearest, do the whole process again
        //choose the edge and a point from that edge randomly, and return the 2 points
        while (true) {
            if (newTree.size() == 0) {
                return pointsAndRooms;
            }
            if (otherRoom.getConnectedTo().contains(currentRoom) || nearestPoint.equals(currentRoom.center())) {

                newTree.remove(nearestPoint);
                nearestPoint = newTree.nearest(currentPoint.x(), currentPoint.y());
                //get the near room
                otherRoom = centerToRoom.get(nearestPoint);

            } else {
                break;
            }
        }
        Room[] rooms = new Room[2];
        rooms[0] = currentRoom;
        rooms[1] = otherRoom;

        pointAndEdgeInt = (HashMap<String, Object>) chooseRandomFromEdge(rand, otherRoom);


        //find the edge point and the integer corresponding to the edge chosen

        int otherEdgeInt = (Integer) pointAndEdgeInt.get("edge");
        Position otherPoint = (Position) pointAndEdgeInt.get("point");
        twoPoints[0] = currentPoint;
        twoPoints[1] = otherPoint;
        edgeInts[0] = currentEdgeInt;
        edgeInts[1] = otherEdgeInt;
        pointsAndRooms.put("points", twoPoints);
        pointsAndRooms.put("rooms", rooms);
        pointsAndRooms.put("edge", edgeInts);


        return pointsAndRooms;


    }

    //pick 2 points from one given room to its nearest room
    //First choose the edge(in an array list of poistions) randomly, 4 choices
    // second from the edge, choose randomly 1 point
    //TODO Make sure you have KDTree that has all CENTERS of all rooms TODO, add centers to a KDTree when generating rooms DONE
    //TODO Make sure you have a map that maps center points to its room DONE
    //find the nearest point, and find that room, TODO add connectedTo attribute to all the rooms.
    //if the conntected to has the current room, have a new KDTree that does not have the current nearest, do the whole process again
    //choose the edge and a point from that edge randomly, and return the 2 points

    public Map<String, Object> pick2PointsAndRooms(Random rand, Room currentRoom, Room otherRoom) {

        //0 means leftEdge
        //1 means rightEdge
        //2 means topEdge
        //3 means bottomEdge
        int[] edgeInts = new int[2];//edgeInts corresponds to the edge we chose for the current room and the other room

        HashMap<String, Object> pointsAndRooms = new HashMap<>();

        //choose the edge from the room
        Position[] twoPoints = new Position[2];
        HashMap<String, Object> pointAndEdgeInt = (HashMap<String, Object>) chooseRandomFromEdge(rand, currentRoom);


        //find the edge point and the integer corresponding to the edge chosen
        Position currentPoint = (Position) pointAndEdgeInt.get("point");
        int currentEdgeInt = (Integer) pointAndEdgeInt.get("edge");
        //find the near point to the current point
        Position nearestPoint = allCenters.nearest(currentPoint.x(), currentPoint.y());
        //get the near room


//        Room otherRoom = centerToRoom.get(nearestPoint);
//        KDTree newTree = allCenters.clone();
//        //if the conntected to has the current room, have a new KDTree that does not have the current nearest, do the whole process again
//        //choose the edge and a point from that edge randomly, and return the 2 points
//        while (true) {
//            if (newTree.size() == 0) {
//                System.out.println("cannot connect");
//                return pointsAndRooms;
//            }
//            if (otherRoom.getConnectedTo().contains(currentRoom) || nearestPoint.equals(currentRoom.center())) {
//
//                newTree.remove(nearestPoint);
//                nearestPoint = newTree.nearest(currentPoint.x(), currentPoint.y());
//                //get the near room
//                otherRoom = centerToRoom.get(nearestPoint);
//
//            } else {
//                break;
//            }
//        }

        Room[] rooms = new Room[2];
        rooms[0] = currentRoom;
        rooms[1] = otherRoom;

        pointAndEdgeInt = (HashMap<String, Object>) chooseRandomFromEdge(rand, otherRoom);


        //find the edge point and the integer corresponding to the edge chosen

        int otherEdgeInt = (Integer) pointAndEdgeInt.get("edge");
        Position otherPoint = (Position) pointAndEdgeInt.get("point");
        twoPoints[0] = currentPoint;
        twoPoints[1] = otherPoint;
        edgeInts[0] = currentEdgeInt;
        edgeInts[1] = otherEdgeInt;
        pointsAndRooms.put("points", twoPoints);
        pointsAndRooms.put("rooms", rooms);
        pointsAndRooms.put("edge", edgeInts);


        return pointsAndRooms;


    }
    //pick 2 points from 2 give rooms

    //need to care about time complexity, choose the right data structure, avoid theta(n)
    //working now
    public void drawRectangleRoom(TETile[][] world, Room room) {

        int width = room.getWidth();
        int height = room.getHeight();
        //start of the line
        Position start = room.getTopLeft();
        //draw the top line
        drawLine(world, Tileset.WALL, start, width);
        for (int i = 0; i < height - 2; i++) {
            start = start.shift(0, -1);
            drawRoomMiddleLine(world, width, start);
        }
        start = start.shift(0, -1);
        drawLine(world, Tileset.WALL, start, width);


    }

    public HashMap<String, Object> genRoomInfo(Random rand, int minWidth, int maxWidth, int minHeight, int maxHeight) {
        HashMap<String, Object> info = new HashMap<>();
        int minTopLeftY = 3;//adjustable
        int maxTopLeftY = HEIGHT;
        int minTopLeftX = 0;
        int maxTopLeftX = WIDTH - 3; // adjustable


        int topLeftX = -1;
        int topLeftY = -1;
        int width = -1;
        int height = -1;
        int bottomRightX = -1;
        int bottomRightY = -1;
        boolean isEligibile = false;
        while (!isEligibile) {
            isEligibile = true;
            topLeftX = rand.nextInt(minTopLeftX, maxTopLeftX);

            topLeftY = rand.nextInt(minTopLeftY, maxTopLeftY);


            width = rand.nextInt(minWidth, maxWidth);
            height = rand.nextInt(minHeight, maxHeight);
            bottomRightX = topLeftX + width - 1;
            bottomRightY = topLeftY - height + 1;
            for (Position[] pos : allCriticalPoints) {
                Position topLeft = pos[0];
                Position bottomRight = pos[1];
                //if it is not what I want, what is inside makes the room eligible
                //add a constant to make the bound strict
                if (!(bottomRightY > topLeft.y() + 1 || topLeftY < bottomRight.y() - 1 || topLeftX > bottomRight.x() + 1 || bottomRightX < topLeft.x() - 1)) {
                    isEligibile = false;
                }
                if (topLeftX < 0 || topLeftY < 0 || topLeftX > WIDTH || topLeftY > HEIGHT ||
                        bottomRightX < 0 || bottomRightY < 0 || bottomRightY > HEIGHT || bottomRightX > WIDTH) {

                    isEligibile = false;
                }

                if (topLeftX < 0 || topLeftY < 0 ||
                        bottomRightX >= WIDTH || bottomRightY >= HEIGHT) {
                    isEligibile = false;
                }


            }

        }


        Position topLeft = new Position(topLeftX, topLeftY);
        Position bottomRight = new Position(bottomRightX, bottomRightY);
        info.put("topLeft", topLeft);
        info.put("bottomRight", bottomRight);
        info.put("width", width);
        info.put("height", height);
        Position[] pos = new Position[]{topLeft, bottomRight};
        allCriticalPoints.add(pos);
        return info;


        //1. we have to make sure the room does not exceed the boundary
        //2.
        //generate topleft


        //generate width and height, if they make the room out of bound regenerate, do it one by one


        //need to add bottom right and topleft position to the list


    }

    public Room generateRoom(Random rand, int minWidth, int maxWidth, int minHeight, int maxHeight) {
        HashMap<String, Object> posWidHei = genRoomInfo(rand, maxWidth, minWidth, minHeight, maxHeight);
        Position topLeft = (Position) posWidHei.get("topLeft");
        int width = (int) posWidHei.get("width");
        int height = (int) posWidHei.get("height");

        Room ret = new Room(width, height, topLeft);
        if (allCenters == null) {
            ArrayList<Position> centers = new ArrayList<>();
            centers.add(ret.center());
            allCenters = new KDTree(centers);
        } else {
            allCenters.insert(ret.center());

        }

        //put it in the center to room map
        centerToRoom.put(ret.center(), ret);
        if (unconnectedRooms.size() == 0) {
            unconnectedRooms.add(ret);
            unconnectedRooms.add(ret);
        } else {
            int randomIndex = rand.nextInt(unconnectedRooms.size());
            unconnectedRooms.add(randomIndex, ret);
            unconnectedRooms.add(ret);
        }


        //try to pick 2 points to connect, when there is already some rooms present
        if (allCenters.size() > 3) {
            HashMap<String, Object> pointsAndRooms = (HashMap<String, Object>) pick2PointsAndRooms(rand, ret);
            Position[] pos = (Position[]) pointsAndRooms.get("points");
            Room[] rooms = (Room[]) pointsAndRooms.get("rooms");


        }


//        int width = RandomUtils.uniform(rand, 4, 9);
//            int height = RandomUtils.uniform(rand, 4, 9);
//            int xLowerBound = 1;
//        int xUpperBound = WIDTH - width-1;
//        int yLowerBound = 1+ width;
//        int yUpperBound = HEIGHT - 1;
//            int x = RandomUtils.uniform(rand, xLowerBound, xUpperBound);
//            int y = RandomUtils.uniform(rand, yLowerBound, yUpperBound);
//            Position topLeft = new Position(x, y);
//            // Scan and see if there is
//            Room room = new Room(width, height, topLeft);

        //add its ID to the map
        IDToRoom.put(ret.ID(), ret);

        connectedUnion.makeSet(ret.ID());
        roomIDs.add(ret.ID());


        return ret;
    }

    //if you wanna draw a new wall, if that location already has a floor, do not draw on it
    public void drawLineToLeft(TETile[][] world, TETile tile, Position point, int length) {
        int startX = point.x();
        int startY = point.y();
        //if we are here to draw walls, do not make it intersect with any floor
        if (tile.equals(Tileset.WALL)) {
            for (int x = startX; x > startX - length; x--) {
                if (x < 0 || x >= WIDTH || startY < 0 || startY >= HEIGHT) continue;

                if (world[x][startY].equals(Tileset.FLOOR)) {
                    continue;
                }
                world[x][startY] = tile;
            }
        } else {
            for (int x = startX; x > startX - length; x--) {
                if (x < 0 || x >= WIDTH || startY < 0 || startY >= HEIGHT) continue;
                world[x][startY] = tile;
            }
        }


    }

    public void drawLineToRight(TETile[][] world, TETile tile, Position point, int length) {
        int startX = point.x();
        int startY = point.y();
        if (tile.equals(Tileset.WALL)) {
            for (int x = startX; x < startX + length; x++) {
                if (x < 0 || x >= WIDTH || startY < 0 || startY >= HEIGHT) continue;

                if (world[x][startY].equals(Tileset.FLOOR)) {
                    continue;
                }
                world[x][startY] = tile;
            }
        } else {
            for (int x = startX; x < startX + length; x++) {
                if (x < 0 || x >= WIDTH || startY < 0 || startY >= HEIGHT) continue;
                world[x][startY] = tile;
            }
        }


    }

    public void drawLineToUp(TETile[][] world, TETile tile, Position point, int length) {
        int startX = point.x();
        int startY = point.y();
        if (tile.equals(Tileset.WALL)) {
            for (int y = startY; y < startY + length; y++) {
                if (startX < 0 || startX >= WIDTH || y < 0 || y >= HEIGHT) continue;

                if (world[startX][y].equals(Tileset.FLOOR)) {
                    continue;
                }
                world[startX][y] = tile;
            }
        } else {

            for (int y = startY; y < startY + length; y++) {
                if (startX < 0 || startX >= WIDTH || y < 0 || y >= HEIGHT) continue;
                world[startX][y] = tile;
            }
        }


    }

    public void drawLineToDown(TETile[][] world, TETile tile, Position point, int length) {
        int startX = point.x();
        int startY = point.y();

        if (tile.equals(Tileset.WALL)) {
            for (int y = startY; y > startY - length; y--) {
                if (startX < 0 || startX >= WIDTH || y < 0 || y >= HEIGHT) continue;

                if (world[startX][y].equals(Tileset.FLOOR)) {
                    continue;
                }
                world[startX][y] = tile;
            }
        } else {
            for (int y = startY; y > startY - length; y--) {
                if (startX < 0 || startX >= WIDTH || y < 0 || y >= HEIGHT) continue;
                world[startX][y] = tile;
            }
        }


    }

    public Position drawHallwaysOnDir(Random rand, TETile[][] world, int currentEdgeInt, Position point, int minLength, int maxLength) {

        int length = rand.nextInt(minLength, maxLength);
        switch (currentEdgeInt) {
            case 0 -> {
                drawLineToLeft(world, Tileset.FLOOR, point, length);
                //draw additional wall
                Position leftWall = point.shift(-length, 0);
                //TODO check if works
                int x = leftWall.x();
                int y = leftWall.y();
                if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
                    drawLineToRight(world, Tileset.WALL, leftWall, 1);
                }

                //shift up in y one unit and draw the outside
                Position up = point.shift(0, +1);
                drawLineToLeft(world, Tileset.WALL, up, length + 1);
                Position down = point.shift(0, -1);
                drawLineToLeft(world, Tileset.WALL, down, length + 1);
                return point.shift(-length, 0);

            }
            case 1 -> {
                //also you have to draw a wall tile after the floor tile stops
                drawLineToRight(world, Tileset.FLOOR, point, length);
                Position rightWall = point.shift(length, 0);
                //TODO check if works

                int x = rightWall.x();
                int y = rightWall.y();
                if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
                    drawLineToRight(world, Tileset.WALL, rightWall, 1);
                }

                Position up = point.shift(0, +1);
                drawLineToRight(world, Tileset.WALL, up, length + 1);
                Position down = point.shift(0, -1);
                drawLineToRight(world, Tileset.WALL, down, length + 1);
                return point.shift(+length, 0);
            }

            case 2 -> {
                drawLineToUp(world, Tileset.FLOOR, point, length);
                Position upWall = point.shift(0, length);
                //TODO check if works
                int x = upWall.x();
                int y = upWall.y();
                if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
                    finalWorldFrame[upWall.x()][upWall.y()] = Tileset.WALL;
                }

                Position left = point.shift(-1, 0);
                drawLineToUp(world, Tileset.WALL, left, length + 1);
                Position right = point.shift(+1, 0);
                drawLineToUp(world, Tileset.WALL, right, length + 1);
                //TODO make sure it is right
                return point.shift(0, length);
            }
            case 3 -> {
                drawLineToDown(world, Tileset.FLOOR, point, length);
                Position downWall = point.shift(0, -length - 1);
                //TODO check if works

                int x = downWall.x();
                int y = downWall.y();
                if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
                    finalWorldFrame[downWall.x()][downWall.y()] = Tileset.WALL;
                }

                Position left = point.shift(-1, 0);
                drawLineToDown(world, Tileset.WALL, left, length + 1);
                Position right = point.shift(+1, 0);
                drawLineToDown(world, Tileset.WALL, right, length + 1);
                return point.shift(0, -length);
            }
        }
        return null;
    }


    //draw the line in some direction, need a draw line helper method

    public Position drawHallwaysOnDir(TETile[][] world, int currentEdgeInt, Position point, int length) {


        switch (currentEdgeInt) {
            case 0 -> {
                drawLineToLeft(world, Tileset.FLOOR, point, length);
                //draw additional wall
                Position leftWall = point.shift(-length, 0);
                //TODO check if works
                drawLineToRight(world, Tileset.WALL, leftWall, 1);
                //shift up in y one unit and draw the outside
                Position up = point.shift(0, +1);
                drawLineToLeft(world, Tileset.WALL, up, length + 1);
                Position down = point.shift(0, -1);
                drawLineToLeft(world, Tileset.WALL, down, length + 1);
                return point.shift(-length, 0);

            }
            case 1 -> {
                //also you have to draw a wall tile after the floor tile stops
                drawLineToRight(world, Tileset.FLOOR, point, length);
                Position rightWall = point.shift(length, 0);
                //TODO check if works
                drawLineToRight(world, Tileset.WALL, rightWall, 1);
                Position up = point.shift(0, +1);
                drawLineToRight(world, Tileset.WALL, up, length + 1);
                Position down = point.shift(0, -1);
                drawLineToRight(world, Tileset.WALL, down, length + 1);
                return point.shift(+length, 0);
            }

            case 2 -> {
                drawLineToUp(world, Tileset.FLOOR, point, length);
                Position upWall = point.shift(0, length);
                //TODO check if works
                drawLineToUp(world, Tileset.WALL, upWall, 1);
                Position left = point.shift(-1, 0);
                drawLineToUp(world, Tileset.WALL, left, length + 1);
                Position right = point.shift(+1, 0);
                drawLineToUp(world, Tileset.WALL, right, length + 1);
                //TODO make sure it is right
                return point.shift(0, length);
            }
            case 3 -> {
                drawLineToDown(world, Tileset.FLOOR, point, length);
                Position downWall = point.shift(0, -length);
                //TODO check if works
                drawLineToDown(world, Tileset.WALL, downWall, 1);
                Position left = point.shift(-1, 0);
                drawLineToDown(world, Tileset.WALL, left, length + 1);
                Position right = point.shift(+1, 0);
                drawLineToDown(world, Tileset.WALL, right, length + 1);
                return point.shift(0, -length);
            }
        }
        return null;
    }

    //roomstoconnect has the following info,
    /*
     * Room object that you will connect
     * the two points from the two rooms
     * what edge from these two rooms we chose
     * Algo:
     * Step 1: if you go from the right edge, go right some tiles first, same for other edges
     * there are four situation, the other room can be upleft, upright, downleft and down right, determine which is the case
     * if you have the other room in the upleft, first do step 1, then choose randomly between up or left direction and go some amount in that direction,
     * you have to make sure the resulting dx or dy does not exceed the difference in the room.x- otherroom.x, or y-y, we wanna make sure the hallway does not exceed the other point
     * if the dx or dy for one direction is all used, only go the other direction for the remaining dx or dy.
     * TODO set up dx and dy first, must be an integer and positive so use absolute value
     *
     *
     * */
    public void connect2Rooms(Random rand, TETile[][] world, Map<String, Object> roomsToConnect) {
        // the hallways has corners, we draw a straight hallway first, and we choose the direction in that hallway and keep drawing it
        //set up the how long 1 straight hallway should be
        int minLength = 2;
        int maxLength = 3; // adjustable, adjust to optimize


        /*
        *
        *    pointsAndRooms.put("points", twoPoints);
        pointsAndRooms.put("rooms", rooms);
        pointsAndRooms.put("edge", edgeInts);*/
        Room[] rooms = (Room[]) roomsToConnect.get("rooms");
        Room currentRoom = rooms[0];
        Room anotherRoom = rooms[1];

        int[] edgeInts = (int[]) roomsToConnect.get("edge");
        //union 2 rooms


        int currentEdgeInt = edgeInts[0];

        Position[] points = (Position[]) roomsToConnect.get("points");
        Position point = points[0];
        Position otherPoint = points[1];
        //TODO EW
        if (checkPositionValid(otherPoint)) {
            finalWorldFrame[otherPoint.x()][otherPoint.y()] = Tileset.FLOOR;
        }


        //calculate dx and dy, and what position it is

        //step 1 done
        //the starting point  for remaining action will be shifted

        Position newStart = drawHallwaysOnDir(rand, world, currentEdgeInt, point, minLength, maxLength);


        Direction dir = new Direction(newStart, otherPoint);
        int dx = dir.getDx();
        int dy = dir.getDy();
        RelativePosition relativePosition = dir.getRelativePosition();




        /*            * Step 1: if you go from the right edge, go right some tiles first, same for other edges
         * there are four situation, the other room can be upleft, upright, downleft and down right, determine which is the case
         * if you have the other room in the upleft, first do step 1, then choose randomly between up or left direction and go some amount in that direction,
         * you have to make sure the resulting dx or dy does not exceed the difference in the room.x- otherroom.x, or y-y, we wanna make sure the hallway does not exceed the other point
         * if the dx or dy for one direction is all used, only go the other direction for the remaining dx or dy.*/

        //first determine two directions, one direction belongs to x and another belongs to y
        int[] directions = new int[2];
        //x direction(left, right) at index 0, y direction at index 1

        /*
        *
        *        case 0 -> {
                drawLineToLeft(world, Tileset.FLOOR, point, length);
                //shift up in y one unit and draw the outside
                Position up = point.shift(0, +1);
                drawLineToLeft(world, Tileset.WALL, up, length);
                Position down = point.shift(0, -1);
                drawLineToLeft(world, Tileset.WALL, down, length);
                break;
            }
            case 1 -> {
                 drawLineToRight(world, Tileset.FLOOR, point, length);
                 Position up = point.shift(0, +1);
                drawLineToRight(world, Tileset.WALL, up, length);
                Position down = point.shift(0, -1);
                drawLineToRight(world, Tileset.WALL, down, length);
                break;
            }

            case 2 -> {
                drawLineToUp(world, Tileset.FLOOR, point, length);
                Position left = point.shift(-1, 0);
                drawLineToUp(world, Tileset.WALL, left, length);
                Position right  = point.shift(+1, 0);
                drawLineToUp(world, Tileset.WALL, right, length);
            }
            case 3 -> {
                drawLineToDown(world, Tileset.FLOOR, point, length);
                Position left = point.shift(-1, 0);
                drawLineToDown(world, Tileset.WALL, left, length);
                Position right  = point.shift(+1, 0);
                drawLineToDown(world, Tileset.WALL, right, length);
            }
        }*/
        //set the direction the hallway will
        switch (dir.getRelativePosition()) {
            case TOPLEFT -> {
                directions[0] = 0;
                directions[1] = 2;
                break;


            }

            case TOPRIGHT -> {
                directions[0] = 1;
                directions[1] = 2;
                break;
            }

            case BOTTOMLEFT -> {
                directions[0] = 0;
                directions[1] = 3;
                break;

            }

            case BOTTOMRIGHT -> {
                directions[0] = 1;
                directions[1] = 3;
                break;

            }
        }


        randomHallWayDrawer(rand, directions, newStart, dx, dy);
        wrapAllFloorWithWall(finalWorldFrame);

        connectedUnion.safeUnion(currentRoom.ID(), anotherRoom.ID());
        System.out.println(connectedUnion);


        //


    }

    //          * Step 1: if you go from the right edge, go right some tiles first, same for other edges
//    * there are four situation, the other room can be upleft, upright, downleft and down right, determine which is the case
//    * if you have the other room in the upleft, first do step 1, then choose randomly between up or left direction and go some amount in that direction,
//    * you have to make sure the resulting dx or dy does not exceed the difference in the room.x- otherroom.x, or y-y, we wanna make sure the hallway does not exceed the other point
//    * if the dx or dy for one direction is all used, only go the other direction for the remaining dx or dy.*/
    public void randomHallWayDrawer(Random rand, int[] directions, Position newStart, int dx, int dy) {
        //choose a direction, until dx or dy = 0
        int minLength = 3;//adjustable

        int smaller = -1;
        int bigger = -1;
        if (dx > dy) {
            bigger = dx;
            smaller = dy;

        } else {
            bigger = dy;
            smaller = dx;

        }
        if (smaller < minLength) {
            minLength = smaller - 1;

        }
        dx++;
        dy++;
        int maxLength = smaller;
        while (dx > 0 || dy > 0) {
            int choice = rand.nextInt(2);
            /*
             * drawHallwaysOnDir(TETile[][] world, int currentEdgeInt, Position point, int length)*/
            if (dx == 0) {
                choice = 1;
            }
            if (dy == 0) {
                choice = 0;
            }
            int direction = directions[choice];

            if (choice == 0) {

                //length
                int length = rand.nextInt(0, dx + 1);
                newStart = drawHallwaysOnDir(finalWorldFrame, direction, newStart, length);
                dx -= length;
            } else {

                int length = rand.nextInt(0, dy + 1);
                newStart = drawHallwaysOnDir(finalWorldFrame, direction, newStart, length);
                dy -= length;
            }
        }


    }

    //Check if the center is 10 tiles away from the current existing centers
    public boolean checkRoomEligibility(ArrayList<Room> rooms, Room room) {
//        for (Room existingRoom : rooms) {
//
//            int existingWidth = existingRoom.getWidth();
//            int existingHeight = existingRoom.getHeight();
//            //If the centerX is not at least 10 tiles away from the existing centers
//
//            //TODO you can adjust this 10
//
//
//
//
//
//        }

        return true;
    }

        //TODO debug

        public ArrayList<Position> get2TilesAroundCross(Position position) {
        ArrayList<Position> positions = new ArrayList<>();
                ArrayList<Position> validPositions = new ArrayList<>();


        Position pos1 = position.shift(-2, 0);
        positions.add(pos1);
        for (int i = 1; i < 5; i++) {
            positions.add(pos1.shift(i, 0));

        }
        Position pos2 = position.shift(0, -2);
        positions.add(pos2);
        for (int i = 1; i < 5; i++) {
            positions.add(pos2.shift(0, i));

        }


                for (Position p : positions) {
        int x = p.x();
        int y = p.y();
        if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
            validPositions.add(p);
        }
    }





        return validPositions;


    }


    public ArrayList<Position> get2TilesAround(Position position) {
        ArrayList<Position> positions = new ArrayList<>();
                ArrayList<Position> validPositions = new ArrayList<>();


        Position pos1 = position.shift(-2, 2);
        positions.add(pos1);
        for (int i = 1; i < 5; i++) {
            positions.add(pos1.shift(i, 0));

        }
        Position pos2 = position.shift(-2, 1);
        positions.add(pos2);
        for (int i = 1; i < 5; i++) {
            positions.add(pos2.shift(i, 0));

        }
        Position pos3 = position.shift(-2, 0);
        positions.add(pos3);
        for (int i = 1; i < 5; i++) {
            positions.add(pos3.shift(i, 0));

        }
        Position pos4 = position.shift(-2, -1);
        positions.add(pos4);
        for (int i = 1; i < 5; i++) {
            positions.add(pos4.shift(i, 0));
        }

        Position pos5 = position.shift(-2, -2);
        positions.add(pos5);
        for (int i = 1; i < 5; i++) {
            positions.add(pos5.shift(i, 0));

        }

                for (Position p : positions) {
        int x = p.x();
        int y = p.y();
        if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
            validPositions.add(p);
        }
    }





        return validPositions;


    }






    public ArrayList<Position> get3TilesAround(Position position) {
        ArrayList<Position> positions = new ArrayList<>();
           ArrayList<Position>   validPositions = new ArrayList<>();

        Position pos1 = position.shift(-3, 3);
        positions.add(pos1);
        for (int i = 1; i < 7; i++) {
            positions.add(pos1.shift(i, 0));

        }
        Position pos2 = position.shift(-3, 2);
        positions.add(pos2);
        for (int i = 1; i < 7; i++) {
            positions.add(pos2.shift(i, 0));

        }
        Position pos3 = position.shift(-3, 1);
        positions.add(pos3);
        for (int i = 1; i < 7; i++) {
            positions.add(pos3.shift(i, 0));

        }
        Position pos4 = position.shift(-3, 0);
        positions.add(pos4);
        for (int i = 1; i < 7; i++) {
            positions.add(pos4.shift(i, 0));
        }

        Position pos5 = position.shift(-3, -1);
        positions.add(pos5);
        for (int i = 1; i < 7; i++) {
            positions.add(pos5.shift(i, 0));

        }
        Position pos6 = position.shift(-3, -2);
        positions.add(pos6);
        for (int i = 1; i < 7; i++) {
            positions.add(pos6.shift(i, 0));

        }
        Position pos7 = position.shift(-3, -3);
        positions.add(pos7);
        for (int i = 1; i < 7; i++) {
            positions.add(pos7.shift(i, 0));

        }
         for (Position p : positions) {
        int x = p.x();
        int y = p.y();
        if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
            validPositions.add(p);
        }
    }

        return validPositions;


    }

    public ArrayList<Position> findPathToCharacter(Position monsterPos) {
        GameGraph game = new GameGraph(allFloorTiles);


        Position goal = characterPosition;

        AStarSolver<Position> solver = new AStarSolver<>(game, monsterPos, goal, 1999);
        return (ArrayList<Position>) solver.solution();
    }

    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        //Fill the world up first
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        resetWorld(finalWorldFrame);


        //Find the seed
        int start = input.toLowerCase().indexOf('n');
        int end = input.toLowerCase().indexOf('s');
        int seed = Integer.parseInt(input.substring(start + 1, end));


        //set the seed to a random object and use this random for anything randomly generated
        rand.setSeed(seed);

        //Build rooms, 5-10 number of rooms, with width and height from 6-11, check if they will intersect with all previous rooms
        //use loop to build the room use a helper function
        while (true) {
            try {
                int numOfRooms = rand.nextInt(20, 24);

                drawNRooms(rand, 10, 5, 20, 4, 5);
                connectUnconnectedRooms(rand);
                connectAllUnconnectedRooms(rand);


                break;

            } catch (Exception e) {
                rand = new Random(seed * 2L);
                int numOfRooms = rand.nextInt(20, 24);

                drawNRooms(rand, 10, 5, 20, 4, 5);
                connectUnconnectedRooms(rand);
                connectAllUnconnectedRooms(rand);


            }
        }

        //put the avatar in a random place
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (finalWorldFrame[i][j] == Tileset.FLOOR) {
                    allFloorTiles.add(new Position(i, j));
                }
            }
        }


        return finalWorldFrame;


    }

    public void resetChar() {
        finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.FLOOR;
        Position left = characterPosition.shift(-1, 0);
        Position right = characterPosition.shift(1, 0);
        if (checkPositionValid(left) && checkPositionValid(right)) {
              if (finalWorldFrame[left.x()][left.y()] == Tileset.AVATARSWORDL2
        ||finalWorldFrame[left.x()][left.y()] ==Tileset.AVATARBACKSWORDL2
    ) {
            finalWorldFrame[left.x()][left.y()] = Tileset.FLOOR;
        }
          if (finalWorldFrame[right.x()][right.y()].equals(Tileset.AVATARSWORDR2)
          ||finalWorldFrame[right.x()][right.y()] ==Tileset.AVATARBACKSWORDR2
       ) {
            finalWorldFrame[right.x()][right.y()] = Tileset.FLOOR;
        }
        }

    }

    public void resetToFloor(Position position) {
        finalWorldFrame[position.x()][position.y()] = Tileset.FLOOR;
    }

//    public void moveLeft() {
//
//        Position temp = characterPosition.shift(-1, 0);
//
//        int x = temp.x();
//        int y = temp.y();
//        if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
//        } else {
//            if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
//                return;
//            }
//            resetChar();
//            characterPosition = characterPosition.shift(-1, 0);
//
//            finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;
//            System.out.println("reached");
//        }
//
//    }
//
//    public void moveRight() {
//
//
//        Position temp = characterPosition.shift(+1, 0);
//
//        int x = temp.x();
//        int y = temp.y();
//        if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
//        } else {
//            if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
//                return;
//            }
//            resetChar();
//            characterPosition = characterPosition.shift(+1, 0);
//
//            finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;
//            System.out.println("reached");
//        }
//
//
//    }
//
//    public void moveUp() {
//        Position temp = characterPosition.shift(0, 1);
//        int x = temp.x();
//        int y = temp.y();
//        if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
//        } else {
//            if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
//                return;
//            }
//            resetChar();
//            characterPosition = characterPosition.shift(0, 1);
//
//            finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;
//            System.out.println("reached");
//        }
//
//
//    }
//
//    public void moveDown() {
//
//
//        Position temp = characterPosition.shift(0, -1);
//        int x = temp.x();
//        int y = temp.y();
//        if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
//            return;
//        } else {
//            if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
//                return;
//            }
//            resetChar();
//            characterPosition = characterPosition.shift(0, -1);
//
//            finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;
//            System.out.println("reached");
//        }
//    }

         public boolean checkPositionValid(Position position) {
            int x = position.x();
            int y = position.y();
            if(x>0&&x<WIDTH&& y>0&&y<HEIGHT) {
                return true;
            }
            return false;
        }

    public class Avatar {
        private final Position position;
        private boolean hasSword;
        //0 means left, 1 means right
        private int swordPosition;
        private int heart = 5;
        private boolean isStunningMonster;
        private boolean readyForAttack;
        private boolean isAttacking;
        private int maxStamina = 400;
        private int stamina = maxStamina;

        public int getStamina() {
            return stamina;
        }

        public void increaseStamina() {
            if (stamina < maxStamina) {
                stamina++;
            }
        }

        public void decreaseStamina(int num) {
            if (stamina - num < 0) {
                stamina = 0;
                return;

            }
            stamina -= num;
        }


        public void setReadyForAttack(boolean readyForAttack) {
            this.readyForAttack = readyForAttack;
        }

        public boolean getIsAttacking() {
            return isAttacking;
        }

        public void setIsAttacking(boolean isAttacking) {
            this.isAttacking = isAttacking;
        }

        public boolean isThereAMonster(Position position) {
            if (finalWorldFrame[position.x()][position.y()] == Tileset.MONSTER) {
                return true;
            }
            return false;
        }


        public void attack(char c) {
            System.out.println(heart);


            switch(c) {
                case 'w' -> {
                    Monster target;
                    ArrayList<Monster> monsters = getMonsters2TilesAroundCross(characterPosition);

                    for (Monster m : monsters) {
                        System.out.println("Monster at" + m.getPosition());
                        System.out.println("Avatar at" + characterPosition);
                        if (comparePositionRough(characterPosition, m.getPosition()) == 0) {

                            System.out.println("attacking");

                            moveUpDisregardMonster();
                            moveUpDisregardMonster();

                            // if the end has a monster, move that monster
                            Position nextStep = characterPosition.shift(0, 1);
                            while (isThereAMonster(nextStep)) {
                                positionToMonster.get(nextStep).roam();
                            }
                            moveUpDisregardMonster();
                            m.decreaseHeart();
                           if (m.getHeart()>0) {
                               m.dodge();
                               while (isThereAPlayer(m.getPosition())) {

                                   m.dodge();
                               }
                                    System.out.println("dodged once");
                            }
                            isAttacking = true;
                            readyForAttack = false;
                            if (m.getHeart() > 0) {
                                finalWorldFrame[m.position.x()][m.position.y()] = Tileset.MONSTER;

                            }


                        }
                    }

                }

                case 's' -> {
                    ArrayList<Monster> monsters = getMonsters2TilesAroundCross(characterPosition);

                     for (Monster m : monsters) {
                        System.out.println("Monster at" + m.getPosition());
                        System.out.println("Avatar at" + characterPosition);
                        if (comparePositionRough(characterPosition, m.getPosition()) == 1) {

                            System.out.println("attacking");

                            moveDownDisregardMonster();
                            moveDownDisregardMonster();
                            Position nextStep = characterPosition.shift(0, -1);
                            while (isThereAMonster(nextStep)) {
                                positionToMonster.get(nextStep).roam();
                            }
                            moveDownDisregardMonster();
                              m.decreaseHeart();
                              if (m.getHeart()>0) {
                                  m.dodge();
                               while (isThereAPlayer(m.getPosition())) {

                                   m.dodge();
                               }
                                    System.out.println("dodged once");
                            }



                              isAttacking = true;
                              readyForAttack = false;
                                if (m.getHeart() > 0) {
                                finalWorldFrame[m.position.x()][m.position.y()] = Tileset.MONSTER;

                            }



                        }
                    }

                }

                case 'a' -> {

                    ArrayList<Monster> monsters = getMonsters2TilesAroundCross(characterPosition);

                     for (Monster m : monsters) {
                        System.out.println("Monster at" + m.getPosition());
                        System.out.println("Avatar at" + characterPosition);
                        if (comparePositionRough(characterPosition, m.getPosition()) == 2) {

                            System.out.println("attacking");

                            moveLeftDisregardMonster();
                            moveLeftDisregardMonster();
                               Position nextStep = characterPosition.shift(-1, 0);
                            while (isThereAMonster(nextStep)) {
                                positionToMonster.get(nextStep).roam();
                            }
                            moveLeftDisregardMonster();
                              m.decreaseHeart();
                      if (m.getHeart()>0) {
                          m.dodge();
                               while (isThereAPlayer(m.getPosition())) {

                                   m.dodge();
                               }
                                    System.out.println("dodged once");
                            }

                                              isAttacking = true;
                                                                          readyForAttack = false;
                                                                            if (m.getHeart() > 0) {
                                finalWorldFrame[m.position.x()][m.position.y()] = Tileset.MONSTER;

                            }



                        }
                    }
                }

                case 'd' -> {
                    ArrayList<Monster> monsters = getMonsters2TilesAroundCross(characterPosition);
                           for (Monster m : monsters) {
                        System.out.println("Monster at" + m.getPosition());
                        System.out.println("Avatar at" + characterPosition);
                        if (comparePositionRough(characterPosition, m.getPosition()) == 3) {

                            System.out.println("attacking");

                            moveRightDisregardMonster();
                            moveRightDisregardMonster();
                                   Position nextStep = characterPosition.shift(1, 0);
                            while (isThereAMonster(nextStep)) {
                                positionToMonster.get(nextStep).roam();
                            }
                            moveRightDisregardMonster();
                              m.decreaseHeart();
                        if (m.getHeart()>0) {
                            m.dodge();
                               while (isThereAPlayer(m.getPosition())) {

                                   m.dodge();

                               }
                                    System.out.println("dodged once");
                            }

                                              isAttacking = true;
                                                                          readyForAttack = false;
                                                                            if (m.getHeart() > 0) {
                                finalWorldFrame[m.position.x()][m.position.y()] = Tileset.MONSTER;

                            }



                        }
                    }

                }
            }
            ter.attackSound();
        }

        public void setIsStunningMonster(boolean isStunningMonster) {
            this.isStunningMonster = isStunningMonster;
        }

        public int getHeart() {
            return heart;
        }

        public void setHeart(int heart) {
            this.heart = heart;
        }


        public void adjustHeart(int setHeart) {
            heart += setHeart;

        }

        public boolean checkPositionValid(Position position) {
            int x = position.x();
            int y = position.y();
            if(x>0&&x<WIDTH&& y>0&&y<HEIGHT) {
                return true;
            }
            return false;
        }

        public Avatar(Position position) {

            this.position = position;
            finalWorldFrame[position.x()][position.y()] = Tileset.AVATAR;
            characterPosition = position;
        }


        public Position getPosition() {
            return position;
        }

        public ArrayList<Monster> getMonsters2TilesAround() {
            ArrayList<Position> twoTilesAway = get2TilesAround(characterPosition);
            ArrayList<Monster> monsters = new ArrayList<>();
            for (Position p : twoTilesAway) {
                if(finalWorldFrame[p.x()][p.y()] == Tileset.MONSTER) {
                    Monster monster = positionToMonster.get(p);

                    monsters.add(monster);
                }
            }
            return monsters;

        }


           public ArrayList<Monster> getMonsters2TilesAroundCross(Position position) {
            ArrayList<Position> twoTilesAway = get2TilesAroundCross(position);
            ArrayList<Monster> monsters = new ArrayList<>();
            for (Position p : twoTilesAway) {
                if(finalWorldFrame[p.x()][p.y()] == Tileset.MONSTER) {
                    Monster monster = positionToMonster.get(p);

                    monsters.add(monster);
                }
            }
            return monsters;

        }

        //0 means up, 1 means down, 2 means left, 3 means right

        //TODO has a bug

        public int comparePosition(Position p1, Position p2) {
            //left
            if (p2.equals(p1.shift(-1, 1))) {
                return 2;
            }
            //up
              if (p2.equals(p1.shift(0, 1))) {
                return 0;
            }
              //right
                if (p2.equals(p1.shift(1, 1))) {
                return 3;
            }
                //ok
              if (p2.equals(p1.shift(-1, 0))) {
                return 2;
            }
              //ok
              if (p2.equals(p1.shift(1, 0))) {
                return 3;
            }

              if (p2.equals(p1.shift(-1, -1))) {
                return 2;
            }

              if (p2.equals(p1.shift(0, -1))) {
                return 1;
            }

              if (p2.equals(p1.shift(1, -1))) {
                return 3;
            }
            return -1;




        }

            //0 means up, 1 means down, 2 means left, 3 means right
           public int comparePositionRough(Position p1, Position p2) {
            //left
               int otherX = p2.x();
               int otherY = p2.y();
               int thisX = p1.x();
               int thisY = p1.y();
               if (otherY > thisY) {
                   System.out.println("up");

                   return 0;
               }
               if (otherY < thisY) {
                   System.out.println("down");
                   return 1;
               }
               if (otherX < thisX) {
                   System.out.println("left");
                   return 2;
               }
               if (otherX > thisX) {
                   System.out.println("right");

                   return 3;

               }

            return -1;




        }

        public void stun() {
            System.out.println(stamina);
            if (stamina < 90) {
                return;
            }

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARSTUN;

            ArrayList<Monster> monstersAround = getMonsters2TilesAroundCross(characterPosition);
            isStunningMonster = true;
            for(Monster monster : monstersAround) {
                monster.setIsStunned(true);
                readyForAttack = true;
                //0 means up, 1 means down, 2 means left, 3 means right
                int direction = comparePosition(characterPosition, monster.getPosition());
                switch (direction) {
                    case 0 -> {
                        monster.moveUp();
                        monster.moveUp();
                    }
                    case 1 -> {
                        monster.moveDown();
                        monster.moveDown();
                    }
                    case 2 -> {
                        monster.moveLeft();
                        monster.moveLeft();
                    }
                    case 3 -> {
                        monster.moveRight();
                        monster.moveRight();
                    }
                }

            }
            decreaseStamina(90);
            ter.chargeSound();

        }

        public void moveChar(char c) {
            //if the user used stunn, we do not use the normal move character
            //attack it self can move

            if (readyForAttack) {
                return;
            }

            switch (c) {
                case 'w' -> {
                    Position nextStep = characterPosition.shift(0, 1);
                    if (checkPositionValid(nextStep)) {
                        if (finalWorldFrame[nextStep.x()][nextStep.y()] == Tileset.POTION) {
                        stamina += 200;
                        ter.staminaSound();
                    }
                    }


                    moveUp();
                }
                case 's' -> {
                    Position nextStep = characterPosition.shift(0, -1);
                   if (checkPositionValid(nextStep)) {
                        if (finalWorldFrame[nextStep.x()][nextStep.y()] == Tileset.POTION) {
                        stamina += 200;
                        ter.staminaSound();
                    }
                    }


                    moveDown();
                }
                case 'a' -> {
                    Position nextStep = characterPosition.shift(-1, 0);
                   if (checkPositionValid(nextStep)) {
                        if (finalWorldFrame[nextStep.x()][nextStep.y()] == Tileset.POTION) {
                        stamina += 200;
                        ter.staminaSound();
                    }
                    }

                    moveLeft();
                }
                case 'd' -> {
                    Position nextStep = characterPosition.shift(1, 0);
                  if (checkPositionValid(nextStep)) {
                        if (finalWorldFrame[nextStep.x()][nextStep.y()] == Tileset.POTION) {
                        stamina += 200;
                        ter.staminaSound();
                    }
                    }

                    moveRight();
                }

            }
            increaseStamina();
            ter.walkSound();

        }

        public void otherMovement(char c) {
            switch (c) {
                case 'e' -> {
                    stun();
                }
                case 'w' -> {
                    if (readyForAttack) {
                        attack('w');
                    }

                }
                case 's' -> {
                    if (readyForAttack) {
                        attack('s');
                    }

                }
                case 'a' -> {
                    if (readyForAttack) {
                          attack('a');
                    }


                }
                case 'd' -> {
                    if (readyForAttack) {
                         attack('d');
                    }

                }
            }
        }


        public void moveLeft() {


            Position temp = characterPosition.shift(-1, 0);

            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(-1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARLEFTWITHSWORD;

            }

        }


         public void moveLeftDisregardMonster() {


            Position temp = characterPosition.shift(-1, 0);

            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) ) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(-1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARLEFTWITHSWORD;

            }

        }


        public void setAvatarToRightPosture(char c) {
            Position left = characterPosition.shift(-1, 0);
            Position right = characterPosition.shift(+1, 0);
            boolean isLeft = isBlockedTile(left);
            boolean isRight = isBlockedTile(right);


            if (isLeft && isRight) {
                switch (c) {
                    case 'w' -> {
                        finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACKWITHSWORD;

                        break;
                    }
                    case 's' -> {
                        finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;

                        break;

                    }
                }
            }


        }

        public void moveRight() {


            Position temp = characterPosition.shift(+1, 0);

            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(+1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARRIGHTWITHSWORD;

            }


        }

         public void moveRightDisregardMonster() {


            Position temp = characterPosition.shift(+1, 0);

            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) ) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(+1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARRIGHTWITHSWORD;

            }


        }


        public void setSwordLeft(char c) {
            if (c == 'd') {

                Position more = characterPosition.shift(-1, 0);
                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARSWORDL1;
                finalWorldFrame[more.x()][more.y()] = Tileset.AVATARSWORDL2;
            } else if (c == 'u') {
                Position more = characterPosition.shift(-1, 0);
                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACKSWORDL1;
                finalWorldFrame[more.x()][more.y()] = Tileset.AVATARBACKSWORDL2;
            }


        }

        public void setSwordRight(char c) {
            if (c == 'd') {
                Position more = characterPosition.shift(1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARSWORDR1;
                finalWorldFrame[more.x()][more.y()] = Tileset.AVATARSWORDR2;
            } else if (c == 'u') {
                Position more = characterPosition.shift(1, 0);

                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACKSWORDR1;
                finalWorldFrame[more.x()][more.y()] = Tileset.AVATARBACKSWORDR2;
            }

        }

        //if the left side is wall, put it on the right
        //if the right side has a wall put it on the left
        //if both side has a wall, return false.
        // and if so , the other method will set the sword at the character's back
        public boolean setSwordAvatar(char c) {
            Position left = characterPosition.shift(-1, 0);
            Position right = characterPosition.shift(+1, 0);

            boolean isLeftBlocked = isBlockedTile(left);

            boolean isRightBlocked = isBlockedTile(right);


            if (c == 'd' || c == 'u') {
                if (isLeftBlocked && isRightBlocked) {
                    return true;  // Both sides are blocked, return true
                } else if (isRightBlocked) {
                    setSwordLeft(c);
                    swordPosition = 0;
                } else if (isLeftBlocked) {
                    setSwordRight(c);
                    swordPosition = 1;
                } else {
                    if (swordPosition == 0) {
                        setSwordLeft(c);
                    } else {
                        setSwordRight(c);
                    }
                }
            }

            return false;
        }

        private boolean isBlockedTile(Position pos) {
            if (checkPositionValid(pos)) {
                 TETile tile = finalWorldFrame[pos.x()][pos.y()];
            boolean ret = tile.equals(Tileset.WALL) || tile == Tileset.MONSTER;

            return ret;
            }
            return false;

        }

        public void moveUp() {
            Position temp = characterPosition.shift(0, 1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {



                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(0, 1);
                boolean isBetween2Walls = setSwordAvatar('u');

                if (isBetween2Walls) {
                    finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACKWITHSWORD;

                }


//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }


        }

         public void moveUpDisregardMonster() {
            Position temp = characterPosition.shift(0, 1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {



                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL)) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(0, 1);
                boolean isBetween2Walls = setSwordAvatar('u');
                if (isBetween2Walls) {
                    finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACKWITHSWORD;

                }


//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }


        }





        public void moveDown() {


            Position temp = characterPosition.shift(0, -1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
                return;
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(0, -1);
                Position more = characterPosition.shift(-1, 0);

                boolean isBetween2Walls = setSwordAvatar('d');
                if (isBetween2Walls) {
                    finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;

                }

            }
        }


           public void moveDownDisregardMonster() {


            Position temp = characterPosition.shift(0, -1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
                return;
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) ) {
                    return;
                }
                resetChar();
                characterPosition = characterPosition.shift(0, -1);
                Position more = characterPosition.shift(-1, 0);

                boolean isBetween2Walls = setSwordAvatar('d');
                if (isBetween2Walls) {
                    finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATAR;

                }

            }
        }








    }




    /*
    *
    * final TETile AVATAR = new TETile('@', Color.white, Color.black, "you", "byow\\lab12\\CHRISFLOOR.png");
    public static final TETile AVATARLEFT = new TETile('L', Color.white, Color.black, "you", "byow\\lab12\\CHRISLEFTFLOOR.png");
    public static final TETile AVATARRIGHT = new TETile('R', Color.white, Color.black, "you", "byow\\lab12\\CHRISRIGHTFLOOR.png");
    public static final TETile AVATARBACK = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\CHRISBACKFLOOR.png");
    public static final TETile AVATARSWORDR1 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\RIGHTHANDSWORD1.png");
    public static final TETile AVATARSWORDR2 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\RIGHTHANDSWORD2.png");
    public static final TETile AVATARSWORDL1 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\LEFTHANDSWORD1.png");
      public static final TETile AVATARSWORDL2 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\LEFTHANDSWORD2.png");

      public static final TETile AVATARBACKSWORDL1 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\BACKSWORDLEFT1.png");
    public static final TETile AVATARBACKSWORDL2 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\BACKSWORDLEFT2.png");
    public static final TETile AVATARBACKSWORDR1 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\BACKSWORDRIGHT1.png");
        public static final TETile AVATARBACKSWORDR2 = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\BACKSWORDRIGHT2.png");
                public static final TETile AVATARBACKWITHSWORD = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\BACKWITHSWORD.png");
    public static final TETile AVATARLEFTWITHSWORD = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\TOLEFTSWORD.png");
    public static final TETile AVATARRIGHTWITHSWORD = new TETile('B', Color.white, Color.black, "you", "byow\\lab12\\TORIGHTSWORD.png");*/

    public boolean isThereAPlayer(Position position) {
        if(finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATAR)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARLEFT)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARRIGHT)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARBACK)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARSWORDR1)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARSWORDL1)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARBACKSWORDL1)

        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARBACKSWORDR1)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARBACKWITHSWORD)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARLEFTWITHSWORD)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARRIGHTWITHSWORD)
        ||finalWorldFrame[position.x()][position.y()].equals(Tileset.AVATARSTUN)
        ) {
            return true;
        }
        if (position.equals(characterPosition)) {
            return true;
        }
        return false;
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public class Monster {
        private Position position;
        private int HP;
        private boolean foundPlayer;
        private boolean isStunned;
        private boolean isKilled;
        private int heart = 3;


        public int getHeart() {
            return heart;
        }

        public void decreaseHeart() {
            if (heart - 1 >= 0) {
                heart--;
            }
        }



/*
        if the monster see the player is stuns, getStunned function will be executed
*/

        public void setIsKilled(boolean isKilled) {
            this.isKilled = isKilled;
        }

        public Monster(Position position) {
            this.position = position;
            finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;
            positionToMonster.put(position, this);

        }
        public void roamSlowly() {
            if (heart == 0) {
                isKilled = true;
            }
            if (isKilled) {
                monsters.remove(this);
                return;
            }
            int roamInt = rand.nextInt(3);

            switch(roamInt) {
                case 0 -> {
                    roam();
                }
                case 1 -> {
                    roam();
                }
                case 2 -> {
                    return;
                }
            }
        }

        public Position getPosition() {
            return this.position;

        }










        //if the left side is wall, put it on the right
        //if the right side has a wall put it on the left
        //if both side has a wall, return false.
        // and if so , the other method will set the sword at the character's back


        private boolean isBlockedTile(Position pos) {
            TETile tile = finalWorldFrame[pos.x()][pos.y()];
            boolean ret = tile.equals(Tileset.WALL) || tile == Tileset.MONSTER;

            return ret;
        }

      public double distance(Position me, Position other) {
        return Math.hypot(me.x() - other.x(), me.y() - other.y());
    }

        public void dodgeHard() {
            while (distance(position, characterPosition) <= 3) {
                 int dodgeInt = rand.nextInt(1);
            switch(dodgeInt) {
                case 0 -> {
                    moveUpFreely();
                    moveUpFreely();

                }
                case 1 -> {
                    moveDownFreely();
                    moveDownFreely();
                }
                case 2 -> {
                    moveLeftFreely();
                    moveLeftFreely();
                }
            }
            }
        }

        public void dodge() {
            int dodgeInt = rand.nextInt(2);
            System.out.println("dodge int is" +dodgeInt);
            boolean moved = false;
            switch(dodgeInt) {
                case 0 -> {
                    while (!moved) {
                        int dir = rand.nextInt(4);
                        switch (dir) {
                            case 1 -> {
                                moved = moveUpFreely();
                                System.out.println("move up");
                                break;
                            }
                            case 2 -> {
                                moved = moveDownFreely();
                                   System.out.println("move down");
                                break;
                            }
                            case 3 -> {
                                moved = moveLeftFreely();
                                   System.out.println("move left");
                                break;
                            }
                            case 4 -> {
                                moved = moveRightFreely();
                                   System.out.println("move right");
                                break;
                            }
                        }


                    }



                }




            }
        }


          public boolean moveUpFreely() {

            Position temp = position.shift(0, 1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                         ||finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION)|| isThereAPlayer(temp)) {
                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(0, 1);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }

            return true;


        }

        public boolean moveUp() {
            if (isStunned) {
                isStunned = false;
                return false;
            }
            Position temp = position.shift(0, 1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                         ||finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION)) {
                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(0, 1);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }

            return true;


        }


           public boolean moveDown() {
             if (isStunned) {
                isStunned = false;
                return false;
            }
            Position temp = position.shift(0, -1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                ||finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION)) {
                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(0, -1);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }

        public boolean moveDownFreely() {

            Position temp = position.shift(0, -1);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                        || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION) || isThereAPlayer(temp)) {

                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(0, -1);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }



           public boolean moveLeft() {

             if (isStunned) {
                isStunned = false;
                 return false;
            }
            Position temp = position.shift(-1, 0);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                ||finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION)) {
                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(-1, 0);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }


               public boolean moveLeftFreely() {
            Position temp = position.shift(-1, 0);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                        || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION) || isThereAPlayer(temp)) {

                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(-1, 0);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }

        public void setIsStunned(boolean isStunned) {
            this.isStunned = isStunned;
        }



           public boolean moveRight() {
             if (isStunned) {
                isStunned = false;
                 return false;
            }
            Position temp = position.shift(1, 0);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                ||finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION)) {
                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(1, 0);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }


                 public boolean moveRightFreely() {
             if (isStunned) {
                isStunned = false;
                 return false;
            }
            Position temp = position.shift(1, 0);
            int x = temp.x();
            int y = temp.y();
            if (!(x > 0 && x < WIDTH && y > 0 && y < HEIGHT)) {
            } else {
                if (finalWorldFrame[temp.x()][temp.y()].equals(Tileset.WALL) || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.MONSTER)
                        || finalWorldFrame[temp.x()][temp.y()].equals(Tileset.POTION) || isThereAPlayer(temp)) {

                    return false;
                }
                  resetToFloor(position);

                this.position = position.shift(1, 0);
                positionToMonster.put(position, this);


                finalWorldFrame[position.x()][position.y()] = Tileset.MONSTER;




//                finalWorldFrame[characterPosition.x()][characterPosition.y()] = Tileset.AVATARBACK;

            }
            return true;


        }

         public void roamFreely() {
              if (isKilled) {
                return;
            }



                boolean moved = false;
                //if it is not moved
                while (!moved) {
                     int roamInt = rand.nextInt(4);
                switch (roamInt) {
                    case 0 -> {
                        moved = moveUpFreely();
                        break;

                    }
                    case 1 -> {
                        moved = moveDownFreely();
                        break;
                    }
                    case 2 -> {
                        moved = moveLeftFreely();
                        break;
                    }
                    case 3 -> {

                        moved = moveRightFreely();
                        break;
                    }
                }
                }




        }




        public void roam() {
              if (isKilled) {
                return;
            }
            if (avatar.isAttacking) {
                     boolean moved = false;
                //if it is not moved
                while (!moved) {
                     int roamInt = rand.nextInt(4);
                switch (roamInt) {
                    case 0 -> {
                        moved = moveUp();
                        break;

                    }
                    case 1 -> {
                        moved = moveDown();
                        break;
                    }
                    case 2 -> {
                        moved = moveLeft();
                        break;
                    }
                    case 3 -> {

                        moved = moveRight();
                        break;
                    }
                }
                }
            }

            if (!foundPlayer) {
                boolean moved = false;
                //if it is not moved
                while (!moved) {
                     int roamInt = rand.nextInt(4);
                switch (roamInt) {
                    case 0 -> {
                        moved = moveUp();

                    }
                    case 1 -> {
                        moved = moveDown();
                    }
                    case 2 -> {
                        moved = moveLeft();
                    }
                    case 3 -> {

                        moved = moveRight();
                    }
                }
                }

            }


        }

        public void foundPlayer() {
            ArrayList<Position> aroundArea = get3TilesAround(position);
            for (Position p : aroundArea) {
                if (isThereAPlayer(p)) {
                    foundPlayer = true;
                }
            }
        }

        public void moveToCharacter1Step() {
              if (heart == 0) {
                isKilled = true;
            }
              if (isKilled) {
                return;
            }

             if (isStunned) {
                isStunned = false;
                return;
            }
            if (foundPlayer) {
                ArrayList<Position> path = findPathToCharacter(position);
                Position nextStep = path.get(1);
                //do not run into any player or monster
                if (finalWorldFrame[nextStep.x()][nextStep.y()] == Tileset.MONSTER||isThereAPlayer(nextStep)

                        ) {
                    if (isThereAPlayer(nextStep)) {
                        if (avatar.isStunningMonster) {
                            return;
                        } else {
                            avatar.adjustHeart(-1);
                        }


                    }

                    return;
                }
                resetToFloor(position);
                finalWorldFrame[nextStep.x()][nextStep.y()] = Tileset.MONSTER;
                this.position = nextStep;
                positionToMonster.put(position, this);
            }


        }


    }

    public class Sword {
        private final Position position;

        public Sword(Position position) {
            this.position = position;
            finalWorldFrame[position.x()][position.y()] = Tileset.SWORD;

        }
    }
}
