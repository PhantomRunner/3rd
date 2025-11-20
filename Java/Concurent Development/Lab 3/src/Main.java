public class Main {
    public static void main(String[] args) {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(0, 4);

        Point missedPoint = new Point(3, 4);

        CollectionPoint collection = new CollectionPoint();


        collection.add(p1);
        collection.add(p2);
        collection.add(p3);

        System.out.println(collection);

        collection.replace(p1, missedPoint);
        System.out.println(collection);

        collection.replace(p3, new Point(111, 12));
        System.out.println(collection);


    }
}
