package byow.Core;

public class WeightedEdge<Vertex> {
    private final Vertex v;
    private final Vertex w;
    private final double weight;

    public WeightedEdge(Vertex v, Vertex w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public Vertex from() {
        return v;
    }

    public Vertex to() {
        return w;
    }

    public double weight() {
        return weight;
    }
}