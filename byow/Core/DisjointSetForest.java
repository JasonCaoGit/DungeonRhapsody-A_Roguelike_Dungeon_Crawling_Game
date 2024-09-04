package byow.Core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisjointSetForest<T> implements DisjointSet<T> {

    private final Map<T, T> set_holder;
    private final Map<T, Integer> rank;

    public DisjointSetForest() {
        set_holder = new HashMap<>();
        rank = new HashMap<>();
    }

    public DisjointSetForest(DisjointSetForest<T> disjointSetForest) {
        set_holder = new HashMap<>(disjointSetForest.set_holder);
        rank = new HashMap<>(disjointSetForest.rank);
    }

    public static void main(String[] args) {
        DisjointSetForest<Integer> dis = new DisjointSetForest<>();
        ArrayList<Integer> list = new ArrayList<>();
        dis.makeSet(0);
        dis.makeSet(1);
        dis.makeSet(2);
        list.add(0);
        list.add(1);
        list.add(2);
        dis.safeUnion(0, 1);
        dis.safeUnion(1, 2);
        dis.makeSet(4);
        dis.makeSet(5);
        list.add(4);
        list.add(5);
        dis.safeUnion(4, 5);


        System.out.println(dis);
        System.out.println(dis.countDisjointSets(list, dis));

    }

    public void makeSet(T object) {
        if (set_holder.containsKey(object))
            return;

        set_holder.put(object, object);
        rank.put(object, 0);
    }

    public boolean isConnected(T x, T y) {
        return find(x).equals(find(y));
    }

    public T find(T object) {
        return findByPathCompression(object);
    }

    private T findByPathCompression(T object) {
        if (!set_holder.containsKey(object)) {
            makeSet(object);
            return object;
        }

        if (set_holder.get(object).equals(object))
            return object;
        else {
            T rep = findByPathCompression(set_holder.get(object));
            set_holder.put(object, rep);
            return rep;
        }
    }

    public T union(T x, T y) {
        T result = unionByRank(x, y);
        return result;
    }

    private T unionByRank(T x, T y) {
        T x_rep = findByPathCompression(x);
        T y_rep = findByPathCompression(y);


        if (x_rep == null || y_rep == null) {
            return null;
        }

        if (x_rep.equals(y_rep))
            return x_rep;

        if (rank.get(x_rep) > rank.get(y_rep)) {
            set_holder.put(y_rep, x_rep);
            return x_rep;
        } else if (rank.get(x_rep) < rank.get(y_rep)) {
            set_holder.put(x_rep, y_rep);
            return y_rep;
        } else {
            set_holder.put(y_rep, x_rep);
            rank.put(x_rep, rank.get(x_rep) + 1);
            return x_rep;
        }
    }

    public void safeUnion(T x, T y) {
        makeSet(x);
        makeSet(y);
        union(x, y);
    }

    @Deprecated
    public Map<T, T> getSetHolder() {
        return this.set_holder;
    }

    @Deprecated
    public List<List<T>> getDisjointSets() {
        Map<T, List<T>> disjoint_sets_map = new HashMap<>();
        List<List<T>> disjoint_sets = new ArrayList<>();

        for (Map.Entry<T, T> mapEntry : set_holder.entrySet()) {
            T value = mapEntry.getValue();
            T key = mapEntry.getKey();
            T rep = findByPathCompression(value);

            if (!disjoint_sets_map.containsKey(rep))
                disjoint_sets_map.put(rep, Stream.of(value).collect(Collectors.toList()));
            else
                disjoint_sets_map.get(rep).add(key);
        }

        for (Map.Entry<T, List<T>> mapEntry : disjoint_sets_map.entrySet()) {
            List<T> value = mapEntry.getValue();
            disjoint_sets.add(value);
        }

        return disjoint_sets;
    }

    @Override
    public String toString() {
        Map<T, Set<T>> groups = new HashMap<>();

        // Group elements by their representatives
        for (T element : set_holder.keySet()) {
            T rep = find(element);
            groups.computeIfAbsent(rep, k -> new TreeSet<>()).add(element);
        }

        // Sort the groups by their first element and format each group
        return groups.values().stream()
                .sorted(Comparator.comparing(set -> set.iterator().next().toString()))
                .map(set -> set.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("-")))
                .collect(Collectors.joining("     "));
    }

    public int countDisjointSets(ArrayList<Integer> list, DisjointSetForest<Integer> disjointSetForest) {
        Set<Integer> representatives = new HashSet<>();
        for (int id : list) {
            representatives.add(disjointSetForest.find(id));
        }
        return representatives.size();
    }
}