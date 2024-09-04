package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class GameGraph<Vertex> implements AStarGraph<Position> {

    private final ArrayList<Position> allFloorPositions;


    public GameGraph(ArrayList<Position> allFloorPositions) {
        this.allFloorPositions = allFloorPositions;
    }


    public static void main(String[] args) {
        Position s = new Position(0, 0);
        Position goal = new Position(0, 1);
        ArrayList<Position> floors = new ArrayList<>();
        floors.add(goal);
        floors.add(new Position(0, -1));

        GameGraph<Position> g = new GameGraph<Position>(floors);
        System.out.println(g.estimatedDistanceToGoal(s, goal));
        System.out.println(g.neighbors(s));
    }

    public void addNeighborTiles(ArrayList<WeightedEdge<Position>> neighbors, WeightedEdge edge) {
        if (allFloorPositions.contains(edge.to())) {
            neighbors.add(edge);

        } else {
        }
    }

    public List<WeightedEdge<Position>> neighbors(Position v) {
        Position vertex = v;

        ArrayList<WeightedEdge<Position>> neighbors = new ArrayList<>();
        Position up = vertex.shift(0, 1);
        Position down = vertex.shift(0, -1);
        Position left = vertex.shift(-1, 0);
        Position right = vertex.shift(1, 0);
        WeightedEdge upEdge = new WeightedEdge(vertex, up, 1);
        WeightedEdge downEdge = new WeightedEdge(vertex, down, 1);
        WeightedEdge leftEdge = new WeightedEdge(vertex, left, 1);
        WeightedEdge rightEdge = new WeightedEdge(vertex, right, 1);
        addNeighborTiles(neighbors, upEdge);
        addNeighborTiles(neighbors, downEdge);
        addNeighborTiles(neighbors, leftEdge);
        addNeighborTiles(neighbors, rightEdge);
        return neighbors;
    }


    public double estimatedDistanceToGoal(Position s, Position goal) {
        Position source = s;
        Position target = goal;
        double distance = Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
        return distance;
    }

}
