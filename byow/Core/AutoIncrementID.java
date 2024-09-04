package byow.Core;

public class AutoIncrementID {
    private static int nextId = 1; // Static variable to keep track of the next available ID
    private final int id;

    public AutoIncrementID() {
        this.id = nextId++;
    }

    public static void main(String[] args) {
        AutoIncrementID obj1 = new AutoIncrementID();
        AutoIncrementID obj2 = new AutoIncrementID();
        AutoIncrementID obj3 = new AutoIncrementID();

        System.out.println(obj1.getId()); // Output: 1
        System.out.println(obj2.getId()); // Output: 2
        System.out.println(obj3.getId()); // Output: 3
    }

    public int getId() {
        return id;
    }
}