package byow.lab12.out.production.lab12.byow.lab12;

import java.awt.*;
import java.util.HashMap;

public class Hexagon {
    private Point topLeft;
    private Point middleRight;
    private Point bottomRight;
    private int length;

    public int getLength() {

        return this.length;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getMiddleRight() {
        return middleRight;

    }

    public Point getBottomRight() {
        return bottomRight;
    }


    ///Test the coordinates too!
    public static void main(String[] args) {
        Hexagon hex = new Hexagon(0, 0, 3);
        hex.calculateCoordinates();
        System.out.println(hex.getTopLeft());
        System.out.println(hex.getMiddleRight());
    }
    public Hexagon(int topLeftX, int topLeftY, int length) {

        this.topLeft = new Point(topLeftX, topLeftY);
        this.length = length;
        this.calculateCoordinates();

    }


    public void calculateCoordinates() {

        int topLeftX = topLeft.x;
        int topLeftY = topLeft.y;
        int middleRightX = -1;
        int middleRightY = -1;
        int bottomRightX = -1;
        int bottomRightY = -1;
        switch (length) {
            case 2:

                middleRightX= topLeftX + 2;
                middleRightY = topLeftY - 2;
                middleRight = new Point(middleRightX, middleRightY);
                bottomRightX = topLeftX + 1;
                bottomRightY = topLeftY - 3;
                bottomRight = new Point(bottomRightX, bottomRightY);
                break;

            case 3:
                middleRightX = topLeftX + 4;
                middleRightY = topLeftY -3;
                middleRight = new Point(middleRightX, middleRightY);
                bottomRightX = topLeftX + 2;
                bottomRightY = topLeftY - 5;
                bottomRight = new Point(bottomRightX, bottomRightY);

                break;


            case 4:
                middleRightX = topLeftX + 6;
                middleRightY = topLeftY - 4;
                middleRight = new Point(middleRightX, middleRightY);
                bottomRightX = topLeftX + 3;
                bottomRightY = topLeftY - 7;
                bottomRight = new Point(bottomRightX, bottomRightY);

                break;

            case 5:
                middleRightX = topLeftX + 8;
                middleRightY = topLeftY - 5;
                middleRight = new Point(middleRightX, middleRightY);
                bottomRightX = topLeftX + 4;
                bottomRightY = topLeftY - 9;
                bottomRight = new Point(bottomRightX, bottomRightY);



                break;





        }


    }




}
