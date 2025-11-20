/**
 * CollectionPoint.java
 * Student Name: Stanislav Kril
 * Student Number: 3133810
 */

import java.util.*;

clectionPoint t<>();

    synchronized public void add(Point p) {
        list.add(p);
    }

    synchronized public boolean search(Point p) {
        return list.contains(p);
    }

    synchronized public List<Point> getAllX(int x) {
        List<Point> temp = new ArrayList<>();
        for (Point p : list) {
            if (p.getX() == x) {
                temp.add(p);
            }
        }
        return temp;
    }

    synchronized void replace(Point p1, Point p2) {
        for (Point p : list) {
            if (p.getX() == p1.getX() && p.getY() == p1.getY()) {
                list.set(list.indexOf(p1), p2);
            }
        }
    }

    synchronized public String toString() {
        String result = "";
        for (Point p : list) {
            result = result + " " + p.toString();
        }
        return result;
    }
}

