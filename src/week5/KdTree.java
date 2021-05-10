package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root = null;

    public KdTree() {

    }                           // construct an empty set of points

    // is the set empty?
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }
        return this.root.count == 0;
    }

    // number of points in the set
    public int size() {
        if (this.isEmpty()) {
            return 0;
        }
        return this.root.count + 1;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.root = this.insert(p, this.root, 0);
        if (this.size() == 5) {
            StdOut.println(1);
        }

    }

    // (0.206107, 0.095492)
    private Node insert(Point2D p, Node node, int width) {
        if (node == null) {
            node = new Node();
            node.item = p;
            node.width = width;
            node.count = 0;
            return node;
        }
        int cmp = node.compareTo(p);
        if (cmp < 0) {
            node.count++;
            node.left = insert(p, node.left, ++width);
        } else if (cmp > 0) {
            node.count++;
            node.right = insert(p, node.right, ++width);
        } else {
            node.item = p;
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node node = this.root;
        while (node.item != null) {
            int cmp = node.compareTo(p);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return true;
            }
        }
        return false;
    }


    // draw all points to standard draw
    public void draw() {
        draw(this.root, 0, 0, 1.0, 1.0);
    }

    private void draw(Node node, double xmin, double ymin, double xmax, double ymax) {
        if (null == node) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.item.draw();

        if (node.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            // draw red vertical line
            RectHV rect = new RectHV(node.item.x(), ymin, node.item.x(), ymax);
            rect.draw();
            draw(node.right, node.item.x(), ymin, xmax, ymax);
            draw(node.left, xmin, ymin, node.item.x(), ymax);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            // draw blue horizontal line
            RectHV rect = new RectHV(xmin, node.item.y(), xmax, node.item.y());
            rect.draw();
            draw(node.right, xmin, node.item.y(), xmax, ymax);
            draw(node.left, xmin, ymin, xmax, node.item.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> stack = new Stack<Point2D>();

        if (this.isEmpty()) {
            return stack;
        }
        if (rect.contains(this.root.item)) {
            stack.push(this.root.item);
        }
        this.range(rect, new RectHV(this.root.item.x(), 0, 1.0, 1.0), this.root.right, stack);
        this.range(rect, new RectHV(0, 0, this.root.item.x(), 1.0), this.root.left, stack);
        return stack;
    }

    private void range(RectHV rect, RectHV bufRect, Node node, Stack<Point2D> stack) {
        if (node == null) {
            return;
        }
        if (!rect.intersects(bufRect)) return;
        if (rect.contains(node.item)) {
            stack.push(node.item);
        }

        if (node.isVertical()) {
            double ymin = bufRect.ymin();
            double ymax = bufRect.ymax();


            double xmin = node.item.x();
            double xmax = bufRect.xmax();

            range(rect, new RectHV(xmin, ymin, xmax, ymax), node.right, stack);

            xmin = bufRect.xmin();
            xmax = node.item.x();

            range(rect, new RectHV(xmin, ymin, xmax, ymax), node.left, stack);


        } else {
            double xmin = bufRect.xmin();
            double xmax = bufRect.xmax();

            double ymin = node.item.y();
            double ymax = bufRect.ymax();

            range(rect, new RectHV(xmin, ymin, xmax, ymax), node.right, stack);

            ymin = bufRect.ymin();
            ymax = node.item.y();

            range(rect, new RectHV(xmin, ymin, xmax, ymax), node.left, stack);


        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (this.isEmpty()) {
            return null;
        }

        Node nearestP = new Node();
        nearestP.item = this.root.item;
        nearestP.left = this.root.left;
        nearestP.right = this.root.right;
       this.nearest(p, new RectHV(0, 0, 1.0, 1.0), this.root, nearestP);

       return nearestP.item;
    }

    private void nearest(Point2D p, RectHV bufRect, Node node, Node min) {
        if (node == null) {
            return;
        }

        if (min.item.distanceSquaredTo(p) > node.item.distanceSquaredTo(p)) {
            min.item = node.item;
        }

        if (node.isVertical()) {
            double ymin = bufRect.ymin();
            double ymax = bufRect.ymax();

            double xmin = node.item.x();
            double xmax = bufRect.xmax();

            RectHV rightRect = new RectHV(xmin, ymin, xmax, ymax);
            xmin = bufRect.xmin();
            xmax = node.item.x();
            RectHV leftRect = new RectHV(xmin, ymin, xmax, ymax);
            // right

            if (node.item.x() <= p.x()) {
                this.nearest(p,rightRect, node.right, min);
                if (leftRect.distanceSquaredTo(p) < p.distanceSquaredTo(min.item)) {
                    this.nearest(p,leftRect, node.left, min);
                }
                // left
            } else  {
                this.nearest(p,leftRect, node.left, min);
                if (rightRect.distanceSquaredTo(p) < p.distanceSquaredTo(min.item)) {
                    this.nearest(p,rightRect, node.right, min);
                }
            }
        } else {
            double xmin = bufRect.xmin();
            double xmax = bufRect.xmax();

            double ymin = node.item.y();
            double ymax = bufRect.ymax();

            RectHV rightRect = new RectHV(xmin, ymin, xmax, ymax);
            ymin = bufRect.ymin();
            ymax = node.item.y();

            RectHV leftRect = new RectHV(xmin, ymin, xmax, ymax);

            if (node.item.y() <= p.y()) {
                // рассмытривем только right
                this.nearest(p,rightRect, node.right, min);
                if (leftRect.distanceSquaredTo(p) < p.distanceSquaredTo(min.item)) {
                     this.nearest(p,leftRect, node.left, min);
                }
                // рассмытривем только left
            } else  {
                this.nearest(p,leftRect, node.left, min);
                if (rightRect.distanceSquaredTo(p) < p.distanceSquaredTo(min.item)) {
                     this.nearest(p, rightRect, node.right, min);
                }
            }
        }
        return;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        int c = 0;
        while (!in.isEmpty()) {
            c++;
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            if (c == 7) {
                StdOut.println(1);
            }
            kdtree.insert(p);
        }

        kdtree.nearest(new Point2D(0.2, 0.4));
        kdtree.draw();
    }

    private static class Node implements Comparable {
        public int count = 0;
        public int width = 0;
        public Node left = null;
        public Node right = null;
        public Point2D item = null;

        public Node() {

        }

        public boolean isVertical() {
            return width % 2 == 0;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Point2D) {
                Point2D p = (Point2D) o;

                if (this.isVertical()) {
                    if (p.x() < item.x()) {
                        return -1;
                    } else if (p.x() > item.x()) {
                        return 1;
                    } else {
                        if (p.y() < item.y()) {
                            return -1;
                        } else if (p.y() > item.y()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                } else {
                    if (p.y() < item.y()) {
                        return -1;
                    } else if (p.y() > item.y()) {
                        return 1;
                    } else {
                        if (p.x() < item.x()) {
                            return -1;
                        } else if (p.x() > item.x()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            throw new IllegalArgumentException();
        }
    }
}