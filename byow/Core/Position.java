package byow.Core;

import java.util.ArrayList;
import java.util.Objects;

public class Position {
    public ArrayList<Position> aroundArea;
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static void main(String[] args) {
        Position point = new Position(0, 0);
        System.out.println(point.calcAroundArea());

    }

    private static double distance(double lonV, double lonW, double latV, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the haversine distance squared between two positions, assuming
     * x represents the longitude and y represents the latitude.
     */
    public static double distance(Position p1, Position p2) {
        return distance(p1.x(), p2.x(), p1.y(), p2.y());
    }

    public ArrayList<Position> calcAroundArea() {
        aroundArea = new ArrayList<>();
        Position point1 = this.shift(-1, 1);
        Position point2 = point1.shift(1, 0);
        Position point3 = point2.shift(1, 0);
        Position point4 = point1.shift(0, -1);
        Position point5 = point4.shift(2, 0);
        Position point6 = point4.shift(0, -1);
        Position point7 = point6.shift(1, 0);
        Position point8 = point7.shift(1, 0);

        aroundArea.add(point1);

        aroundArea.add(point2);
        aroundArea.add(point3);
        aroundArea.add(point4);
        aroundArea.add(point5);
        aroundArea.add(point6);
        aroundArea.add(point7);
        aroundArea.add(point8);
        return aroundArea;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Position shift(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public boolean equals(Position other) {
        if (this.x == other.x && this.y == other.y) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.x, x) == 0 &&
                Double.compare(position.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }


}