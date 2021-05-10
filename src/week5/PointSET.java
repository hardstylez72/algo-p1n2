package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> treeSet;

    public PointSET() {
        this.treeSet = new TreeSet<>();
    }                           // construct an empty set of points

    // is the set empty?
    public boolean isEmpty() {
        return this.treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.treeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : this.treeSet) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D point2D : this.treeSet) {
            if (rect.contains(point2D)) {
                stack.push(point2D);
            }
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.isEmpty()) {
            return null;
        }

        Point2D nearest = this.treeSet.first();
        for (Point2D point2D : this.treeSet) {
            if (nearest.distanceSquaredTo(p) > point2D.distanceSquaredTo(p)) {
                nearest = point2D;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}