package byow.Core;

public class Direction {
    private final int dx;
    private final int dy;
    private RelativePosition relativePosition;

    public Direction(Position point, Position other) {
        dx = Math.abs(point.x() - other.x());
        dy = Math.abs(point.y() - other.y());
        //determine the relative Position
        if (other.y() > point.y()) {
            if (other.x() >= point.x()) {
                relativePosition = RelativePosition.TOPRIGHT;
            } else {
                relativePosition = RelativePosition.TOPLEFT;
            }
        }
        if (point.y() == other.y()) {
            if (other.x() > point.x()) {
                relativePosition = RelativePosition.TOPRIGHT;
            } else {
                relativePosition = RelativePosition.BOTTOMLEFT;
            }
        }

        if (other.y() < point.y()) {
            if (other.x() > point.x()) {
                relativePosition = RelativePosition.BOTTOMRIGHT;
            } else {
                relativePosition = RelativePosition.BOTTOMLEFT;
            }
        }

    }

    public static void main(String[] args) {
        Position pos1 = new Position(3, 12);
        Position pos2 = new Position(12, 13);
        Direction dir = new Direction(pos1, pos2);
        System.out.println(dir.relativePosition);
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;

    }

    public RelativePosition getRelativePosition() {
        return relativePosition;
    }

}
