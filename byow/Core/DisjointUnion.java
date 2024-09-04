package byow.Core;

import java.util.Arrays;

public class DisjointUnion {
    private final int[] parent;
    private final int[] rank;
    private final int size;

    public DisjointUnion(int size) {
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public static void main(String[] args) {
        int size = 10;
        DisjointUnion du = new DisjointUnion(size);

        // Create some unions
        du.union(11, 2);
        du.union(3, 4);
        du.union(1, 3);
        du.union(5, 6);

        // Print the parent array to see the structure
        System.out.println("Parent array: " + Arrays.toString(du.parent));

        // Find two elements in different sets
        int[] differentSet = du.findTwoInDifferentSets();
        if (differentSet != null) {
            System.out.println("Two elements in different sets: " + differentSet[0] + " and " + differentSet[1]);
        } else {
            System.out.println("All elements are in the same set");
        }

        // Demonstrate that the found elements are indeed in different sets
        if (differentSet != null) {
            System.out.println("Root of " + differentSet[0] + ": " + du.find(differentSet[0]));
            System.out.println("Root of " + differentSet[1] + ": " + du.find(differentSet[1]));
        }
    }

    public int find(int x) {
        if (x < 0 || x >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    public int[] findTwoInDifferentSets() {
        if (size <= 1) {
            return null; // Not possible to find two elements in different sets
        }

        int firstSetRepresentative = find(0);
        for (int i = 1; i < size; i++) {
            if (find(i) != firstSetRepresentative) {
                return new int[]{0, i}; // Found two elements in different sets
            }
        }

        return null; // All elements are in the same set
    }
}