package byow.Core;

import java.util.ArrayList;

public class Room {
    private static int nextID = 1; // Static variable to keep track of the next available ID
    private final int id;
    //room without the wall
    private final int width;
    private final int height;
    private final Position topLeft;
    private Position bottomRight;
    private Position center;
    private final ArrayList<Room> connectedTo;
    private ArrayList<Position> leftEdge;
    private ArrayList<Position> rightEdge;
    private ArrayList<Position> topEdge;
    private ArrayList<Position> bottomEdge;


    //room always need an anchor point

    public Room(int width, int height, Position topLeft) {
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        calculatePoints();
        connectedTo = new ArrayList<>();
        this.id = nextID++;

    }

    public Room(int width, int height, int topLeftX, int topLeftY) {
        this.width = width;
        this.height = height;
        this.topLeft = new Position(topLeftX, topLeftY);
        calculatePoints();
        connectedTo = new ArrayList<>();
        this.id = nextID++;

    }

    public static void main(String[] args) {

        Room room = new Room(7, 7, 20, 9);
        Room room1 = new Room(7, 7, 20, 10);

        System.out.println(room.getBottomRight());
        System.out.println(room.center);

        System.out.println(room.leftEdge);
        System.out.println(room.rightEdge);
        System.out.println(room.topEdge);
        System.out.println(room.bottomEdge);


    }

    public ArrayList<Position> getLeftEdge() {
        return leftEdge;
    }

    public ArrayList<Position> getRightEdge() {
        return rightEdge;
    }

    public ArrayList<Position> getTopEdge() {
        return topEdge;
    }

    public ArrayList<Position> getBottomEdge() {
        return bottomEdge;
    }

    public ArrayList<Room> getConnectedTo() {
        return connectedTo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public Position getBottomRight() {
        return bottomRight;
    }

    public Position center() {
        return this.center;
    }

    public void calculatePoints() {
        int bottomRightX = topLeft.x() + width - 1;
        int bottomRightY = topLeft.y() - height + 1;
        this.bottomRight = new Position(bottomRightX, bottomRightY);
        int centerX = topLeft.x() + Math.floorDiv(width, 2);
        int centerY = topLeft.y() - Math.floorDiv(height, 2);
        this.center = new Position(centerX, centerY);
        calculateLeftEdge();
        calculateRightEdge();
        calculateTopEdge();
        calculateBottomEdge();


    }

    public int ID() {
        return this.id;
    }

    public void calculateLeftEdge() {
        leftEdge = new ArrayList<>();
        //x does not change
        //y go to y-height +1, add all the points
        int X = topLeft.x();
        int Y = topLeft.y();
        for (int y = Y; y > Y - height; y--) {
            Position edge = new Position(X, y);
            leftEdge.add(edge);

        }
        leftEdge.remove(0);
        leftEdge.remove(leftEdge.size() - 1);

    }

    public void calculateRightEdge() {
        rightEdge = new ArrayList<>();

        int X = topLeft.x() + width - 1;
        int Y = topLeft.y();
        for (int y = Y; y > Y - height; y--) {
            Position edge = new Position(X, y);
            rightEdge.add(edge);

        }
        rightEdge.remove(0);
        rightEdge.remove(rightEdge.size() - 1);
    }


    public void calculateTopEdge() {
        topEdge = new ArrayList<>();
        //x will go to x + width -1
        //y does not change

        int X = topLeft.x();
        int Y = topLeft.y();
        for (int x = X; x < X + width; x++) {
            Position edge = new Position(x, Y);
            topEdge.add(edge);

        }

        topEdge.remove(0);
        topEdge.remove(topEdge.size() - 1);

    }

    public void calculateBottomEdge() {

        bottomEdge = new ArrayList<>();
        int X = topLeft.x();
        int Y = topLeft.y() - height + 1;
        for (int x = X; x < X + width; x++) {
            Position edge = new Position(x, Y);
            bottomEdge.add(edge);

        }

        bottomEdge.remove(0);
        bottomEdge.remove(bottomEdge.size() - 1);
    }


}
