package byow.Core;

public interface DisjointSet<T> {

    /**
     * Creates a new DisjointSet with the supplied object
     *
     * @param object the value of the DisjointSet
     */
    void makeSet(T object);

    /**
     * Replaces the set containing x and the set containing y with their union.
     *
     * @param x first object
     * @param y second object
     */
    T union(T x, T y);

    /**
     * Returns the Representative Object of the supplied Object
     *
     * @param object Finds Representative of this
     */
    T find(T object);

    /**
     * Return true if supplied objects belong to same DisjointSet
     *
     * @param x first object
     * @param y second object
     */
    boolean isConnected(T x, T y);


}