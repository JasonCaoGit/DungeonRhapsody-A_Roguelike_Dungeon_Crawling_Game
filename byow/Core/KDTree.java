package byow.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree implements Cloneable {
    private List<Position> positions;
    private Node root;


    public KDTree(List<Position> positions) {
        this.positions = positions;
        root = new Node(positions.get(0), Orientation.X);
        buildTreeFromPositions();
    }

    public static void main(String[] args) {
        ArrayList<Position> positions = new ArrayList<Position>();
        positions.add(new Position(0, 0));
        positions.add(new Position(-2, 1));
        positions.add(new Position(5, 6));
        positions.add(new Position(4, 2));
        positions.add(new Position(8, 3));
        Random rand = new Random();
        for (int i = 0; i < 10000000; i++) {
            positions.add(new Position(rand.nextInt(1000), rand.nextInt(1000)));
        }
        KDTree tree = new KDTree(positions);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1200; i++) {
            tree.nearest(rand.nextInt(1000), rand.nextInt(1000));
        }
        long end = System.currentTimeMillis();
        System.out.println("Total time used " + (end - start) / 1000.0 + " seconds");
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.getLeftChild()) + size(node.getRightChild());
    }

    public boolean isWorthIt(Node n, Position goal, double bestDistance) {
        Orientation orientation = n.getOrientation();
        double currentDistance;
        if (orientation == Orientation.X) {
            currentDistance = Math.abs(goal.x() - n.getPosition().x());
        } else {
            currentDistance = Math.abs(goal.y() - n.getPosition().y());
        }
        return currentDistance < bestDistance;
    }

    public Node nearest(Node n, Position goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Position.distance(n.getPosition(), goal) < Position.distance(best.getPosition(), goal)) {
            best = n;
        }
        Node goalNode = new Node(goal);
        Node goodSide;
        Node badSide;
        if (n.compareTo(goalNode) > 0) {
            goodSide = n.getLeftChild();
            badSide = n.getRightChild();
        } else {
            goodSide = n.getRightChild();
            badSide = n.getLeftChild();
        }
        best = nearest(goodSide, goal, best);
        double bestDistance = Position.distance(best.getPosition(), goal);
        if (isWorthIt(n, goal, bestDistance)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }

    public Position nearest(int x, int y) {
        Position goal = new Position(x, y);
        Node bestNode = nearest(root, goal, root);
        return bestNode.getPosition();
    }

    public void buildTreeFromPositions() {
        for (int i = 1; i < positions.size(); i++) {
            insert(positions.get(i));
        }
    }

    public void insert(Position position) {
        root = insert(root, position, 0);
    }

    public Node insert(Node node, Position position, int depth) {
        if (node == null) {
            Orientation orientation = (depth % 2 == 0) ? Orientation.X : Orientation.Y;
            return new Node(position, orientation);
        }
        Node insert = new Node(position);
        if (node.compareTo(insert) > 0) {
            node.setLeftChild(insert(node.getLeftChild(), position, depth + 1));
        } else {
            node.setRightChild(insert(node.getRightChild(), position, depth + 1));
        }
        return node;
    }

    public void remove(Position position) {
        root = remove(root, position, 0);
    }

    private Node remove(Node node, Position position, int depth) {
        if (node == null) {
            return null;
        }

        Orientation orientation = (depth % 2 == 0) ? Orientation.X : Orientation.Y;

        if (node.getPosition().equals(position)) {
            if (node.getRightChild() != null) {
                Node minNode = findMin(node.getRightChild(), orientation, depth + 1);
                node.position = minNode.position;
                node.setRightChild(remove(node.getRightChild(), minNode.position, depth + 1));
            } else if (node.getLeftChild() != null) {
                Node minNode = findMin(node.getLeftChild(), orientation, depth + 1);
                node.position = minNode.position;
                node.setLeftChild(remove(node.getLeftChild(), minNode.position, depth + 1));
            } else {
                return null;
            }
        } else if (comparePositions(position, node.getPosition(), orientation) < 0) {
            node.setLeftChild(remove(node.getLeftChild(), position, depth + 1));
        } else {
            node.setRightChild(remove(node.getRightChild(), position, depth + 1));
        }

        return node;
    }

    private Node findMin(Node node, Orientation orientation, int depth) {
        if (node == null) {
            return null;
        }

        Orientation currentOrientation = (depth % 2 == 0) ? Orientation.X : Orientation.Y;

        if (currentOrientation == orientation) {
            if (node.getLeftChild() == null) {
                return node;
            }
            return findMin(node.getLeftChild(), orientation, depth + 1);
        }

        Node leftMin = findMin(node.getLeftChild(), orientation, depth + 1);
        Node rightMin = findMin(node.getRightChild(), orientation, depth + 1);
        Node currentMin = node;

        if (leftMin != null && comparePositions(leftMin.getPosition(), currentMin.getPosition(), orientation) < 0) {
            currentMin = leftMin;
        }
        if (rightMin != null && comparePositions(rightMin.getPosition(), currentMin.getPosition(), orientation) < 0) {
            currentMin = rightMin;
        }

        return currentMin;
    }

    private int comparePositions(Position p1, Position p2, Orientation orientation) {
        if (orientation == Orientation.X) {
            return Integer.compare(p1.x(), p2.x());
        } else {
            return Integer.compare(p1.y(), p2.y());
        }
    }

    @Override
    public KDTree clone() {
        try {
            KDTree clonedTree = (KDTree) super.clone();
            clonedTree.positions = new ArrayList<>(this.positions);
            clonedTree.root = cloneNode(this.root);
            return clonedTree;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // This should never happen
        }
    }

    private Node cloneNode(Node node) {
        if (node == null) {
            return null;
        }
        Node clonedNode = new Node(node.getPosition(), node.getOrientation());
        clonedNode.setLeftChild(cloneNode(node.getLeftChild()));
        clonedNode.setRightChild(cloneNode(node.getRightChild()));
        return clonedNode;
    }

    private class Node implements Cloneable {
        private Position position;
        private Orientation orientation;
        private Node leftChild;
        private Node rightChild;
        private int depth;

        public Node(Position position, Orientation orientation) {
            this.position = position;
            this.orientation = orientation;
        }

        public Node(Position position, Orientation orientation, int depth) {
            this.position = position;
            this.orientation = orientation;
            this.depth = depth;
        }

        public Node(Position position) {
            this.position = position;
        }

        private void setOrientation() {
            orientation = (depth % 2 == 0) ? Orientation.X : Orientation.Y;
        }

        public Orientation getOrientation() {
            return orientation;
        }

        public void setOrientation(Orientation orientation) {
            this.orientation = orientation;
        }

        public Position getPosition() {
            return this.position;
        }

        public Node getLeftChild() {
            return this.leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return this.rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }

        public double compareTo(Node another) {
            switch (orientation) {
                case X:
                    return this.getPosition().x() - another.getPosition().x();
                default:
                    return this.getPosition().y() - another.getPosition().y();
            }
        }

        @Override
        protected Node clone() {
            try {
                Node clonedNode = (Node) super.clone();
                clonedNode.position = new Position(this.position.x(), this.position.y());
                // Note: leftChild and rightChild will be set by the cloneNode method
                return clonedNode;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(); // This should never happen
            }
        }
    }
}